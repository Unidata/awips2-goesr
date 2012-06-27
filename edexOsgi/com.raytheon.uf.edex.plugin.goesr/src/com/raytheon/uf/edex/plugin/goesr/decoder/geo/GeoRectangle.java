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

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;

/**
 * GeoRectange wraps an array of double values representing the corner points of
 * a rectangle. The corner point values represent latitude, longitude pairs
 * where the even value of the array is the longitude and the odd value is the
 * latitude. Note that the array only uses the four corner points and does not
 * close on itself.
 * <table>
 * <tr>
 * <td>UL.Lon:p[0],UL.Lat:p[1]</td>
 * <td>---------</td>
 * <td>UR.Lon:p[2],UR.Lat:p[3]</td>
 * </tr>
 * <tr>
 * <td></td>
 * <td></td>
 * <td></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td></td>
 * <td></td>
 * </tr>
 * <tr>
 * <td></td>
 * <td></td>
 * <td></td>
 * </tr>
 * <tr>
 * <td>LL.Lon:p[6],LL.Lat:p[7]</td>
 * <td>---------</td>
 * <td>LR.Lon:p[4],LR.Lat:p[5]</td>
 * </tr>
 * </table>
 * 
 * 
 * <pre>
 * 
 * SOFTWARE HISTORY
 * 
 * Date         Ticket#    Engineer    Description
 * ------------ ---------- ----------- --------------------------
 * Jun 20, 2012        796 jkorman     Initial creation
 * 
 * </pre>
 * 
 * @author jkorman
 * @version 1.0
 */

public class GeoRectangle {

    private double[] points = new double[8];

    /**
     * Create an instance from a set of given corner points in lat/lon space.
     * 
     * @param points
     *            A not null array of corner points to set.
     */
    public GeoRectangle(double[] points) {
        copyIntoInternal(points);
    }

    /**
     * Populate the internal corner points data. See class description of points
     * format.
     * 
     * @param points
     *            A not null array of corner points to set.
     */
    public void setPoints(double[] points) {
        copyIntoInternal(points);
    }

    /**
     * Get a copy of the internal corner points data.
     * 
     * @param points
     *            A copy of the internal corner points data.
     */
    public double[] getPoints() {
        double[] p = new double[8];
        if (points != null) {
            for (int i = 0; i < p.length; i++) {
                p[i] = points[i];
            }
        }
        return p;
    }

    /*
     * Copy an array of corner points data to the internal points array.
     * 
     * @param points A not null array of corner points to set.
     */
    private void copyIntoInternal(double[] p) {
        if ((points != null) && (points.length == 8)) {
            for (int i = 0; i < p.length; i++) {
                points[i] = p[i];
            }
        }
    }

    /**
     * Set the upper left x axis pixel position relative to the origin of the
     * grid space.
     * 
     * @param value
     *            The axis x pixel position.
     */
    public void setUL_X(double value) {
        points[0] = value;
    }

    /**
     * Set the upper left y axis pixel position relative to the origin of the
     * grid space.
     * 
     * @param value
     *            The axis y pixel position.
     */
    public void setUL_Y(double value) {
        points[1] = value;
    }

    /**
     * Set the upper right x axis pixel position relative to the origin of the
     * grid space.
     * 
     * @param value
     *            The axis x pixel position.
     */
    public void setUR_X(double value) {
        points[2] = value;
    }

    /**
     * Set the upper right y axis pixel position relative to the origin of the
     * grid space.
     * 
     * @param value
     *            The axis y pixel position.
     */
    public void setUR_Y(double value) {
        points[3] = value;
    }

    /**
     * Set the lower right x axis pixel position relative to the origin of the
     * grid space.
     * 
     * @param value
     *            The axis x pixel position.
     */
    public void setLR_X(double value) {
        points[4] = value;
    }

    /**
     * Set the lower right y axis pixel position relative to the origin of the
     * grid space.
     * 
     * @param value
     *            The axis y pixel position.
     */
    public void setLR_Y(double value) {
        points[5] = value;
    }

    /**
     * Set the lower left x axis pixel position relative to the origin of the
     * grid space.
     * 
     * @param value
     *            The axis x pixel position.
     */
    public void setLL_X(double value) {
        points[6] = value;
    }

