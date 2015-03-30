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

import javax.measure.unit.SI;

import org.opengis.referencing.crs.CoordinateReferenceSystem;

import ucar.nc2.Attribute;
import ucar.nc2.NetcdfFile;

import com.raytheon.uf.edex.plugin.goesr.geospatial.GoesrSatelliteHeight;

/**
 * 
 * Base class for {@link GoesrEnvelopeFactory}s that determine the envelope by
 * looking at global attributes on the netcdf file. This class provides a common
 * method for calculating nx,ny,dx,dy within an envelope and subclasses can
 * calculate minX and minY either by using the product center or tile center
 * attributes. Since the Level2 data types do not set these attributes,
 * factories of this type are generally only applicable to the sectorized CMI
 * files.
 * 
 * <pre>
 * 
 * SOFTWARE HISTORY
 * 
 * Date          Ticket#  Engineer    Description
 * ------------- -------- ----------- --------------------------
 * Apr 17, 2015  4043     bsteffen    Initial creation
 * 
 * </pre>
 * 
 * @author bsteffen
 * @version 1.0
 */
public abstract class AbstractCenterEnvelopeFactory implements
        GoesrEnvelopeFactory {

    protected GoesrEnvelope loadDistanceNumber(NetcdfFile cdfFile,
            CoordinateReferenceSystem crs) {
        GoesrEnvelope envelope = new GoesrEnvelope();
        Attribute attr = cdfFile.findGlobalAttribute("pixel_x_size");
        if (attr == null) {
            return null;
        } else {
            envelope.setDx(attr.getNumericValue().doubleValue() * 1000);
        }
        attr = cdfFile.findGlobalAttribute("pixel_y_size");
        if (attr == null) {
            return null;
        } else {
            envelope.setDy(attr.getNumericValue().doubleValue() * 1000);
        }
        attr = cdfFile.findGlobalAttribute("product_tile_width");
        if (attr == null) {
            return null;
        } else {
            envelope.setNx(attr.getNumericValue().intValue());
        }
        attr = cdfFile.findGlobalAttribute("product_tile_height");
        if (attr == null) {
            return null;
        } else {
            envelope.setNy(attr.getNumericValue().intValue());
        }
        double orbitalHeight = GoesrSatelliteHeight.getOrbitalHeight(crs,
                SI.KILOMETER);
        if (!Double.isNaN(orbitalHeight)) {
            // Geostationary dx/dy hack;
            double dx = envelope.getDx();
            double angularSeparation = (dx / 1000.0)
                    * RADIANS_PER_KM_SPACING;
            dx  = Math.tan(angularSeparation)
                    * orbitalHeight;
            envelope.setDx(dx * 1000);
            double dy = envelope.getDy();
            angularSeparation = (dy / 1000.0)
                    * RADIANS_PER_KM_SPACING;
            dy  = Math.tan(angularSeparation)
                    * orbitalHeight;
            envelope.setDy(dy * 1000);
        }

        return envelope;
    }

}