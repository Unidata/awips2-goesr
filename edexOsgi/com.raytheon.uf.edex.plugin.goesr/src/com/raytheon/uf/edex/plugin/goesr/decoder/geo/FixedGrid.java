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

import com.raytheon.uf.common.dataplugin.satellite.SatMapCoverage;
import com.raytheon.uf.edex.plugin.goesr.decoder.GOESRAttributes;

import ucar.nc2.Variable;

/**
 * A class representation of the GOES-R FixedGrid projection information
 * contained in the GOES-R netCDF file. The ICD says that this is "unprojected"
 * data, that is, the data is as it is seen from the satellite. This would be a
 * orthographic type projection.
 * 
 * <pre>
 * 
 * SOFTWARE HISTORY
 * 
 * Date         Ticket#    Engineer    Description
 * ------------ ---------- ----------- --------------------------
 * Jun 1, 2012        796 jkorman     Initial creation
 * 
 * </pre>
 * 
 * @author jkorman
 * @version 1.0
 */

public class FixedGrid extends GOESRProjection {

    // Default from the ICD.
    private static final double DEF_SEMI_MAJOR = 6378137d;

    // Default from the ICD.
    private static final double DEF_SEMI_MINOR = 6356732.31414d;

    // Default satellite height in meters, from the ICD.
    private static final double DEF_SAT_HEIGHT = 35786023d;

    /**
     * Height in meters above the surface
     */
    private Double perspectiveHeight;

    /**
     * Create an instance of a GOES-R FixedGrid projection using the supplied
     * projection and attributes data.
     * 
     * @param projData
     *            The netCDF variable for this projection.
     * @param attributes
     *            netCDF global attributes for the GOES-R data.
     */
    public FixedGrid(Variable projData, GOESRAttributes attributes) {
        super(projData, attributes);

        // Defaults from ICD until we have real data to look at!
        setPerspectiveHeight(DEF_SAT_HEIGHT);
        setSemiMajor(DEF_SEMI_MAJOR);
        setSemiMinor(DEF_SEMI_MINOR);
    }

    /**
     * Get the height of the perspective in meters. This is the satellite
     * altitude.
     * 
     * @return the perspectiveHeight The height of the perspective in meters
     */
    public Double getPerspectiveHeight() {
        return perspectiveHeight;
    }

    /**
     * Set the height of the perspective in meters. This is the satellite
     * altitude.
     * 
     * @param perspectiveHeight
     *            The height of the perspective in meters
     */
    public void setPerspectiveHeight(Double perspectiveHeight) {
        this.perspectiveHeight = perspectiveHeight;
    }

    /**
     * Get the coverage object for the projection information.
     * 
     * @return The coverage object for the projection information.
     * @throws GOESRProjectionException
     *             An error occurred attempting to create the coverage.
     */
    protected SatMapCoverage getCoverage() throws GOESRProjectionException {
        throw new GOESRProjectionException("FixedGrid not yet implemted");
    }

}
