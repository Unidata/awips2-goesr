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
import java.io.FileInputStream;
import java.io.IOException;

import com.raytheon.uf.common.status.IUFStatusHandler;
import com.raytheon.uf.common.status.UFStatus;
import com.raytheon.uf.common.util.FileUtil;
import com.raytheon.uf.edex.plugin.goesr.decoder.geo.GOESRProjectionFactory;
import com.raytheon.uf.edex.wmo.message.WMOHeader;

/**
 * Create an instance of a GOESDataDecoder using given input data. The
 * getDecoder method takes the original data path and a temporary file path. If
 * the original path references a valid (HDF) netCDF file, the contents of the
 * netCDF portion of the file are copied to a temporary file prior to creating
 * the decoder instance.
 * 
 * <pre>
 * 
 * SOFTWARE HISTORY
 * 
 * Date         Ticket#    Engineer    Description
 * ------------ ---------- ----------- --------------------------
 * Jun 6, 2012        796 jkorman     Initial creation
 * 
 * </pre>
 * 
 * @author jkorman
 * @version 1.0
 */

public abstract class GOESRDecoderFactory {

    private static final IUFStatusHandler log = UFStatus
            .getHandler(GOESRDecoderFactory.class);

    /*
     * Create the projection factory for the decoder.
     */
    private static GOESRProjectionFactory projFactory = new GOESRProjectionFactory();

    /**
     * Create a decoder instance from the given input data.
     * 
     * @param path
     *            Path to the raw WMO formatted GOES-R data.
     * @param tmpPath
     *            Path to the location to receive the extracted GOES-R netCDF
     *            data.
     * @return The decoder that was created.
     */
    public static GOESRDataDecoder getDecoder(File path, File tmpPath) {
        GOESRDataDecoder decoder = null;
        File temp = null;
        try {
            String fName = path.getName();
            // If the temporary file prefix is too short, pad it out so that
            // it is at least three characters.
            if (fName.length() < 3) {
                fName += "XXX";
            }
            // use the default suffix.
            temp = FileUtil.createTempFile(tmpPath, fName, null);
            if (temp != null) {
                if (copyToTemp(path, temp)) {
                    decoder = new GOESRNetCDFDecoder(temp, projFactory);
                }
            } else {
                log.error("Could not create temporary file while processing file "
                        + path);
            }
        } catch (Exception e) {
            log.error("Error attempting to create decoder", e);
        }
        return decoder;
    }

    /**
     * Copy a WMO formatted netCDF file into a temporary location. The WMO
     * header will be stripped from the file and the data will start with the
     * netCDF (HDF) signature.
     * 
     * @param filePath
     *            The path to the input netCDF file to be copied.
     * @param tempPath
     *            The path to the output netCDF file.
     * @return The WMO header of the data.
     */
    private static boolean copyToTemp(File filePath, File tempPath) {
        boolean success = false;

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePath);
        } catch (IOException ioe) {
            log.error("Unable to create GOES-R netCDF input stream from file "
                    + filePath);
        }

        if (fis != null) {
            int size = (int) filePath.length();
            if (size > GOESRConstants.PRE_READ_SIZE) {
                size = GOESRConstants.PRE_READ_SIZE;
            }
            byte[] hData = new byte[size];
            try {
                fis.read(hData);
                WMOHeader wmo = new WMOHeader(hData);
                int pos = 0;
                if (wmo.isValid()) {
                    // Start looking for the beginning of the data
                    pos = wmo.getMessageDataStart();
                }

                String fData = GOESRUtil.toString(hData);
                // Position of the HDF signature.
                int hdfPos = fData.indexOf(GOESRConstants.HDF_SIG, pos);
                if (hdfPos >= pos) {
                    // We've got a valid signature. Copy the data from this
                    // point.
                    success = FileUtil.copyFile(filePath, tempPath, hdfPos);
                } else {
                    log.error(String
                            .format("No valid GOES-R signature in first %d bytes of file [%s]",
                                    size, filePath.getName()));
                }
            } catch (IOException ioe) {
                log.error("Unable to read GOES-R netCDF input file " + filePath, ioe);
            } finally {
                try {
                    fis.close();
                } catch (IOException ioe) {
                    log.error("Unable to close GOES-R netCDF input file "
                            + filePath);
                }
            }
        }
        return success;
    }

}
