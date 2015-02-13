/**
 * This software was developed and / or modified by Raytheon Company,
 * pursuant to Contract DG133W-05-CQ-1067 with the US Government.
 * 
 * U.S. EXPORT CONTROLLED TECHNICAL DATA
 * This software product contains export-restricted data whose
 * export/transfer/disclosure is restricted by U.S. law. Dissemination
 * to non-U.S. persons whether in the United States or abroad requires
 * an export license or other authorization.
 * 
 * Contractor Name:        Raytheon Company
 * Contractor Address:     6825 Pine Street, Suite 340
 *                         Mail Stop B8
 *                         Omaha, NE 68106
 *                         402.291.0100
 * 
 * See the AWIPS II Master Rights File ("Master Rights File.pdf") for
 * further licensing information.
 **/
package com.raytheon.uf.edex.plugin.goesr.decoder;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.measure.unit.Unit;

import ucar.ma2.Array;
import ucar.nc2.Attribute;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

import com.raytheon.uf.common.dataplugin.satellite.SatMapCoverage;
import com.raytheon.uf.common.dataplugin.satellite.SatelliteMessageData;
import com.raytheon.uf.common.dataplugin.satellite.SatelliteRecord;
import com.raytheon.uf.common.datastorage.records.IDataRecord;
import com.raytheon.uf.common.status.IUFStatusHandler;
import com.raytheon.uf.common.status.UFStatus;
import com.raytheon.uf.common.time.DataTime;
import com.raytheon.uf.edex.plugin.goesr.decoder.geo.GOESRProjection;
import com.raytheon.uf.edex.plugin.goesr.decoder.geo.GOESRProjectionException;
import com.raytheon.uf.edex.plugin.goesr.decoder.geo.GOESRProjectionFactory;

/**
 * This decoder attempts to open a potential GOES-R netCDF file, decode the data
 * contained in it, and make it available to be stored.
 * 
 * <pre>
 * The code implements the 
 * Ground Segment (GS) to Advanced Weather Interactive Processing System (AWIPS)
 * Interface Control Document (ICD)
 * DOCUMENT CONTROL NUMBER: 7034704 CDRL SE-08 REVISION B
 * Date 31 MAY 2012
 * *****
 * Some variances between the code and the revision are noted. These are
 * due to discrepancies between the ICD and the sample data. These have
 * been reported.
 * 
 * <pre>
 * 
 * SOFTWARE HISTORY
 * 
 * Date         Ticket#    Engineer    Description
 * ------------ ---------- ----------- --------------------------
 * May 31, 2012        796 jkorman     Initial creation
 * Jul  5, 2013       2123 mschenke    Changed to use in-memory netcdf object
 * Feb 13, 2015       4043 bsteffen    Include scene number in secctor.
 * 
 * </pre>
 * 
 * @author jkorman
 * @version 1.0
 */

public class GOESRNetCDFDecoder implements GOESRDataDecoder {

    private static final transient IUFStatusHandler log = UFStatus
            .getHandler(GOESRNetCDFDecoder.class);

    private final Pattern SCENE_PATTERN = Pattern.compile("-\\d{1,3}$");

    private GOESRProjectionFactory projFactory;

    private NetcdfFile cdfFile;

    private GOESRAttributes attributes;

    /**
     * Create an instance of a GOESRFileDecoder.
     * 
     * @param path
     *            File path to the GOES-R data to be decoded.
     * @param wmo
     *            The WMOHeader of the data.
     * @param name
     *            The plugin name.
     * @param projFactory
     *            The projection factory to use for generating projection
     *            information from the data.
     * @throws DecoderException
     *             Thrown for various errors while constructing the decoder.
     */
    public GOESRNetCDFDecoder(NetcdfFile netcdfFile,
            GOESRProjectionFactory projFactory) {
        this.cdfFile = netcdfFile;
        this.projFactory = projFactory;
        this.attributes = new GOESRAttributes(cdfFile);
    }

    /**
     * Decode the GOES-R data and return the data in a SatelliteRecord. If an
     * error occurs or the decoder is unable to decode the data, a null
     * reference may be returned.
     * 
     * @return The decoded GOES-R satellite data.
     */
    @Override
    public SatelliteRecord decodeFile() {
        SatelliteRecord rec = null;

        try {
            rec = readCMIData();
        } catch (Exception e) {
            log.error("Could not read the netCDF data file", e);
            rec = null;
        }
        if (rec != null) {
            // Set the satellite altitude in kilometers
            rec.setSatHeight(attributes.getSatellite_altitude().intValue() / 1000);

            rec.setDataTime(new DataTime(attributes.getStart_date_time()));

            // datauri(1)
            rec.setSource(attributes.getProduction_location());

            // datauri(2)
            rec.setCreatingEntity(attributes.getSatellite_id());

            String[] parts = attributes.getProduct_name().split("-");
            // datauri(3)
            /*
             * The ICD indicates all the regions are upper case but previous
             * revisions included lower case variants, to ensure that all data,
             * even older test data gets a consistent sector always force upper
             * case.
             */
            String sector = parts[0].toUpperCase();
            Matcher sceneMatcher = SCENE_PATTERN.matcher(attributes
                    .getSource_scene());
            if (sceneMatcher.matches()) {
                sector = sector + sceneMatcher.group(0);
            }
            rec.setSectorID(sector);
            String physicalElement = String.format("CH-%02d-%4.2fum",
                    attributes.getChannel_id(),
                    attributes.getCentral_wavelength());
            // datauri(4)
            rec.setPhysicalElement(physicalElement);

            SatelliteMessageData messageData = (SatelliteMessageData) rec
                    .getMessageData();
            IDataRecord dataRec = messageData.getStorageRecord(rec,
                    SatelliteRecord.SAT_DATASET_NAME);
            rec.setMessageData(dataRec);

            StringBuilder message = new StringBuilder("GOES-R");
            message.append("|");
            synchronized (GOESRConstants.GOESR_DATETIME) {
                message.append(GOESRConstants.GOESR_DATETIME.format(attributes
                        .getStart_date_time()));
            }
            message.append("|");
            message.append(attributes.getProduct_name());
            message.append("|");

            log.info(message.toString());
        }
        return rec;
    }

