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
package com.raytheon.uf.edex.plugin.goesr.decoder.geo;

/**
 * Thrown when the given projection could not be created.
 * 
 * <pre>
 * 
 * SOFTWARE HISTORY
 * 
 * Date         Ticket#    Engineer    Description
 * ------------ ---------- ----------- --------------------------
 * Jul 3, 2012            jkorman     Initial creation
 * 
 * </pre>
 * 
 * @author jkorman
 * @version 1.0
 */

public class GOESRProjectionException extends Exception {

    private static final long serialVersionUID = 9077163987824558053L;

    /**
     * Constructs a InvalidProjectionException with no error message.
     */
    public GOESRProjectionException() {
        super();
    }

    /**
     * Constructs a InvalidProjectionException with an error message.
     * 
     * @param message
     *            The exception detail message.
     */
    public GOESRProjectionException(String message) {
        super(message);
    }

    /**
     * Constructs a InvalidProjectionException with an error message and a
     * chained cause.
     * 
     * @param message
     *            The exception detail message.
     * @param cause
     *            The exception that caused this exception to be thrown.
     */
    public GOESRProjectionException(String message, Throwable cause) {
        super(message, cause);
    }

}
