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

import org.geotools.geometry.DirectPosition2D;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.raytheon.edex.util.satellite.SatSpatialFactory;
import com.raytheon.uf.common.dataplugin.satellite.SatMapCoverage;
import com.raytheon.uf.common.geospatial.MapUtil;
import com.vividsolutions.jts.geom.Coordinate;

/**
 * Base class for GOES-R map projections.
 * 
 * <pre>
 * 
 * SOFTWARE HISTORY
 * 
 * Date          Ticket#  Engineer    Description
 * ------------- -------- ----------- --------------------------
 * Jun 01, 2012  796      jkorman     Initial creation
 * Jul 05, 2013  2123     mschenke    Refactored to create GOES-R Projection given a CRS
 * Oct 29, 2014  3770     bsteffen    Calculate corners from product center instead of tile center.
 * 
 * </pre>
 * 
 * @author jkorman
 * @version 1.0
 */

public class GOESRProjection {

    /** Name of the projection. */
    private final String name;

    /** Calculated Coordinate Reference System */
    private final CoordinateReferenceSystem crs;

    /** Hold on to the transform for this projection. */
    private final MathTransform latLonToCRS;

    private double dx;

    private double dy;

    private int nx;

    private int ny;

    private Coordinate tileCenterPoint;

    private int tileOffsetX;
    
    private int tileOffsetY;
    
    private Coordinate productCenterPoint;
    
    private int productWidth;
    
    private int productHeight;
    
    public GOESRProjection(String name, CoordinateReferenceSystem crs)
            throws GOESRProjectionException {
        super();
        this.name = name;
        this.crs = crs;
        try {
            this.latLonToCRS = MapUtil.getTransformFromLatLon(crs);
        } catch (FactoryException e) {
            throw new GOESRProjectionException(
                    "Unable to get tranform from lat/lon to projection crs with name: "
                            + name);
        }
    }

