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
package com.raytheon.uf.edex.plugin.goesr.decoder;

import java.text.ParseException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ucar.ma2.DataType;
import ucar.nc2.Attribute;

import com.raytheon.uf.common.status.IUFStatusHandler;
import com.raytheon.uf.common.status.UFStatus;

/**
 * Some GOES-R utility methods.
 * 
 * <pre>
 * 
 * SOFTWARE HISTORY
 * 
 * Date          Ticket#  Engineer    Description
 * ------------- -------- ----------- --------------------------
 * Jun 22, 2012  796      jkorman     Initial creation
 * Oct 29, 2014  3770     bsteffen    Allow getAttributeDouble to handle an
 *                                    empty string without errors.
 * 
 * </pre>
 * 
 * @author jkorman
 * @version 1.0
 */
public class GOESRUtil {

    private static final transient IUFStatusHandler log = UFStatus
            .getHandler(GOESRUtil.class);

    private static Pattern DATE_PTRN = Pattern
            .compile("(\\d{4})(\\d{3})(\\d{2})(\\d{2})(\\d{2})");

    private static final String POINTS_FMT = "[%.5f %.5f]";

    /**
     * Parse the GOES-R date time string. The GOES-R date/time is in
     * the form "yyyydddhhmmss" where
     * <table>
     * <tr><td>yyyy</td><td>year</td></tr>
     * <tr><td>ddd</td><td>day of the year</td></tr>
     * <tr><td>hh</td><td>hour of the day</td></tr>
     * <tr><td>mm</td><td>minutes of the hour</td></tr>
     * <tr><td>ss</td><td>seconds</td></tr>
     * </table>
     * The implied time zone is UTC.
     * @param date
     *            The date time to convert.
     * @return The converted date time.
     */
    public static Date parseFileDate(String date, String attrName) {
        Date d = null;
        if (date != null) {
            Matcher m = DATE_PTRN.matcher(date);
            if (m.matches()) {
                try {
                    synchronized(GOESRConstants.GOESR_DT) {
                        d = GOESRConstants.GOESR_DT.parse(date);
                    }
                } catch (ParseException e) {
                    log.error("Could not parse datetime [" + date
                            + "] name = " + attrName);
                }
            }
        }
        return d;
    }

    /**
     * Convert a byte array to a String with no translation.
     * 
     * @param data
     *            A byte array to convert to a String
     * @return The converted data. If the input is null, a null reference is
     *         returned.
     */
    public static String toString(byte[] data) {
        String s = null;
        if (data != null) {
            if (data.length > 0) {
                char[] c = new char[data.length];
                for (int i = 0; i < data.length; i++) {
                    c[i] = (char) (data[i] & 0xFF);
                }
                // Now wrap the char array. 
                s = new String(c);
            } else {
                s = "";
            }
        }
        return s;
    }

    /**
     * Get the float value of a specified attribute.
     * 
     * @param attr
     *            The attributes to read.
     * @param defaultValue
     *            A default value.
     * @return The float value from the attribute, or the defaultValue.
     */
    public static float getAttributeFloat(Attribute attr, float defaultValue) {
        float retValue = defaultValue;
        if (attr != null) {
            Number n = null;
            if (DataType.STRING.equals(attr.getDataType())) {
                try {
                    n = Float.parseFloat(attr.getStringValue());
                } catch (NumberFormatException nfe) {
                    log.error("Could not parse Float value ["
                            + attr.getStringValue() + "] name = "
                            + attr.getName());
                }
            } else {
                n = attr.getNumericValue();
            }
            if (n != null) {
                retValue = n.floatValue();
            }
        }
        return retValue;
    }

    /**
     * Get the double value of a specified attribute.
     * 
     * @param attr
     *            The attributes to read.
     * @param defaultValue
     *            A default value.
     * @return The double value from the attribute, or the defaultValue.
     */
    public static double getAttributeDouble(Attribute attr, double defaultValue) {
        double retValue = defaultValue;
        if (attr != null) {
            Number n = null;
            if (DataType.STRING.equals(attr.getDataType())) {
                String stringValue = attr.getStringValue();
                /*
                 * The ICD indicates there are cases where double values should
                 * be sent as blank attributes if no reasonable value could be
                 * found(specifically tile_center_latitude and
                 * tile_center_longitude) so do not log an error in this case.
                 */
                if(!stringValue.isEmpty()){
                    try {
                        n = Double.parseDouble(attr.getStringValue());
                    } catch (NumberFormatException nfe) {
                        log.error("Could not parse Double value ["
                                + attr.getStringValue() + "] name = "
                                + attr.getName());
                    }
                }
            } else {
                n = attr.getNumericValue();
            }
            if (n != null) {
                retValue = n.doubleValue();
            }
        }
        return retValue;
    }

    /**
     * Get the String value of a specified attribute.
     * 
     * @param attr
     *            The attributes to read.
     * @param defaultValue
     *            A default value.
     * @return The String value from the attribute, or the defaultValue.
     */
    public static String getAttributeString(Attribute attr, String defaultValue) {
        String retValue = defaultValue;
        if (attr != null) {
            retValue = attr.getStringValue();
        }
        return retValue;
    }

    /**
     * Get the Integer value of a specified attribute.
     * 
     * @param attr
     *            The attributes to read.
     * @param defaultValue
     *            A default value.
     * @return The Integer value from the attribute, or the defaultValue.
     */
    public static Integer getAttributeInteger(Attribute attr,
            Integer defaultValue) {
        Integer retValue = defaultValue;
        if (attr != null) {
            Number n = null;
            if (DataType.STRING.equals(attr.getDataType())) {
                try {
                    n = Integer.parseInt(attr.getStringValue());
                } catch (NumberFormatException nfe) {
                    log.error("Could not parse Integer value ["
                            + attr.getStringValue() + "] name = "
                            + attr.getName());
                }
            } else {
                n = attr.getNumericValue();
            }
            if (n != null) {
                retValue = n.intValue();
            }
        }
        return retValue;
    }

    /**
     * Get the date-time value of a specified attribute.
     * 
     * @param attr
     *            The attributes to read.
     * @param defaultValue
     *            A default date.
     * @return The date-time from the attribute, or the defaultValue.
     */
    public static Date getAttributeDate(Attribute attr, Date defaultValue) {
        Date retValue = defaultValue;
        if (attr != null) {
            if (DataType.STRING.equals(attr.getDataType())) {
                retValue = parseFileDate(attr.getStringValue(), attr.getName());
            }
        }
        return retValue;
    }

    /**
     * Create a string representation of a double array. This array is
     * assumed to be a set of points where a point is Point(n) (array[(n-1)*2], array[((n-1)*2) + 1]),
     * @param points An array of points to convert.
     * @param StringBuilder to accept the formatted data. If the target is null, a new StringBuilder
     * is created.
     * @return The formatted points.
     */
    public static StringBuilder formatCornerPoints(double [] points, StringBuilder target) {
        if(target == null) {
            target = new StringBuilder();
        }
        target.append("{");
        if((points != null) && (points.length > 0)) {
            int size = points.length;
            if(size % 2 == 1) {
                size-=2;
            } else {
                size--;
            }
            for (int i = 0; i < size; i += 2) {
                target.append(String.format(POINTS_FMT, points[i], points[i + 1]));
                if(i+2 < size) {
                    target.append(",");
                }
            }
        }
        target.append("}");

        return target;
    }
    
    /**
     * Hide the default constructor
     */
    private GOESRUtil() {
    }
}
