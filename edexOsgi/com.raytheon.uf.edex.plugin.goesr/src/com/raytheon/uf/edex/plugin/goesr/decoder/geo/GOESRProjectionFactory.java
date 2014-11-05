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

import java.util.HashMap;
import java.util.Map;

import org.opengis.referencing.crs.CoordinateReferenceSystem;

import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

import com.raytheon.edex.plugin.satellite.dao.SatMapCoverageDao;
import com.raytheon.uf.common.dataplugin.satellite.SatMapCoverage;
import com.raytheon.uf.edex.plugin.goesr.decoder.GOESRAttributes;
import com.raytheon.uf.edex.plugin.goesr.decoder.GOESRConstants;
import com.raytheon.uf.edex.plugin.goesr.decoder.GOESRUtil;
import com.vividsolutions.jts.geom.Coordinate;

/**
 * Create the map projection corresponding to the projection information
 * contained in the GOES-R netCDF file.
 * 
 * <pre>
 * 
 * SOFTWARE HISTORY
 * 
 * Date          Ticket#  Engineer    Description
 * ------------- -------- ----------- --------------------------
 * Jun 01, 2012  796      jkorman     Initial creation
 * Jul 05, 2013  2123     mschenke    Refactored to have CRS factory for each type of CRS
 * Oct 29, 2014  3770     bsteffen    Pass more attributes to the projection.
 * 
 * 
 * </pre>
 * 
 * @author jkorman
 * @version 1.0
 */

public class GOESRProjectionFactory {

    public abstract static class AbstractGOESRProjectionFactoryImplementation {

        public GOESRProjection constructProjection(Variable projData,
                GOESRAttributes attributes) throws GOESRProjectionException {
            GOESRProjection projection = new GOESRProjection(getName(projData),
                    constructCoordinateReferenceSystem(projData, attributes));
            projection.setTileDimensions(getNx(projData, attributes),
                    getNy(projData, attributes), getDx(projData, attributes),
                    getDy(projData, attributes));
            projection.setTileCenterPoint(getTileCenterPoint(projData,
                    attributes));
            /*
             * Give the projection factory all the information available and let
             * it choose the geolocation method.
             */
            Coordinate productCenter = new Coordinate(
                    attributes.getProduct_center_longitude(),
                    attributes.getProduct_center_latitude());
            projection.setProductTileParameters(productCenter,
                    attributes.getProduct_columns(),
                    attributes.getProduct_rows(),
                    attributes.getTile_column_offset(),
                    attributes.getTile_row_offset());
            return projection;

        }

        public double getDx(Variable projData, GOESRAttributes attributes) {
            // convert km to m for dx
            return attributes.getPixel_x_size() * 1000;
        }

        public double getDy(Variable projData, GOESRAttributes attributes) {
            // convert km to m for dx
            return attributes.getPixel_y_size() * 1000;
        }

        public int getNx(Variable projData, GOESRAttributes attributes) {
            return attributes.getProduct_tile_width();
        }

        public int getNy(Variable projData, GOESRAttributes attributes) {
            return attributes.getProduct_tile_height();
        }

        public Coordinate getTileCenterPoint(Variable projData,
                GOESRAttributes attributes) {
            return new Coordinate(attributes.getTile_center_longitude(),
                    attributes.getTile_center_latitude());
        }

        public String getName(Variable projData) {
            return GOESRUtil
                    .getAttributeString(
                            projData.findAttribute(GOESRConstants.PROJ_ATTR_GRID_MAPPING_NAME_ID),
                            "UNKNOWN");
        }

        public abstract CoordinateReferenceSystem constructCoordinateReferenceSystem(
                Variable projData, GOESRAttributes attributes)
                throws GOESRProjectionException;
    }

    private Map<String, AbstractGOESRProjectionFactoryImplementation> projMap = new HashMap<String, AbstractGOESRProjectionFactoryImplementation>();
    {
        projMap.put(GOESRConstants.PROJECTION_LAMBERT_ID,
                new LambertConformal());
        projMap.put(GOESRConstants.PROJECTION_MERCATOR_ID, new Mercator());
        projMap.put(GOESRConstants.PROJECTION_POLAR_ID,
                new PolarStereographic());
        projMap.put(GOESRConstants.PROJECTION_FIXEDGRID_ID, new FixedGrid());
    }

    private SatMapCoverageDao satDao;

    /**
     * Create an instance of this factory.
     */
    public GOESRProjectionFactory() {
        satDao = new SatMapCoverageDao();
    }

    /**
     * Create the correct projection based on the netCDF data and attributes.
     * 
     * @return The map projection information. A null reference is created if
     *         not map projection could be created.
     */
    public GOESRProjection createProjection(NetcdfFile cdfFile,
            String projName, GOESRAttributes attributes)
            throws GOESRProjectionException {
        Variable projection = cdfFile.findVariable(projName);
        if (projection != null) {
            AbstractGOESRProjectionFactoryImplementation factory = projMap
                    .get(projName);
            if (factory != null) {
                return factory.constructProjection(projection, attributes);
            } else {
                String message = String.format(
                        "Invalid projection identifier [%s].", projName);
                throw new GOESRProjectionException(message);
            }
        } else {
            throw new GOESRProjectionException("Projection variable was null");
        }
    }

    /**
     * Create a coverage object from a given projection.
     * 
     * @param projection
     *            The projection information to create the coverage from.
     * @return The created coverage.
     * @throws GOESRProjectionException
     *             An error occurred creating the coverage.
     */
    public SatMapCoverage getCoverage(GOESRProjection projection)
            throws GOESRProjectionException {
        SatMapCoverage coverage = projection.getCoverage();

        try {
            coverage = satDao.getOrCreateCoverage(coverage);
        } catch (Exception e) {
            throw new GOESRProjectionException("Could not create coverage", e);
        }
        return coverage;
    }

}
