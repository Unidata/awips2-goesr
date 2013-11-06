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

import org.geotools.referencing.operation.DefaultMathTransformFactory;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchIdentifierException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import ucar.nc2.Variable;

import com.raytheon.uf.common.geospatial.MapUtil;
import com.raytheon.uf.common.geospatial.projection.Geostationary;
import com.raytheon.uf.edex.plugin.goesr.decoder.GOESRAttributes;
import com.raytheon.uf.edex.plugin.goesr.decoder.GOESRConstants;
import com.raytheon.uf.edex.plugin.goesr.decoder.GOESRUtil;
import com.raytheon.uf.edex.plugin.goesr.decoder.geo.GOESRProjectionFactory.AbstractGOESRProjectionFactoryImplementation;

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
 * Jun 1, 2012        796  jkorman     Initial creation
 * Jul 5, 2013       2123  mschenke    Implemented Geostationary projection
 * </pre>
 * 
 * @author jkorman
 * @version 1.0
 */

public class FixedGrid extends AbstractGOESRProjectionFactoryImplementation {

    private static final DefaultMathTransformFactory dmtFactory = new DefaultMathTransformFactory();

    /** Multiplier for angular separation. From SE-21 Metadata Model */
    private static final double RADIANS_PER_KM_SPACING = 28 * 1e-6;

    private static final double DEF_FALSE_EASTING = 0.0;

    private static final double DEF_FALSE_NORTHING = 0.0;

    // Default from the ICD.
    private static final double DEF_SEMI_MAJOR = 6378137.0;

    // Default from the ICD.
    private static final double DEF_SEMI_MINOR = 6356732.31414;

    // Default satellite height in meters, from the ICD.
    private static final double DEF_SAT_HEIGHT = 35786023.0;

    private static final String DEF_SWEEP_AXIS = "x";

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.raytheon.uf.edex.plugin.goesr.decoder.geo.GOESRProjectionFactory.
     * GOESRCoordinateReferenceSystemFactory
     * #constructCoordinateReferenceSystem(ucar.nc2.Variable,
     * com.raytheon.uf.edex.plugin.goesr.decoder.GOESRAttributes)
     */
    @Override
    public CoordinateReferenceSystem constructCoordinateReferenceSystem(
            Variable projData, GOESRAttributes attributes)
            throws GOESRProjectionException {
        double falseEasting = GOESRUtil.getAttributeDouble(projData
                .findAttribute(GOESRConstants.PROJ_ATTR_FALSE_EASTING_ID),
                DEF_FALSE_EASTING);
        double falseNorthing = GOESRUtil.getAttributeDouble(projData
                .findAttribute(GOESRConstants.PROJ_ATTR_FALSE_NORTHING_ID),
                DEF_FALSE_NORTHING);
        double semiMajor = GOESRUtil.getAttributeDouble(
                projData.findAttribute(GOESRConstants.PROJ_ATTR_SEMI_MAJOR_ID),
                DEF_SEMI_MAJOR);
        double semiMinor = GOESRUtil.getAttributeDouble(
                projData.findAttribute(GOESRConstants.PROJ_ATTR_SEMI_MINOR_ID),
                DEF_SEMI_MINOR);

        double standardParallel = GOESRUtil.getAttributeDouble(
                projData.findAttribute(GOESRConstants.PROJ_ATTR_ORIGIN_LAT_ID),
                -9999);

        double centralMeridian = GOESRUtil.getAttributeDouble(
                projData.findAttribute(GOESRConstants.PROJ_ATTR_ORIGIN_LON_ID),
                -9999);

        double satelliteOrbitalHeight = getSatelliteOrbitalHeight(projData);
        String sweepAxis = GOESRUtil
                .getAttributeString(
                        projData.findAttribute(GOESRConstants.PROJ_FIXEDGRID_ATTR_SWEEP_ANGLE_AXIS_ID),
                        DEF_SWEEP_AXIS);

        try {
            ParameterValueGroup parameters = dmtFactory
                    .getDefaultParameters(Geostationary.PROJECTION_NAME);

            parameters.parameter("semi_major").setValue(semiMajor);
            parameters.parameter("semi_minor").setValue(semiMinor);
            parameters.parameter("latitude_of_origin").setValue(
                    standardParallel);
            parameters.parameter("central_meridian").setValue(centralMeridian);
            parameters.parameter("false_easting").setValue(falseEasting);
            parameters.parameter("false_northing").setValue(falseNorthing);
            parameters.parameter(Geostationary.ORBITAL_HEIGHT).setValue(
                    satelliteOrbitalHeight);
            parameters.parameter(Geostationary.SWEEP_AXIS).setValue(
                    DEF_SWEEP_AXIS.equals(sweepAxis) ? 0 : 1);

            return MapUtil.constructProjection(Geostationary.PROJECTION_NAME,
                    parameters);
        } catch (NoSuchIdentifierException e) {
            throw new GOESRProjectionException(
                    "Unable to find projection by name: "
                            + Geostationary.PROJECTION_NAME, e);
        } catch (FactoryException e) {
            throw new GOESRProjectionException(
                    "Error constructing projected CRS", e);
        }
    }

    @Override
    public double getDx(Variable projData, GOESRAttributes attributes) {
        return getActualGridSpacing(super.getDx(projData, attributes),
                projData, attributes);
    }

    @Override
    public double getDy(Variable projData, GOESRAttributes attributes) {
        return getActualGridSpacing(super.getDy(projData, attributes),
                projData, attributes);
    }

    private double getActualGridSpacing(double gridSpacing, Variable projData,
            GOESRAttributes attributes) {
        double angularSeparation = (gridSpacing / 1000.0)
                * RADIANS_PER_KM_SPACING;
        return Math.tan(angularSeparation)
                * getSatelliteOrbitalHeight(projData);
    }

    private double getSatelliteOrbitalHeight(Variable projData) {
        return GOESRUtil
                .getAttributeDouble(
                        projData.findAttribute(GOESRConstants.PROJ_FIXEDGRID_ATTR_PERSPECTIVE_POINT_HEIGHT_ID),
                        DEF_SAT_HEIGHT);
    }
}