    /**
     * Close both the netCDF instance and also delete the temporary data file.
     * Note that this method must be called before disposing of the decoder
     * instance to release decoder resources.
     */
    @Override
    public void closeDecoder() {
        if (cdfFile != null) {
            try {
                cdfFile.close();
            } catch (IOException ioe) {
                log.error("Could not close NetCDF file", ioe);
            }
        }
    }

    /**
     * Get the 1 dimensional short array image data.
     * 
     * @param var
     *            NetCDF variable containing the image data.
     * @return The 1 dimensional short array image data.
     */
    private short[] getShortCMIData(Variable var) {
        short[] data = null;
        try {
            Array a = var.read();
            if (a != null) {
                data = (short[]) a.copyTo1DJavaArray();
            } else {
                log.error("No short [] data read from CMI variable for GOES-R data");
            }
        } catch (IOException ioe) {
            log.error("IO error reading short CMI data in GOES-R data", ioe);
        }
        return data;
    }

    /**
     * Get the 1 dimensional byte array image data.
     * 
     * @param var
     *            NetCDF variable containing the image data.
     * @return The 1 dimensional byte array image data.
     */
    private byte[] getByteCMIData(Variable var) {
        byte[] data = null;
        try {
            Array a = var.read();
            if (a != null) {
                data = (byte[]) a.copyTo1DJavaArray();
            } else {
                log.error("No byte [] data read from CMI variable for GOES-R data");
            }
        } catch (IOException ioe) {
            log.error("IO error reading byte CMI data in GOES-R data", ioe);
        }
        return data;
    }

    /**
     * Read the Cloud and Moisture Imagery (CMI) data.
     * 
     * @return A populated satellite record if successful.
     */
    private SatelliteRecord readCMIData() {

        SatelliteRecord rec = null;

        Variable griddedData = cdfFile
                .findVariable(GOESRConstants.VARIABLE_SECTORIZED_CMI_DATA_ID);
        if (griddedData == null) {
            griddedData = cdfFile
                    .findVariable(GOESRConstants.VARIABLE_CMI_DATA_ID);
        }

        if (griddedData != null) {
            Attribute attrProj = griddedData
                    .findAttribute(GOESRConstants.CMI_ATTR_GRID_MAPPING_ID);
            if (attrProj != null) {
                GOESRProjection projection = null;
                try {
                    projection = projFactory.createProjection(cdfFile,
                            attrProj.getStringValue(), attributes);

                    SatMapCoverage coverage = projFactory
                            .getCoverage(projection);
                    if (coverage != null) {
                        rec = new SatelliteRecord();

                        // Read out units
                        String units = Unit.ONE.toString();
                        Attribute attr = griddedData
                                .findAttribute(GOESRConstants.CMI_ATTR_UNITS_ID);
                        if (attr != null
                                && "1".equals(attr.getStringValue()) == false) {
                            units = attr.getStringValue();
                        }
                        rec.setUnits(units);
                        rec.setCoverage(coverage);

                        int nx = rec.getCoverage().getNx();
                        int ny = rec.getCoverage().getNy();

                        SatelliteMessageData messageData = null;
                        int bitDepth = attributes.getBit_depth();
                        if (bitDepth == GOESRConstants.BIT_DEPTH_08) {
                            messageData = new SatelliteMessageData(
                                    getByteCMIData(griddedData), nx, ny);
                        } else {
                            messageData = new SatelliteMessageData(
                                    getShortCMIData(griddedData), nx, ny);
                        }
                        attr = griddedData
                                .findAttribute(GOESRConstants.CMI_ATTR_ADD_OFFSET_ID);
                        messageData.setDataAttribute(
                                SatelliteRecord.SAT_ADD_OFFSET,
                                GOESRUtil.getAttributeFloat(attr, 0));

                        attr = griddedData
                                .findAttribute(GOESRConstants.CMI_ATTR_SCALE_FACTOR_ID);
                        messageData.setDataAttribute(
                                SatelliteRecord.SAT_SCALE_FACTOR,
                                GOESRUtil.getAttributeFloat(attr, 0));

                        attr = griddedData
                                .findAttribute(GOESRConstants.CMI_ATTR_FILL_VALUE_ID);
                        messageData.setDataAttribute(
                                SatelliteRecord.SAT_FILL_VALUE,
                                GOESRUtil.getAttributeFloat(attr, 0));

                        rec.setMessageData(messageData);

                    }
                } catch (GOESRProjectionException ipe) {
                    log.error(
                            "Could not create projection/coverage information for GOES-R data",
                            ipe);
                }
            }
        } else {
            log.error("No image grid data found in GOES-R data");
        }
        return rec;
    }

}

