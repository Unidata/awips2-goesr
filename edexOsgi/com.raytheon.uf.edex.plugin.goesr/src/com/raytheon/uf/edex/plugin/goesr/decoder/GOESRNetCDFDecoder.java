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

import java.io.File;
import java.io.IOException;

import ucar.ma2.Array;
import ucar.nc2.Attribute;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

import com.raytheon.edex.exception.DecoderException;
import com.raytheon.uf.common.dataplugin.satellite.SatMapCoverage;
import com.raytheon.uf.common.dataplugin.satellite.SatelliteMessageData;
import com.raytheon.uf.common.dataplugin.satellite.SatelliteRecord;
import com.raytheon.uf.common.datastorage.records.IDataRecord;
import com.raytheon.uf.common.status.IUFStatusHandler;
import com.raytheon.uf.common.status.UFStatus;
import com.raytheon.uf.common.status.UFStatus.Priority;
import com.raytheon.uf.common.time.DataTime;
import com.raytheon.uf.edex.plugin.goesr.decoder.geo.GOESRProjection;
import com.raytheon.uf.edex.plugin.goesr.decoder.geo.GOESRProjectionException;
import com.raytheon.uf.edex.plugin.goesr.decoder.geo.GOESRProjectionFactory;
import com.raytheon.uf.edex.plugin.goesr.decoder.geo.GeoRectangle;

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
 * 
 * </pre>
 * 
 * @author jkorman
 * @version 1.0
 */

public class GOESRNetCDFDecoder implements GOESRDataDecoder {

    private static final transient IUFStatusHandler log = UFStatus
            .getHandler(GOESRNetCDFDecoder.class);

    private GOESRProjectionFactory projFactory;

    private File filePath;

    private NetcdfFile cdfFile;

    private GOESRAttributes attributes;

