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

import java.text.ParsePosition;

import javax.measure.UnconvertibleException;
import javax.measure.Unit;
import javax.measure.format.ParserException;

import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.raytheon.uf.common.units.UnitConv;
import com.raytheon.uf.edex.plugin.goesr.exception.GoesrProjectionException;
import com.raytheon.uf.edex.plugin.goesr.geospatial.GoesrSatelliteHeight;

import si.uom.SI;
import tec.uom.se.format.SimpleUnitFormat;
import ucar.nc2.Attribute;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

/**
 *
 * Base class for all envelope factories that find envelopes using the x and y
 * dimension variables.
 *
 * <pre>
 *
 * SOFTWARE HISTORY
 *
 * Date          Ticket#  Engineer    Description
 * ------------- -------- ----------- --------------------------
 * Apr 17, 2015  4336     bsteffen    Initial creation
 * May  8, 2019  7596     tgurney     Fixes for units upgrade
 *
 * </pre>
 *
 * @author bsteffen
 */
public abstract class AbstractDimensionEnvelopeFactory
        implements GoesrEnvelopeFactory {

    @Override
    public GoesrEnvelope getEnvelope(NetcdfFile cdfFile,
            CoordinateReferenceSystem crs) throws GoesrProjectionException {
        Variable x = cdfFile.findVariable("x");
        if (x == null || !x.isCoordinateVariable()) {
            return null;
        }
        Variable y = cdfFile.findVariable("y");
        if (y == null || !y.isCoordinateVariable()) {
            return null;
        }
        return getEnvelopeFromDimensions(cdfFile, crs, x, y);

    }

    protected abstract GoesrEnvelope getEnvelopeFromDimensions(
            NetcdfFile cdfFile, CoordinateReferenceSystem crs, Variable x,
            Variable y) throws GoesrProjectionException;

    protected static double findDistance(CoordinateReferenceSystem crs,
            double scale, Variable dimension) throws GoesrProjectionException {
        Attribute attr = dimension.findAttribute("units");
        if (attr == null) {
            return scale;
        }
        String units = attr.getStringValue();
        return findDistance(crs, scale, units);
    }

    protected static double findDistance(CoordinateReferenceSystem crs,
            double scale, String units) throws GoesrProjectionException {
        if ("meters".equals(units)) {
            return scale;
        } else if ("rad".equals(units) || "radian".equals(units)) {
            double orbitalHeight = GoesrSatelliteHeight.getOrbitalHeight(crs,
                    SI.METRE);
            if (Double.isNaN(orbitalHeight)) {
                return 1000 * scale / RADIANS_PER_KM_SPACING;
            } else {
                return Math.tan(scale) * orbitalHeight;
            }
        } else if ("microradian".equals(units) || "microrad".equals(units)) {
            return findDistance(crs, scale / 1000 / 1000, "rad");
        } else {
            try {
                Unit<?> u = SimpleUnitFormat
                        .getInstance(SimpleUnitFormat.Flavor.ASCII)
                        .parseProductUnit(units, new ParsePosition(0));
                if (u.isCompatible(SI.METRE)) {
                    UnitConv.getConverterToUnchecked(u, SI.METRE)
                            .convert(scale);
                } else {
                    throw new GoesrProjectionException(
                            "Incompatible units: " + units);
                }
            } catch (UnconvertibleException | ParserException e) {
                throw new GoesrProjectionException(
                        "Incompatible units: " + units, e);
            }
        }

        return scale;
    }

}
