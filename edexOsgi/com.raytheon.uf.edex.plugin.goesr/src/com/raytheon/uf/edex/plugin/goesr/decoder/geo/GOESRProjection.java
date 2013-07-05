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
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.raytheon.uf.common.dataplugin.satellite.SatMapCoverage;
import com.raytheon.uf.common.geospatial.MapUtil;
import com.raytheon.uf.edex.plugin.goesr.decoder.GOESRAttributes;

/**
 * Base class for GOES-R map projections.
 * 
 * <pre>
 * 
 * SOFTWARE HISTORY
 * 
 * Date         Ticket#    Engineer    Description
 * ------------ ---------- ----------- --------------------------
 * Jun 1, 2012         796 jkorman     Initial creation
 * Jul 5, 2013        2123 mschenke    Refactored to create GOES-R Projection given a CRS
 * 
 * </pre>
 * 
 * @author jkorman
 * @version 1.0
 */

public class GOESRProjection {

    /** Name of the projection. */
    private final String name;

    /** Global attributes from the GOES-R data. */
    private final GOESRAttributes attributes;

    /** Calculated Coordinate Reference System */
    private final CoordinateReferenceSystem crs;

    /** Hold on to the transform for this projection. */
    private final MathTransform latLonToCRS;

    public GOESRProjection(GOESRAttributes attributes, String name,
            CoordinateReferenceSystem crs) throws GOESRProjectionException {
        this.attributes = attributes;
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
        GeoRectangle bounds = calcCornerPoints();
        double minX = bounds.getLL_X();
        double minY = bounds.getLL_Y();
        return new SatMapCoverage(minX, minY, getNx(), getNy(), getDx(),
                getDy(), crs, bounds.getGeometry());
    }

    public float getDx() {
        // convert km to m for dx
        return attributes.getPixel_x_size() * 1000;
    }

    public float getDy() {
        // convert km to m for dy
        return attributes.getPixel_y_size() * 1000;
    }

    public int getNx() {
        return attributes.getProduct_tile_width();
    }

    public int getNy() {
        return attributes.getProduct_tile_height();
    }

    /**
     * Using projection information, calculate the corner points of the data in
     * latitude/longitude space.
     * 
     * @return The calculated corner points.
     */
    private GeoRectangle calcCornerPoints() throws GOESRProjectionException {
        float tileCenterLat = attributes.getTile_center_latitude();
        float tileCenterLon = attributes.getTile_center_longitude();

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
}
