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

import java.util.Arrays;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;
import com.vividsolutions.jts.io.ParseException;

/**
 * GeoRectange wraps an array of double values representing the corner points of
 * a rectangle. The corner point values represent crs x,y pairs where the even
 * value of the array is the longitude and the odd value is the latitude. Note
 * that the array only uses the four corner points and does not close on itself.
 * <table>
 * <tr>
 * <td>UL.x:p[0],UL.y:p[1]</td>
 * <td>---------</td>
 * <td>UR.x:p[2],UR.y:p[3]</td>
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
 * <td>LL.x:p[6],LL.y:p[7]</td>
 * <td>---------</td>
 * <td>LR.x:p[4],LR.y:p[5]</td>
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
 * Jul  5, 2013       2123 mschenke    Cleaned up, changed to use crs space
 * </pre>
 * 
 * @author jkorman
 * @version 1.0
 */

public class GeoRectangle {

    private final double[] points;

    /**
     * Create an instance from a set of given corner points in lat/lon space.
     * 
     * {UL_X, UL_Y, UR_X, UR_Y, LR_X, LR_Y, LL_X, LL_Y}
     * 
     * @param points
     *            A not null array of corner points to set.
     */
    public GeoRectangle(double[] points) {
        if (points == null) {
            throw new IllegalArgumentException("corner points must not be null");
        }
        this.points = Arrays.copyOf(points, points.length);
    }

    /**
     * Get a copy of the internal corner points data.
     * 
     * @param points
     *            A copy of the internal corner points data.
     */
    public double[] getPoints() {
        return Arrays.copyOf(points, points.length);
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
     * Create a closed bounding Polygon from the internal lon/lat points.
     * 
     * @return The bounding Polygon.
     * @throws ParseException
     *             An error occurred creating the polygon.
     */
    public Polygon getGeometry() throws GOESRProjectionException {
        Polygon polygon = null;
        try {
            Coordinate c0 = new Coordinate(getUL_X(), getUL_Y());
            Coordinate c1 = new Coordinate(getUR_X(), getUR_Y());
            Coordinate c2 = new Coordinate(getLR_X(), getLR_Y());
            Coordinate c3 = new Coordinate(getLL_X(), getLL_Y());
            GeometryFactory gf = new GeometryFactory();
            polygon = gf.createPolygon(gf.createLinearRing(new Coordinate[] {
                    c0, c1, c2, c3, c0 }), null);
        } catch (Exception e) {
            throw new GOESRProjectionException("Error creating geometry", e);
        }
        return polygon;
    }
}
