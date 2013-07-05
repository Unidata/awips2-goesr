package com.raytheon.uf.edex.plugin.goesr;

import java.util.UUID;

import ucar.nc2.NetcdfFile;

import com.raytheon.edex.esb.Headers;
import com.raytheon.uf.common.dataplugin.PluginDataObject;
import com.raytheon.uf.common.dataplugin.satellite.SatelliteRecord;
import com.raytheon.uf.common.status.IUFStatusHandler;
import com.raytheon.uf.common.status.UFStatus;
import com.raytheon.uf.edex.plugin.goesr.decoder.GOESRDataDecoder;
import com.raytheon.uf.edex.plugin.goesr.decoder.GOESRNetCDFDecoder;
import com.raytheon.uf.edex.plugin.goesr.decoder.geo.GOESRProjectionFactory;

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
 * Jul  5, 2013       2123 mschenke    Removed temporary file creation
 * 
 * </pre>
 * 
 * @author
 * @version 1.0
 */

public class GOESRDecoder {

    private static final transient IUFStatusHandler log = UFStatus
            .getHandler(GOESRDecoder.class);

    /** The projection factory for the decoder. */
    private static GOESRProjectionFactory projFactory = new GOESRProjectionFactory();

    private PluginDataObject[] empty = new PluginDataObject[0];

    /**
     * Decode the GOES-R data contained in the file.
     * 
     * @param data
     *            A file that should contain GOES-R image data.
     * @return An array of PluginDataObject containing the decoded GOES-R
     *         satellite data.
     */
    public PluginDataObject[] decode(byte[] goesrData, Headers headers) {
        GOESRDataDecoder decoder = null;
        try {
            decoder = new GOESRNetCDFDecoder(NetcdfFile.openInMemory(UUID
                    .randomUUID().toString(), goesrData), projFactory);
        } catch (Exception e) {
            log.error("Could create GOES-R decoder from data", e);
            return empty;
        }

        SatelliteRecord record = null;
        try {
            record = decoder.decodeFile();
        } catch (Throwable t) {
            log.error("Uncaught error in decoder ", t);
        } finally {
            decoder.closeDecoder();
        }

        if (record != null) {
            return new PluginDataObject[] { record };
        }
        return empty;
    }

}
