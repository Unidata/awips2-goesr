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
package com.raytheon.uf.edex.plugin.goesr.geospatial.envelope;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.measure.unit.SI;

import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.ProjectedCRS;
import org.opengis.referencing.datum.Ellipsoid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.raytheon.uf.edex.plugin.goesr.exception.GoesrProjectionException;
import com.raytheon.uf.edex.plugin.goesr.geospatial.GoesrSatelliteHeight;

import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

/**
 * 
 * Uses the scale and offset and size of the x and y variables to determine the
 * size of the product. This factory should work for all GOES-R products.
 * 
 * <pre>
 * 
 * SOFTWARE HISTORY
 * 
 * Date          Ticket#  Engineer  Description
 * ------------- -------- --------- --------------------------------------------
 * Apr 17, 2015  4336     bsteffen  Initial creation
 * Mar 15, 2016  5456     bsteffen  Add 0.5 to shift from cell center to cell
 *                                  corner.
 * Nov 15, 2016  5994     bsteffen  Log a warning for potentially problematic
 *                                  Geostationary envelopes.
 * 
 * </pre>
 * 
 * @author bsteffen
 */
public class DimensionEnvelopeFactory extends AbstractDimensionEnvelopeFactory {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public GoesrEnvelope getEnvelopeFromDimensions(NetcdfFile cdfFile,
            CoordinateReferenceSystem crs, Variable x, Variable y)
                    throws GoesrProjectionException {
        int nx = x.getDimension(0).getLength();
        int ny = y.getDimension(0).getLength();

        double scalex = x.findAttribute("scale_factor").getNumericValue()
                .doubleValue();
        double offsetx = x.findAttribute("add_offset").getNumericValue()
                .doubleValue();
        double scaley = y.findAttribute("scale_factor").getNumericValue()
                .doubleValue();
        double offsety = y.findAttribute("add_offset").getNumericValue()
                .doubleValue();
        double dx = findDistance(crs, scalex, x);
        double dy = findDistance(crs, scaley, y);

        double minx;
        double miny;
        try {

            minx = x.read().getInt(0);
            miny = y.read().getInt(0);
        } catch (IOException e) {
            throw new GoesrProjectionException(
                    "Unable to read min values of x or y dimesnions.", e);
        }

        /*
         * The variables define the position of the center of the data value,
         * expand by half a pixel to get the edge of the "image"
         */
        minx -= 0.5;
        miny -= 0.5;

        /* Convert from pixel spacing to meter spacing. */
        minx = (minx + offsetx / scalex) * dx;
        miny = (miny + offsety / scaley) * dy;

        GoesrEnvelope envelope = new GoesrEnvelope();
        envelope.setNx(nx);
        envelope.setNy(ny);
        envelope.setDx(dx);
        envelope.setDy(dy);
        envelope.setMinX(minx);
        envelope.setMinY(miny);

        validate(cdfFile, crs, envelope);

        return envelope;
    }

    /**
     * Detect common errors and log warnings that the envelope may be invalid.
     * Specifically this will detect if the data has an incorrect sign on the
     * scale or offset of the y variable which results in a product that is
     * impossible to display because it doesn't contain any points on the
     * reference ellipsoid. This has been a common problem in sample data so it
     * is helpful for the decoder to automatically diagnose the problem.
     * 
     * @param cdfFile
     *            the source file.
     * @param crs
     *            the coordinate system of the envelope
     * @param envelope
     *            the envelope.
     */
    private void validate(NetcdfFile cdfFile, CoordinateReferenceSystem crs,
            GoesrEnvelope envelope) {
        if (crs instanceof ProjectedCRS
                && crs.getName().getCode().equals("Geostationary")) {
            Ellipsoid ellpsoid = ((ProjectedCRS) crs).getBaseCRS().getDatum()
                    .getEllipsoid();
            double semiMajor = ellpsoid.getSemiMajorAxis();
            double orbitalHeight = GoesrSatelliteHeight.getOrbitalHeight(crs,
                    SI.METER);
            double maxValid = semiMajor * orbitalHeight
                    / (orbitalHeight + semiMajor);
            double minValid = -1 * maxValid;

            /*
             * Products near the edge of the disk normally have a min value just
             * outside of the range [minValid,maxValid] but the dx/dy values
             * result in a majority of the product being within the valid range.
             * These checks detect the case where the dx,dy just make the rest
             * of the product further out of range.
             */

            if (envelope.getMinY() > maxValid && envelope.getDy() > 0) {
                logGeostationaryWarning(cdfFile, "above", "y");
            } else if (envelope.getMinY() < minValid && envelope.getDy() < 0) {
                logGeostationaryWarning(cdfFile, "below", "y");
            }
            if (envelope.getMinX() > maxValid && envelope.getDx() > 0) {
                logGeostationaryWarning(cdfFile, "right of", "x");
            } else if (envelope.getMinX() < minValid && envelope.getDx() < 0) {
                logGeostationaryWarning(cdfFile, "left of", "x");
            }
        }
    }

    /**
     * Log a verbose warning that a geostationary envelope is outside the
     * reference ellipsoid.
     * 
     * @param cdfFile
     *            the source file
     * @param location
     *            the location of the envelope relative to the reference
     *            ellipsoid(left, right, above or below).
     * @param variableName
     *            the name of the variable that is presumed to be incorrect
     */
    private void logGeostationaryWarning(NetcdfFile cdfFile, String location,
            String variableName) {
        Path shortFilename = Paths.get(cdfFile.getLocation()).getFileName();
        Variable var = cdfFile.findVariable(variableName);
        Number scale = var.findAttribute("scale_factor").getNumericValue();
        Number offset = var.findAttribute("add_offset").getNumericValue();
        logger.warn(
                "Geostationary envelope is defined {} the reference ellipsoid and may not be viewable on a map."
                        + " The scale_factor({}) and add_offset({}) of the {} variable in {} may be incorrect.",
                location, scale, offset, variableName, shortFilename);
    }

}
