package com.raytheon.uf.edex.plugin.goesr;

import java.io.File;
import java.io.IOException;

import com.raytheon.edex.esb.Headers;
import com.raytheon.uf.common.dataplugin.PluginDataObject;
import com.raytheon.uf.common.dataplugin.satellite.SatelliteRecord;
import com.raytheon.uf.common.status.IUFStatusHandler;
import com.raytheon.uf.common.status.UFStatus;
import com.raytheon.uf.common.util.FileUtil;
import com.raytheon.uf.edex.plugin.goesr.decoder.GOESRDataDecoder;
import com.raytheon.uf.edex.plugin.goesr.decoder.GOESRDecoderFactory;

/**
 * This is the EDEX level decoder instance that is the target for all GOES-R
 * data files to be decoded. On creation a temporary location is created if
 * required. During runtime, netCDF files are received, the type of decoder is
 * determined, the data is decoded, and then returned to EDEX for further
 * processing.
 * 
 * <pre>
 * 
 * SOFTWARE HISTORY
 * 
 * Date         Ticket#    Engineer    Description
 * ------------ ---------- ----------- --------------------------
 * May 29, 2012        796 jkorman     Initial creation
 * 
 * </pre>
 * 
 * @author
 * @version 1.0
 */

public class GOESRDecoder {

    private static final transient IUFStatusHandler log = UFStatus
            .getHandler(GOESRDecoder.class);

    private static final String name = "goesr";

    // An empty array.
    private PluginDataObject[] empty = new PluginDataObject[0];

    // The path where temporary netCDF data files will be processed from.
    private File tempFileDir;

    private String tempPathName;

    /**
     * Create the decoder instance.
     */
    public GOESRDecoder() {
    }

    /**
     * Decode the GOES-R data contained in the file.  
     * @param data A file that should contain GOES-R image data.
     * @return An array of PluginDataObject containing the decoded GOES-R satellite data.
     */
    public PluginDataObject[] decode(File file, Headers headers) {

        PluginDataObject[] recs = empty;
        if (tempFileDir == null) {
            try {
                tempFileDir = FileUtil.createTempDir(tempPathName, name);
            } catch (IOException ioe) {
                log.error("Could not create temporary file path");
            }
        }
        if (file != null) {
            if (tempFileDir != null) {

                log.info(String.format("Processing file [%s]", file.getName()));

                GOESRDataDecoder dec = null;
                SatelliteRecord rec = null;
                try {
                    dec = GOESRDecoderFactory.getDecoder(file, tempFileDir);
                    if (dec != null) {
                        rec = dec.decodeFile();
                    }
                } catch (Exception e) {
                    log.error("Uncaught error in decoder ", e);
                } finally {
                    // In an instance of the decoder was created, ensure that we
                    // close it!
                    if (dec != null) {
                        // Need to dispose of decoder resources!
                        dec.closeDecoder();
                    }
                }
                if (rec != null) {
                    recs = new PluginDataObject[] { rec, };
                }
            } else {
                String msg = String
                        .format("Temporary file path [%s] not created. File [%s] not processed",
                                tempPathName, file.getName());
                log.error(msg);
            }
        } else {
            log.error("Input data file path is null. Nothing to process");
        }
        return recs;
    }

    /**
     * Set the temporary path where processing files will take place.
     * 
     * @param tempPath
     *            The temporary path name.
     */
    public void setTempPathName(String tempPath) {
        tempPathName = tempPath;
        try {
            tempFileDir = FileUtil.createTempDir(tempPathName, name);
        } catch (IOException ioe) {
            log.error(String.format("Could not create temporary file path [%s]", tempPathName));
        }
    }
}
