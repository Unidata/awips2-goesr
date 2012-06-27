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

import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.ProjectedCRS;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import ucar.nc2.Variable;

import com.raytheon.uf.common.dataplugin.satellite.SatMapCoverage;
import com.raytheon.uf.common.geospatial.MapUtil;
import com.raytheon.uf.edex.plugin.goesr.decoder.GOESRAttributes;
import com.raytheon.uf.edex.plugin.goesr.decoder.GOESRConstants;
import com.raytheon.uf.edex.plugin.goesr.decoder.GOESRUtil;

/**
 * Base class for GOES-R map projections.
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

public abstract class GOESRProjection {

    // Name of the projection.
    private String name;

    private Double standardLat1;

    private Double standardLat2;

    private Double centralMeridianLongitude;

    private Double projOriginLatitude;

    private Double semiMajor;

    private Double semiMinor;

    private Double falseEasting;

    private Double falseNorthing;

    // Global attributes from the GOES-R data.
    private GOESRAttributes attributes;

    // Calculated Coordinate Reference System
    private ProjectedCRS crs;

    // Hold on to the transform for this projection.
    private MathTransform toLatLon = null;

    public GOESRProjection(Variable projData, GOESRAttributes attributes) {
        this.name = GOESRUtil.getAttributeString(
                projData.findAttribute(GOESRConstants.PROJ_GRID_MAPPING_NAME),
                "ERROR");

        falseEasting = GOESRUtil.getAttributeDouble(
                projData.findAttribute(GOESRConstants.PROJ_FALSE_EASTING), 0d);
        falseNorthing = GOESRUtil.getAttributeDouble(
                projData.findAttribute(GOESRConstants.PROJ_FALSE_NORTHING), 0d);
        this.attributes = attributes;
    }

    /**
     * Get the projection name.
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the projection name.
     * 
     * @param name
     *            The projection name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the first standard latitude.
     * 
     * @return The first standard latitude.
     */
    public Double getStandardLat1() {
        return standardLat1;
    }

    /**
     * Set the first standard latitude.
     * 
     * @param standardLat
     *            The first standard latitude.
     */
    public void setStandardLat1(Double standardLat) {
        standardLat1 = standardLat;
    }

    /**
     * Get the second standard latitude.
     * 
     * @return The second standard latitude.
     */
    public Double getStandardLat2() {
        return standardLat2;
    }

    /**
     * Set the second standard latitude.
     * 
     * @param standardLat
     *            The second standard latitude.
     */
    public void setStandardLat2(Double standardLat) {
        this.standardLat2 = standardLat;
    }

    /**
     * Get the longitude of the central meridian.
     * 
     * @return The longitude of the central meridian.
     */
    public Double getCentralMeridianLongitude() {
        return centralMeridianLongitude;
    }

    /**
     * Get the longitude of the central meridian.
     * 
     * @param centralMeridianLongitude
     *            The longitude of the central meridian.
     */
    public void setCentralMeridianLongitude(Double centralMeridianLongitude) {
        this.centralMeridianLongitude = centralMeridianLongitude;
    }

    /**
     * Set the latitude of the projection origin.
     * 
     * @return The latitude of the projection origin.
     */
    public Double getProjOriginLatitude() {
        return projOriginLatitude;
    }

    /**
     * Get the latitude of the projection origin.
     * 
     * @param The
     *            latitude of the projection origin.
     */
    public void setProjOriginLatitude(Double projOriginLatitude) {
        this.projOriginLatitude = projOriginLatitude;
    }

    /**
     * Get the semimajor axis in meters.
     * 
     * @return The semimajor axis in meters.
     */
    public Double getSemiMajor() {
        return semiMajor;
    }

    /**
     * Set the semimajor axis in meters.
     * 
     * @param semiMajor
     *            The semimajor axis in meters.
     */
    public void setSemiMajor(Double semiMajor) {
        this.semiMajor = semiMajor;
    }

    /**
     * Get the semiminor axis in meters.
     * 
     * @return The semiminor axis in meters.
     */
    public Double getSemiMinor() {
        return semiMinor;
    }

    /**
     * Set the semiminor axis in meters.
     * 
     * @param semiMajor
     *            The semiminor axis in meters.
     */
    public void setSemiMinor(Double semiMinor) {
        this.semiMinor = semiMinor;
    }

    /**
     * Get the false Easting in meters.
     * 
     * @return The false Easting in meters.
     */
    public Double getFalseEasting() {
        return falseEasting;
    }

    /**
     * Set the false Easting in meters.
     * 
     * @param falseEasting
     *            The false Easting in meters.
     */
    public void setFalseEasting(Double falseEasting) {
        this.falseEasting = falseEasting;
    }

    /**
     * Get the false Northing in meters.
     * 
     * @return The false Northing in meters.
     */
    public Double getFalseNorthing() {
        return falseNorthing;
    }

    /**
     * Set the false Northing in meters.
     * 
     * @param falseNorthing
     *            The false Northing in meters.
     */
    public void setFalseNorthing(Double falseNorthing) {
        this.falseNorthing = falseNorthing;
    }

    /**
     * Get the netCDF global attributes associated with this projection.
     * 
     * @return The netCDF global attributes.
     */
    public GOESRAttributes getAttributes() {
        return attributes;
    }

    /**
     * Get the Coordinate Reference System for this projection.
     * 
     * @return The Coordinate Reference System for this projection.
     */
    public ProjectedCRS getCrs() {
        return crs;
    }

    /**
     * Set the Coordinate Reference System (CRS) for this projection.
     * 
     * @param crs
     *            The crs to set
     */
    protected void setCrs(ProjectedCRS crs) {
        this.crs = crs;
    }

    /**
     * Get the coverage object for the projection information.
     * 
     * @return The coverage object for the projection information.
     * @throws GOESRProjectionException
     *             An error occurred attempting to create the coverage.
     */
    protected abstract SatMapCoverage getCoverage() throws GOESRProjectionException;

    /**
     * Using projection information, calculate the corner points of the data in
     * latitude/longitude space.
     * 
     * @return The calculated corner points.
     */
    public GeoRectangle calcCornerPoints() throws GOESRProjectionException {
        GeoRectangle corners = null;

        double[] dIn = new double[4 * 2];
        double[] dOut = new double[4 * 2];

        double dx = getAttributes().getPixel_x_size() * 1000;
        double dy = getAttributes().getPixel_y_size() * 1000;

        int nx = getAttributes().getProduct_tile_width();
        int ny = getAttributes().getProduct_tile_height();

        int colOffset = getAttributes().getTile_column_offset();
        int rowOffset = getAttributes().getTile_row_offset();

        double leftSide = -(getAttributes().getProduct_columns() / 2d)
                + colOffset;
        double rightSide = leftSide + nx;

        int rows = getAttributes().getProduct_rows();
        double topSide = (rows / 2d) - rowOffset;
        double bottomSide = topSide - ny;
        // UpperLeft
        dIn[0] = leftSide * dx;
        dIn[1] = topSide * dy;
        // UpperRight
        dIn[2] = rightSide * dx;
        dIn[3] = dIn[1];
        // LowerRight
        dIn[4] = dIn[2];
        dIn[5] = bottomSide * dy;
        // LowerLeft
        dIn[6] = dIn[0];
        dIn[7] = dIn[5];

        try {
            if (toLatLon == null) {
                toLatLon = MapUtil.getTransformToLatLon(getCrs());
            }
            toLatLon.transform(dIn, 0, dOut, 0, 4);
        } catch (TransformException te) {
            StringBuilder message = new StringBuilder(
                    "Could not transform the corner point data ");
            message = GOESRUtil.formatCornerPoints(dIn, message);
            throw new GOESRProjectionException(message.toString(), te);
        } catch (FactoryException fe) {
            String msg = String.format("Could not create LatLon transform from " + getCrs());
            throw new GOESRProjectionException(msg, fe);
        }
        if (dOut != null) {
            corners = new GeoRectangle(dOut);
        }

        return corners;
    }
}