    /**
     * Set the parameters defining the tile dimensions. These parameters MUST be
     * set before calling {@link #getCoverage()}.
     * 
     * @param nx
     *            number of grid points in the width of the tile.
     * @param ny
     *            number of grid points in the height of the tile.
     * @param dx
     *            spacing(in meters) between grid cells in x direction
     * @param dy
     *            spacing(in meters) between grid cells in y direction
     */
    public void setTileDimensions(int nx, int ny, double dx, double dy) {
        this.nx = nx;
        this.ny = ny;
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Set the Lat/Lon of the tile center which can be used for geolocating the
     * tile. This information is not used if
     * {@link #setProductTileParameters(Coordinate, int, int, int, int)} is
     * given usable parameters, one of the two methods MUST be called before
     * calling {@link #getCoverage()}.
     */
    public void setTileCenterPoint(Coordinate tileCenterPoint) {
        this.tileCenterPoint = tileCenterPoint;
    }

    /**
     * Set the parameters necessary for geolocating this tile relative to the
     * product location. This method of geolocation is preferred to
     * {@link #setTileCenterPoint(Coordinate)}, one of the two methods MUST be
     * called before calling {@link #getCoverage()}.
     * 
     * @param productCenterPoint
     *            center point of product in Lat/Lon
     * @param productWidth
     *            number of grid points in the width of the product.
     * @param productHeight
     *            number of grid points in the height of the product.
     * @param tileOffsetX
     *            number of grid points that this tile is offset from the
     *            product in the x direction.
     * @param tileOffsetY
     *            number of grid points that this tile is offset from the
     *            product in the y direction.
     */
    public void setProductTileParameters(Coordinate productCenterPoint, int productWidth, int productHeight, int tileOffsetX, int tileOffsetY){
        this.productCenterPoint = productCenterPoint;
        this.productWidth = productWidth;
        this.productHeight = productHeight;
        this.tileOffsetX = tileOffsetX;
        this.tileOffsetY = tileOffsetY;
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
     * Get the coverage object for the projection information.
     * 
     * @return The coverage object for the projection information.
     * @throws GOESRProjectionException
     *             An error occurred attempting to create the coverage.
     */
    protected SatMapCoverage getCoverage() throws GOESRProjectionException {
        GeoRectangle bounds = null;
        if (productCenterPoint != null) {
            /*
             * The product center method is more reliable because some tiles in
             * the geostationary projection have a center that is off the edge
             * of the earth and cannot be used.
             */
            bounds = calcCornerPointsFromProductCenter();
        } else {
            bounds = calcCornerPointsFromTileCenter();
        }
        double minX = bounds.getLL_X();
        double minY = bounds.getLL_Y();
        return new SatMapCoverage(SatSpatialFactory.UNDEFINED, minX, minY,
                getNx(), getNy(), getDx(), getDy(), crs);
    }

    public double getDx() {
        return dx;
    }

    public double getDy() {
        return dy;
    }

    public int getNx() {
        return nx;
    }

    public int getNy() {
        return ny;
    }

    /**
     * Using projection information, calculate the corner points of the data in
     * crs space. This version uses the tile center and tile size.
     * 
     * @return The calculated corner points.
     */
    private GeoRectangle calcCornerPointsFromTileCenter()
            throws GOESRProjectionException {
        double tileCenterLat = tileCenterPoint.y;
        double tileCenterLon = tileCenterPoint.x;

        double[] in = new double[] { tileCenterLon, tileCenterLat };
        double[] out = new double[in.length];

        try {
            latLonToCRS.transform(in, 0, out, 0, 1);
        } catch (TransformException e) {
            throw new GOESRProjectionException(
                    "Error transforming tile center point to CRS space", e);
        }

        double tileCRSCenterX = out[0];
        double tileCRSCenterY = out[1];

        double dx = getDx();
        double dy = getDy();

        int nx = getNx();
        int ny = getNy();

        double crsTileXOffset = (nx * dx) / 2.0;
        double crsTileYOffset = (ny * dy) / 2.0;

        in = new double[8];
        out = new double[in.length];

        // UL
        in[0] = tileCRSCenterX - crsTileXOffset;
        in[1] = tileCRSCenterY + crsTileYOffset;

        // UR
        in[2] = tileCRSCenterX + crsTileXOffset;
        in[3] = tileCRSCenterY + crsTileYOffset;

        // LR
        in[4] = tileCRSCenterX + crsTileXOffset;
        in[5] = tileCRSCenterY - crsTileYOffset;

        // LL
        in[6] = tileCRSCenterX - crsTileXOffset;
        in[7] = tileCRSCenterY - crsTileYOffset;

        return new GeoRectangle(in);
    }

    /**
     * Using projection information, calculate the corner points of the data in
     * crs. This version uses the product center, product size, tile offset, and
     * tile size.
     * 
     * @return The calculated corner points.
     */
    private GeoRectangle calcCornerPointsFromProductCenter()
            throws GOESRProjectionException {
        DirectPosition2D productCenter = new DirectPosition2D(productCenterPoint.x, productCenterPoint.y);
        try {
            latLonToCRS.transform(productCenter, productCenter);
        } catch (TransformException e) {
            throw new GOESRProjectionException(
                    "Error transforming tile center point to CRS space", e);
        }
        
        double left = productCenter.x + (tileOffsetX - productWidth / 2.0) * dx;
        double right = left + nx*dx;
        
        double top = productCenter.y - (tileOffsetY - productHeight / 2.0) * dy;
        double bottom = top - ny * dy;

        double[] corners = new double[8];

        // UL
        corners[0] = left;
        corners[1] = top;

        // UR
        corners[2] = right;
        corners[3] = top;

        // LR
        corners[4] = right;
        corners[5] = bottom;

        // LL
        corners[6] = left;
        corners[7] = bottom;

        return new GeoRectangle(corners);
    }
}
