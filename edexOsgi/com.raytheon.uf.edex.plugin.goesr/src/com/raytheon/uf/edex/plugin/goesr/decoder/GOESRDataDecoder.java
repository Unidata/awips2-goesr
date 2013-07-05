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

import com.raytheon.uf.common.dataplugin.satellite.SatelliteRecord;

/**
 * Expose decoder behavior for GOES-R data. The interface exposes a decode file.
 * The interface does not suppose the format of the data other than it may be
 * presented in a temporary file.
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
public interface GOESRDataDecoder {

    /**
     * Decode the GOES-R data and return the data in a SatelliteRecord. If an
     * error occurs or the decoder is unable to decode the data, a null
     * reference may be returned.
     * 
     * @return The decoded GOES-R satellite data.
     */
    public SatelliteRecord decodeFile();

    /**
     * Ensure that any decoder resources are cleaned up.
     */
    public void closeDecoder();

}