    // Bounding lat/lon of the data.
    private GeoRectangle bounds;

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
    public GOESRNetCDFDecoder(File path, GOESRProjectionFactory projFactory)
            throws DecoderException {

        if (path == null) {
            throw new DecoderException("Path is null");
        }
        if (!path.exists()) {
            throw new DecoderException(String.format(
                    "Path [%s] does not exist", path.getAbsolutePath()));
        }
        this.projFactory = projFactory;
        filePath = path;
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
            openCdfFile();

            try {
                rec = readCMIData();
            } catch (Exception e) {
                log.error("Could not read the netCDF data file", e);
                rec = null;
            }
            if (rec != null) {
                rec.setPluginName(GOESRConstants.SAT_PLUGIN_NAME);
                // Set the satellite altitude in kilometers
                rec.setSatHeight(attributes.getSatellite_altitude().intValue() / 1000);
                rec.setSatSubPointLat(attributes.getTile_center_latitude());
                rec.setSatSubPointLon(attributes.getTile_center_longitude());

                rec.setDataTime(new DataTime(attributes.getStart_date_time()));

                // datauri(1)
                rec.setSource(GOESRConstants.SOURCE_GOESR);

                // datauri(2)
                rec.setCreatingEntity(attributes.getSatellite_id());

                String[] parts = attributes.getProduct_name().split("-");
                // datauri(3)
                rec.setSectorID(parts[0]);
                String physicalElement = String.format("CH-%02d-%4.2fum",
                        attributes.getChannel_id(),
                        attributes.getCentral_wavelength());
                // datauri(4)
                rec.setPhysicalElement(physicalElement);

                // datauri(5) is the coverage object built in readCMIData
                try {
                    rec.constructDataURI();
                } catch (Exception e) {
                    log.error("Could not create datauri", e);
                    rec = null;
                }
                SatelliteMessageData messageData = (SatelliteMessageData) rec.getMessageData();
                IDataRecord dataRec = messageData.getStorageRecord(rec, SatelliteRecord.SAT_DATASET_NAME);
                rec.setMessageData(dataRec);
                
                StringBuilder message = new StringBuilder("GOES-R");
                message.append("|");
                synchronized (GOESRConstants.GOESR_DATETIME) {
                    message.append(GOESRConstants.GOESR_DATETIME
                            .format(attributes.getStart_date_time()));
                }
                message.append("|");
                message.append(attributes.getProduct_name());
                message.append("|");

                if (log.isPriorityEnabled(Priority.DEBUG)) {
                    GOESRUtil.formatCornerPoints(bounds.getPoints(), message);
                }
                log.info(message.toString());
            }
        } catch (DecoderException de) {
            log.error("Could not open the netCDF data file", de);
        }
        return rec;
    }

    /**
     * Open the netCDF data file and read out the global attributes.
     * 
     * @throws DecoderException
     *             An error occurred while reading the netCDF file.
     */
    private void openCdfFile() throws DecoderException {
        try {
            cdfFile = NetcdfFile.open(filePath.getAbsolutePath());

            attributes = new GOESRAttributes(cdfFile);
            if (log.isPriorityEnabled(Priority.DEBUG)) {
                log.debug(attributes.toString());
            }
        } catch (IOException ioe) {
            throw new DecoderException(String.format(
                    "Could not open NetCDF file [%s]",
                    filePath.getAbsolutePath()), ioe);
        }
    }

    /**
     * Close both the netCDF instance and also delete the temporary data file. Note
     * that this method must be called before disposing of the decoder instance to
     * release decoder resources.
     */
    @Override
    public void closeDecoder() {
        if (cdfFile != null) {
            try {
                cdfFile.close();
            } catch (IOException ioe) {
                log.error(String.format("Could not close NetCDF file %s",
                        filePath.getName()), ioe);
            }
        }
        if (!filePath.delete()) {
            log.error(String.format(
                    "Could not delete GOES-R temporary file %s",
                    filePath.getName()));
        }
    }

    /**
     * Get the file reference of the netCDF file being decoded.
     * 
     * @return The file reference of the netCDF file being decoded.
     */
    @Override
    public File getGOESRFile() {
        return filePath;
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
                log.error("No short [] data read from CMI variable for file "
                        + filePath.getName());
            }
        } catch (IOException ioe) {
            log.error(
                    "IO error reading short CMI data in file "
                            + filePath.getName(), ioe);
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
                log.error("No byte [] data read from CMI variable for file "
                        + filePath.getName());
            }
        } catch (IOException ioe) {
            log.error(
                    "IO error reading byte CMI data in file "
                            + filePath.getName(), ioe);
        }
        return data;
    }

    /**
     * Read the Cloud and Moisure Imagery (CMI) data.
     * 
     * @return A populated satellite record if successful.
     */
    private SatelliteRecord readCMIData() {

        SatelliteRecord rec = null;

        Variable griddedData = cdfFile
                .findVariable(GOESRConstants.GRIDDED_DATA);
        if (griddedData != null) {

            Attribute attrProj = griddedData
                    .findAttribute(GOESRConstants.GD_GRID_MAPPING);
            if (attrProj != null) {
                GOESRProjection projection = null;
                try {
                    projection = projFactory.createProjection(cdfFile,
                            attrProj.getStringValue(), attributes);

                    bounds = projection.calcCornerPoints();

                    SatMapCoverage coverage = projFactory
                            .getCoverage(projection);
                    if ((bounds != null) && (coverage != null)) {
                        rec = new SatelliteRecord();
                        Attribute attr = griddedData
                                .findAttribute(GOESRConstants.GD_UNITS);
                        // String v = attr.getStringValue();
                        // For now - set units to Generic
                        rec.setUnits("GenericPixel");
                        rec.setCoverage(coverage);

                        rec.setUpperRightLat((float) bounds.getUR_Lat());
                        rec.setUpperRightLon((float) bounds.getUR_Lon());

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
                                .findAttribute(GOESRConstants.GD_ADD_OFFSET);
                        messageData.setDataAttribute(
                                SatelliteRecord.SAT_ADD_OFFSET,
                                GOESRUtil.getAttributeFloat(attr, 0));

                        attr = griddedData
                                .findAttribute(GOESRConstants.GD_SCALE_FACTOR);
                        messageData.setDataAttribute(
                                SatelliteRecord.SAT_SCALE_FACTOR,
                                GOESRUtil.getAttributeFloat(attr, 0));

                        attr = griddedData
                                .findAttribute(GOESRConstants.GD_FILL_VALUE);
                        messageData.setDataAttribute(
                                SatelliteRecord.SAT_FILL_VALUE,
                                GOESRUtil.getAttributeFloat(attr, 0));

                        rec.setMessageData(messageData);

                    }
                } catch (GOESRProjectionException ipe) {
                    log.error(
                            "Could not create projection/coverage information for file "
                                    + filePath.getName(), ipe);
                }
            } else {
                log.error("Cannot find image grid data, grid_mapping attribute in file "
                        + filePath.getName());
            }
        } else {
            log.error("No image grid data found in file " + filePath.getName());
        }
        return rec;
    }
}