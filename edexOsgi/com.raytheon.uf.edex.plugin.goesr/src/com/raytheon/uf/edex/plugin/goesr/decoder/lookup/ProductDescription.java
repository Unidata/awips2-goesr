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

import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

import org.geotools.xml.schema.AttributeValue;

import ucar.nc2.NetcdfFile;

import com.raytheon.uf.common.dataplugin.satellite.SatelliteRecord;
import com.raytheon.uf.edex.netcdf.description.AbstractFieldDescription;
import com.raytheon.uf.edex.netcdf.description.AttributeDescription;
import com.raytheon.uf.edex.netcdf.description.ValueDescription;
import com.raytheon.uf.edex.netcdf.description.VariableDescription;
import com.raytheon.uf.edex.netcdf.description.date.DataTimeDescription;
import com.raytheon.uf.edex.netcdf.description.exception.InvalidDescriptionException;
import com.raytheon.uf.edex.plugin.goesr.exception.GoesrDecoderException;
import com.raytheon.uf.edex.plugin.goesr.exception.GoesrProjectionException;
import com.raytheon.uf.edex.plugin.goesr.geospatial.GoesrProjectionFactory;

/**
 * 
 * Contains the information necessary to match a {@link NetcdfFile} and its
 * global attributes to a {@link SatelliteRecord}. Logically this class is
 * composed of three parts, the {@link AttributeMatcher}s, the
 * {@link DataDescription}, and the {@link AttributeValue}s.
 * 
 * <ul>
 * <li>The {@link AttributeMatcher}s are evaluated to decide if this description
 * can be applied to the specified file.
 * <li>The {@link DataDescription} is optional, when it is present it describes
 * how the data variables with the file are mapped into the message data of the
 * SatelliteRecord.
 * <li>The {@link AbstractFieldDescription}s describe how to map netcdf fields
 * to the attributes on the satellite record.
 * </ul>
 * 
 * <pre>
 * 
 * SOFTWARE HISTORY
 * 
 * Date          Ticket#  Engineer    Description
 * ------------- -------- ----------- --------------------------
 * Apr 17, 2015  4336     bsteffen    Initial creation
 * Dec 04, 2015  5059     nabowle     Use common netcdf description classes
 *                                    where possible.
 * 
 * </pre>
 * 
 * @author bsteffen
 * @version 1.0
 */
@XmlAccessorType(XmlAccessType.NONE)
public class ProductDescription {

    @XmlElement(name = "match")
    private List<AttributeMatcher> matches;

    @XmlElement
    private DataDescription data;

    @XmlElements({
            @XmlElement(name = "physicalElement", type = ValueDescription.class),
            @XmlElement(name = "physicalElementVariable", type = VariableDescription.class),
            @XmlElement(name = "physicalElementAttribute", type = AttributeDescription.class),
            @XmlElement(name = "formattedPhysicalElement", type = FormattedFieldsDescription.class) })
    private AbstractFieldDescription physicalElement;

    @XmlElements({
            @XmlElement(name = "creatingEntity", type = ValueDescription.class),
            @XmlElement(name = "creatingEntityVariable", type = VariableDescription.class),
            @XmlElement(name = "creatingEntityAttribute", type = AttributeDescription.class),
            @XmlElement(name = "formattedCreatingEntity", type = FormattedFieldsDescription.class) })
    private AbstractFieldDescription creatingEntity;

    @XmlElements({
            @XmlElement(name = "source", type = ValueDescription.class),
            @XmlElement(name = "sourceVariable", type = VariableDescription.class),
            @XmlElement(name = "sourceAttribute", type = AttributeDescription.class),
            @XmlElement(name = "formattedSource", type = FormattedFieldsDescription.class) })
    private AbstractFieldDescription source;

    @XmlElements({
            @XmlElement(name = "sectorID", type = ValueDescription.class),
            @XmlElement(name = "sectorIDVariable", type = VariableDescription.class),
            @XmlElement(name = "sectorIDAttribute", type = AttributeDescription.class),
            @XmlElement(name = "formattedSectorID", type = FormattedFieldsDescription.class) })
    private AbstractFieldDescription sectorID;

    @XmlElements({
            @XmlElement(name = "satHeight", type = ValueDescription.class),
            @XmlElement(name = "satHeightVariable", type = VariableDescription.class),
            @XmlElement(name = "satHeightAttribute", type = AttributeDescription.class),
            @XmlElement(name = "formattedSatHeight", type = FormattedFieldsDescription.class) })
    private AbstractFieldDescription satHeight;

    @XmlElements({
            @XmlElement(name = "units", type = ValueDescription.class),
            @XmlElement(name = "unitsVariable", type = VariableDescription.class),
            @XmlElement(name = "unitsAttribute", type = AttributeDescription.class),
            @XmlElement(name = "formattedUnits", type = FormattedFieldsDescription.class) })
    private AbstractFieldDescription units;

    @XmlElement
    private DataTimeDescription dataTime;

    public List<AttributeMatcher> getMatches() {
        return matches;
    }

