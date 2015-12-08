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
package com.raytheon.uf.edex.plugin.goesr.decoder.lookup;

import java.util.IllegalFormatException;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

import ucar.nc2.NetcdfFile;

import com.raytheon.uf.common.dataplugin.satellite.SatelliteRecord;
import com.raytheon.uf.edex.netcdf.description.AbstractFieldDescription;
import com.raytheon.uf.edex.netcdf.description.AttributeDescription;
import com.raytheon.uf.edex.netcdf.description.ValueDescription;
import com.raytheon.uf.edex.netcdf.description.VariableDescription;
import com.raytheon.uf.edex.netcdf.description.exception.InvalidDescriptionException;

/**
 * Applies a Format string to one or more Fields. If the field isn't present in
 * the NetCDF file, the value from a record's attributes will be used instead if
 * present.
 * 
 * <pre>
 * 
 * SOFTWARE HISTORY
 * 
 * Date         Ticket#    Engineer    Description
 * ------------ ---------- ----------- --------------------------
 * Nov 24, 2015 5059       nabowle     Initial creation
 *
 * </pre>
 *
 * @author nabowle
 * @version 1.0
 */
@XmlAccessorType(XmlAccessType.NONE)
public class FormattedFieldsDescription extends AbstractFieldDescription {

    @XmlAttribute
    private String format;

    @XmlElements({ @XmlElement(name = "value", type = ValueDescription.class),
            @XmlElement(name = "variable", type = VariableDescription.class),
            @XmlElement(name = "attribute", type = AttributeDescription.class) })
    private AbstractFieldDescription[] fields;

    /**
     * Constructor.
     */
    public FormattedFieldsDescription() {
        super();
    }

    public String getString(NetcdfFile file, SatelliteRecord record)
            throws InvalidDescriptionException {
        Object[] formatValues = new Object[fields.length];
        Object value;
        for (int i = 0; i < fields.length; i += 1) {
            value = getFormatValue(file, fields[i], record);
            if (value == null) {
                return null;
            }
            formatValues[i] = value;
        }
        try {
            return String.format(format, formatValues);
        } catch (IllegalFormatException | NullPointerException e) {
            throw new InvalidDescriptionException(
                    "Error formatting the values.", e);
        }
    }

    /**
     * Gets the formatValue for the field. If the field is present in the netcdf
     * file, the value from the file is returned. If the field isn't present but
     * a record is given, the value from the record's data attributes is
     * returned. Otherwise, null is returned.
     */
    private Object getFormatValue(NetcdfFile file,
            AbstractFieldDescription field, SatelliteRecord record)
            throws InvalidDescriptionException {
        Object ret;
        if (!field.isPresent(file)) {
            if (record != null) {
                ret = GoesrUtils
                        .getAttributeFromRecord(record, field.getName());
            } else {
                ret = null;
            }
        } else if (field.isNumeric(file)) {
            ret = field.getNumber(file);
        } else {
            ret = field.getString(file);
        }
        return ret;
    }

    @Override
    public String getString(NetcdfFile file) throws InvalidDescriptionException {
        return getString(file, null);
    }

    @Override
    public String getString(NetcdfFile file, int index)
            throws InvalidDescriptionException {
        return getString(file);
    }

    @Override
    public Number getNumber(NetcdfFile file) throws InvalidDescriptionException {
        String stringVal = getString(file);
        if (stringVal == null || stringVal.trim().isEmpty()) {
            return null;
        }
        try {
            return Double.parseDouble(stringVal);
        } catch (NumberFormatException e) {
            throw new InvalidDescriptionException(e);
        }
    }

    @Override
    public Number getNumber(NetcdfFile file, int index)
            throws InvalidDescriptionException {
        return getNumber(file);
    }

    @Override
    public long getLength(NetcdfFile file) {
        return 1;
    }

    @Override
    public boolean isNumeric(NetcdfFile file)
            throws InvalidDescriptionException {
        return false;
    }

    @Override
    public boolean isPresent(NetcdfFile file)
            throws InvalidDescriptionException {
        if (this.fields == null || this.fields.length == 0) {
            throw new InvalidDescriptionException("No fields are configured.");
        }
        for (AbstractFieldDescription field : this.fields) {
            if (!field.isPresent(file)) {
                return false;
            }
        }
        return true;
    }
}
