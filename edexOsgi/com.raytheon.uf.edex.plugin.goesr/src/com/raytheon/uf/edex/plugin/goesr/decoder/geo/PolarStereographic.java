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

import ucar.nc2.Variable;

import com.raytheon.uf.common.dataplugin.satellite.SatMapCoverage;
import com.raytheon.uf.edex.plugin.goesr.decoder.GOESRAttributes;
import com.raytheon.uf.edex.plugin.goesr.decoder.GOESRConstants;
import com.raytheon.uf.edex.plugin.goesr.decoder.GOESRUtil;
import com.vividsolutions.jts.geom.Polygon;

/**
 * A class representation of the GOES-R PolarStereographic projection
 * information contained in the GOES-R netCDF file.
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

public class PolarStereographic extends GOESRProjection {

    // Default from the ICD.
    private static final double DEF_SEMI_MAJOR = 6371200;

    // Default from the ICD.
    private static final double DEF_SEMI_MINOR = 6371200;

    private Double refLongitude;

    /**
     * Create an instance of a GOES-R PolarStereographic projection using the
     * supplied projection and attributes data.
     * 
     * @param projData
     *            The netCDF variable for this projection.
     * @param attributes
     *            netCDF global attributes for the GOES-R data.
     */
    public PolarStereographic(Variable projData, GOESRAttributes attributes) {
        super(projData, attributes);

        // Set defaults from ICD until we have data.
        refLongitude = 90d;

        setSemiMajor(GOESRUtil.getAttributeDouble(
                projData.findAttribute(GOESRConstants.PROJ_SEMI_MINOR),
                DEF_SEMI_MAJOR));
        setSemiMinor(GOESRUtil.getAttributeDouble(
                projData.findAttribute(GOESRConstants.PROJ_SEMI_MAJOR),
                DEF_SEMI_MINOR));
    }

    /**
     * @return the refLongitude
     */
    public Double getRefLongitude() {
        return refLongitude;
    }

    /**
     * @param refLongitude
     *            the refLongitude to set
     */
    public void setRefLongitude(Double refLongitude) {
        this.refLongitude = refLongitude;
    }

    /**
     * Get the coverage object for the projection information.
     * 
     * @return The coverage object for the projection information.
     * @throws GOESRProjectionException
     *             An error occurred attempting to create the coverage.
     */
    protected SatMapCoverage getCoverage() throws GOESRProjectionException {
        SatMapCoverage coverage = new SatMapCoverage();

        Float v = getAttributes().getPixel_x_size();
        coverage.setDx(v);
        v = getAttributes().getPixel_y_size();
        coverage.setDy(v);

        coverage.setNx(getAttributes().getProduct_tile_width());
        coverage.setNy(getAttributes().getProduct_tile_height());
        coverage.setProjection(SatMapCoverage.PROJ_POLAR_STEREO);
        coverage.setLov(getCentralMeridianLongitude().floatValue());
        coverage.setLatin(getStandardLat1().floatValue());
        coverage.setCrsWKT(getCrs().toWKT());

        GeoRectangle b = calcCornerPoints();
        if (b != null) {
            Polygon geom = b.getGeometry();
            coverage.setLocation(geom);
            coverage.setLa1((float) b.getUL_Lat());
            coverage.setLo1((float) b.getUL_Lon());
            // lower right
            coverage.setLa2((float) b.getLR_Lat());
            coverage.setLo2((float) b.getLR_Lon());

            coverage.setGid(coverage.hashCode());
        }
        return coverage;
    }

}