    /**
     * Set the lower left y axis pixel position relative to the origin of the
     * grid space.
     * 
     * @param value
     *            The axis y pixel position.
     */
    public void setLL_Y(double value) {
        points[7] = value;
    }

    /**
     * Get the Upper Left x axis pixel position relative to the origin of the
     * grid space.
     * 
     * @return The x axis pixel position.
     */
    public double getUL_X() {
        return points[0];
    }

    /**
     * Get the Upper Left y axis pixel position relative to the origin of the
     * grid space.
     * 
     * @return The y axis pixel position.
     */
    public double getUL_Y() {
        return points[1];
    }

    /**
     * Get the Upper Right x axis pixel position relative to the origin of the
     * grid space.
     * 
     * @return The x axis pixel position.
     */
    public double getUR_X() {
        return points[2];
    }

    /**
     * Get the Upper Right y axis pixel position relative to the origin of the
     * grid space.
     * 
     * @return The y axis pixel position.
     */
    public double getUR_Y() {
        return points[3];
    }

    /**
     * Get the Lower Right x axis pixel position relative to the origin of the
     * grid space.
     * 
     * @return The x axis pixel position.
     */
    public double getLR_X() {
        return points[4];
    }

    /**
     * Get the Lower Right y axis pixel position relative to the origin of the
     * grid space.
     * 
     * @return The y axis pixel position.
     */
    public double getLR_Y() {
        return points[5];
    }

    /**
     * Get the Lower Left x axis pixel position relative to the origin of the
     * grid space.
     * 
     * @return The x axis pixel position.
     */
    public double getLL_X() {
        return points[6];
    }

    /**
     * Get the Lower Left y axis pixel position relative to the origin of the
     * grid space.
     * 
     * @return The y axis pixel position.
     */
    public double getLL_Y() {
        return points[7];
    }

    /**
     * Get the longitude of the Upper Left pixel position.
     * 
     * @return The Upper Left longitude.
     */
    public double getUL_Lon() {
        return points[0];
    }

    /**
     * Get the latitude of the Upper Left pixel position.
     * 
     * @return The Upper Left latitude.
     */
    public double getUL_Lat() {
        return points[1];
    }

    /**
     * Get the longitude of the Upper Right pixel position.
     * 
     * @return The Upper Right longitude.
     */
    public double getUR_Lon() {
        return points[2];
    }

    /**
     * Get the latitude of the Upper Right pixel position.
     * 
     * @return The Upper Right latitude.
     */
    public double getUR_Lat() {
        return points[3];
    }

    /**
     * Get the longitude of the Lower Right pixel position.
     * 
     * @return The Lower Right longitude.
     */
    public double getLR_Lon() {
        return points[4];
    }

    /**
     * Get the latitude of the Lower Right pixel position.
     * 
     * @return The Lower Right latitude.
     */
    public double getLR_Lat() {
        return points[5];
    }

    /**
     * Get the longitude of the Lower Left pixel position.
     * 
     * @return The Lower Left longitude.
     */
    public double getLL_Lon() {
        return points[6];
    }

    /**
     * Get the latitude of the Lower Left pixel position.
     * 
     * @return The Lower Left latitude.
     */
    public double getLL_Lat() {
        return points[7];
    }

    /**
     * Create a closed bounding Polygon from the internal lon/lat points.
     * 
     * @return The bounding Polygon.
     * @throws ParseException
     *             An error occurred creating the polygon.
     */
    public Polygon getGeometry() throws GOESRProjectionException {
        Polygon polygon = null;
        try {
            Coordinate c0 = new Coordinate(getUL_Lon(), getUL_Lat());
            Coordinate c1 = new Coordinate(getUR_Lon(), getUR_Lat());
            Coordinate c2 = new Coordinate(getLR_Lon(), getLR_Lat());
            Coordinate c3 = new Coordinate(getLL_Lon(), getLL_Lat());
            GeometryFactory gf = new GeometryFactory();
            polygon = gf.createPolygon(
                    gf.createLinearRing(new Coordinate[] { c0, c1, c2, c3, c0 }),
                    null);
        } catch(Exception e) {
            throw new GOESRProjectionException("Error creating geometry", e);
        }
        return polygon;
    }
}
