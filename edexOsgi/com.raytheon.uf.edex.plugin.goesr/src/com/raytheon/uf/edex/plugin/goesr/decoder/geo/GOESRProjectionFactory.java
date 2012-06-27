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

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

import com.raytheon.edex.plugin.satellite.dao.SatMapCoverageDao;
import com.raytheon.uf.common.dataplugin.satellite.SatMapCoverage;
import com.raytheon.uf.edex.plugin.goesr.decoder.GOESRAttributes;
import com.raytheon.uf.edex.plugin.goesr.decoder.GOESRConstants;

/**
 * Create the map projection corresponding to the projection information
 * contained in the GOES-R netCDF file.
 * 
 * <pre>
 * 
 * SOFTWARE HISTORY
 * 
 * Date         Ticket#    Engineer    Description
 * ------------ ---------- ----------- --------------------------
 * Jun 1, 2012        796 jkorman     Initial creation
 * 
 * </pre>
 * 
 * @author jkorman
 * @version 1.0
 */

public class GOESRProjectionFactory {

    private Map<String, Class<? extends GOESRProjection>> projMap = new HashMap<String, Class<? extends GOESRProjection>>();
    {
        projMap.put(GOESRConstants.PROJ_LAMBERT, LambertConformal.class);
        projMap.put(GOESRConstants.PROJ_MERCATOR, Mercator.class);
        projMap.put(GOESRConstants.PROJ_POLAR, PolarStereographic.class);
        projMap.put(GOESRConstants.PROJ_FIXEDGRID, FixedGrid.class);
    }

    private SatMapCoverageDao satDao = null;

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
        GOESRProjection proj = null;

        Variable projection = cdfFile.findVariable(projName);
        if (projection != null) {

            Class<? extends GOESRProjection> clazz = projMap.get(projName);
            if (clazz != null) {
                try {
                    Constructor<? extends GOESRProjection> c = clazz
                            .getConstructor(new Class[] { Variable.class,
                                    GOESRAttributes.class });
                    if (c != null) {
                        try {
                            proj = c.newInstance(new Object[] { projection,
                                    attributes });
                        } catch (Exception e) {
                            String msg = String
                                    .format("Could not create projection instance for [%s]",
                                            projection);
                            throw new GOESRProjectionException(msg, e);
                        }
                    }
                } catch (Exception e) {
                    String msg = String
                            .format("Could not create constructor for [%s]",
                                    projection);
                    throw new GOESRProjectionException(msg, e);
                }
            } else {
                String message = String.format(
                        "Invalid projection identifier [%s].", projName);
                throw new GOESRProjectionException(message);
            }
        } else {
            throw new GOESRProjectionException("Projection variable was null");
        }
        return proj;
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
            SatMapCoverage c = satDao.queryByMapId(coverage.getGid());
            if (c != null) {
                // We found a persisted coverage, so use it.
                coverage = c;
            } else {
                // Save the coverage we created.
                satDao.persist(coverage);
            }
        } catch (Exception e) {
            throw new GOESRProjectionException("Could not create coverage", e);
        }
        return coverage;
    }

}
