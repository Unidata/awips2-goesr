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
import com.raytheon.uf.edex.plugin.goesr.decoder.GOESRAttributes;
import com.raytheon.uf.edex.plugin.goesr.decoder.GOESRConstants;
import com.raytheon.uf.edex.plugin.goesr.decoder.GOESRUtil;
import com.raytheon.uf.edex.plugin.goesr.decoder.geo.GOESRProjectionFactory.GOESRCoordinateReferenceSystemFactory;

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
 * Jun 1, 2012         796 jkorman     Initial creation
 * Jul 5, 2013        2123 mschenke    Refactored to be CRS factory
 * 
 * </pre>
 * 
 * @author jkorman
 * @version 1.0
 */

public class PolarStereographic implements
        GOESRCoordinateReferenceSystemFactory {

    private static final String POLAR_PROJECTION_ID = "Stereographic_North_Pole";

    private static final DefaultMathTransformFactory dmtFactory = new DefaultMathTransformFactory();

    private static final double DEF_FALSE_EASTING = 0.0;

    private static final double DEF_FALSE_NORTHING = 0.0;

    // Default from the ICD.
    private static final double DEF_SEMI_MAJOR = 6371200;

    // Default from the ICD.
    private static final double DEF_SEMI_MINOR = 6371200;

    private static final double DEF_STD_PARALLEL = -9999;
    
    private static final double DEF_CENTRAL_MERIDIAN = -9999;
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
                DEF_STD_PARALLEL);

        double centralMeridian = GOESRUtil.getAttributeDouble(projData
                .findAttribute(GOESRConstants.PROJ_POLAR_ATTR_LON_POLE_ID),
                DEF_CENTRAL_MERIDIAN);

        try {
            ParameterValueGroup parameters = dmtFactory
                    .getDefaultParameters(POLAR_PROJECTION_ID);

            parameters.parameter("semi_major").setValue(semiMajor);
            parameters.parameter("semi_minor").setValue(semiMinor);
            parameters.parameter("standard_parallel_1").setValue(
                    standardParallel);
            parameters.parameter("central_meridian").setValue(centralMeridian);
            parameters.parameter("false_easting").setValue(falseEasting);
            parameters.parameter("false_northing").setValue(falseNorthing);
            return MapUtil.constructProjection(POLAR_PROJECTION_ID, parameters);
        } catch (NoSuchIdentifierException e) {
            throw new GOESRProjectionException(
                    "Unable to find projection by name: " + POLAR_PROJECTION_ID,
                    e);
        } catch (FactoryException e) {
            throw new GOESRProjectionException(
                    "Error constructing projected CRS", e);
        }
    }

}