    public void setMatches(List<AttributeMatcher> matches) {
        this.matches = matches;
    }

    public DataDescription getData() {
        return data;
    }

    public void setData(DataDescription data) {
        this.data = data;
    }

    public AbstractFieldDescription getPhysicalElement() {
        return physicalElement;
    }

    public void setPhysicalElement(AbstractFieldDescription physicalElement) {
        this.physicalElement = physicalElement;
    }

    public AbstractFieldDescription getCreatingEntity() {
        return creatingEntity;
    }

    public void setCreatingEntity(AbstractFieldDescription creatingEntity) {
        this.creatingEntity = creatingEntity;
    }

    public AbstractFieldDescription getSource() {
        return source;
    }

    public void setSource(AbstractFieldDescription source) {
        this.source = source;
    }

    public AbstractFieldDescription getSectorID() {
        return sectorID;
    }

    public void setSectorID(AbstractFieldDescription sectorID) {
        this.sectorID = sectorID;
    }

    public AbstractFieldDescription getUnits() {
        return units;
    }

    public void setUnits(AbstractFieldDescription units) {
        this.units = units;
    }

    public DataTimeDescription getDataTime() {
        return dataTime;
    }

    public void setDataTime(DataTimeDescription dataTime) {
        this.dataTime = dataTime;
    }

    public AbstractFieldDescription getSatHeight() {
        return satHeight;
    }

    public void setSatHeight(AbstractFieldDescription satHeight) {
        this.satHeight = satHeight;
    }

    /**
     * Check if this description contains a {@link DataDescription}. If it does
     * then it should not be used to describe other records, only those that are
     * extracted from its own data description.
     *
     * @return true if this description contains a {@link DataDescription}.
     */
    public boolean hasData() {
        return data != null;
    }

    /**
     * If this description contains a {@link DataDescription} then use the data
     * description to create {@link SatelliteRecord}s with message data set
     * according to the description. This method will also apply any
     * {@link AttributeValue}s in this description.
     *
     * @param cdfFile
     * @param projectionFactory
     * @return
     * @throws GoesrProjectionException
     * @throws ParseException
     * @throws IOException
     */
    public List<SatelliteRecord> getData(NetcdfFile cdfFile,
            GoesrProjectionFactory projectionFactory)
            throws GoesrDecoderException {
        if (data != null) {
            List<SatelliteRecord> records = data.getData(cdfFile,
                    projectionFactory);
            if (records != null) {
                for (SatelliteRecord record : records) {
                    describe(record, cdfFile, true);
                }
                return records;
            }
        }
        return Collections.emptyList();
    }

    /**
     * Apply any {@link AttributeValue}s in this description to the supplied
     * record.
     */
    public void describe(SatelliteRecord record, NetcdfFile cdfFile)
            throws GoesrDecoderException {
        describe(record, cdfFile, false);
    }

    protected void describe(SatelliteRecord record, NetcdfFile cdfFile,
            boolean override) throws GoesrDecoderException {
        if (physicalElement != null
                && (override || record.getPhysicalElement() == null)) {
            record.setPhysicalElement(getString(physicalElement, cdfFile,
                    record));
        }
        if (creatingEntity != null
                && (override || record.getCreatingEntity() == null)) {
            record.setCreatingEntity(getString(creatingEntity, cdfFile, record));
        }
        if (source != null && (override || record.getSource() == null)) {
            record.setSource(getString(source, cdfFile, record));
        }
        if (sectorID != null && (override || record.getSectorID() == null)) {
            record.setSectorID(getString(sectorID, cdfFile, record));
        }
        if (dataTime != null && (override || record.getDataTime() == null)) {
            try {
                record.setDataTime(dataTime.getDataTime(cdfFile));
            } catch (InvalidDescriptionException e) {
                throw new GoesrDecoderException("Unable to get the DataTime.",
                        e);
            }
        }
        if (units != null && (override || record.getUnits() == null)) {
            record.setUnits(getString(units, cdfFile, record));
        }
    }

    private String getString(AbstractFieldDescription field,
            NetcdfFile cdfFile, SatelliteRecord record)
            throws GoesrDecoderException {
        try {
            if (field instanceof FormattedFieldsDescription) {
                FormattedFieldsDescription formattedFields = (FormattedFieldsDescription) field;
                return formattedFields.getString(cdfFile, record);
            }
            String ret = field.getString(cdfFile);
            if (ret == null) {
                ret = (String) GoesrUtils.getAttributeFromRecord(record,
                        field.getName());
            }
            return ret;
        } catch (InvalidDescriptionException e) {
            throw new GoesrDecoderException("Unable to get the field value.", e);
        }
    }

    /**
     * Test all {@link AttributeMatcher}s for this description to see if this
     * description can be applied to a file.
     *
     * @return true if it matches.
     */
    public boolean match(NetcdfFile cdfFile) throws GoesrDecoderException {
        for (AttributeMatcher matcher : matches) {
            if (!matcher.matches(cdfFile)) {
                return false;
            }
        }
        return true;
    }

}
