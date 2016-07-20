package com.raytheon.uf.edex.plugin.goesr.dmw.description;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

import com.raytheon.uf.edex.netcdf.description.AbstractFieldDescription;
import com.raytheon.uf.edex.netcdf.description.AttributeDescription;
import com.raytheon.uf.edex.netcdf.description.ValueDescription;
import com.raytheon.uf.edex.netcdf.description.VariableDescription;

import com.raytheon.uf.edex.netcdf.description.date.DataTimeDescription;
import com.raytheon.uf.edex.netcdf.description.date.EpochOffsetDateValue;

/**
 * A single Derived Motion Winds (DMW) Product Description. Used by the DMWDecoder
 * to decode fields from a single NetCDF file into multiple DMWRecords.
 *
 * <pre>
 *
 * SOFTWARE HISTORY
 *
 * Date         Ticket#    Engineer    Description
 * ------------ ---------- ----------- --------------------------
 * 07/13/2016   19051   mcomerford   Initial creation
 * </pre>
 *
 * @author matt.comerford
 * @version 1.0
 */

@XmlAccessorType(XmlAccessType.NONE)
public class ProductDescription {

    @XmlAttribute(required = true)
    private String name;

    @XmlElement
    private VariableDescription lat;

    @XmlElement
    private VariableDescription lon;

    @XmlElement
    private VariableDescription wspd;

    @XmlElement
    private VariableDescription wdir;

    @XmlElement
    private VariableDescription dqf;

    @XmlElement
    private VariableDescription filter = null;

    @XmlElement
    private VariableDescription channel;

    @XmlElement
    private EpochOffsetDateValue dataTime;


    @XmlElements({
        @XmlElement(name = "orbitalSlotAttribute", type = AttributeDescription.class),
        @XmlElement(name = "orbitalSlotValue", type = ValueDescription.class)
    })
    private AbstractFieldDescription orbitalSlot = null;

    @XmlElements({
        @XmlElement(name = "percentGoodDQFVariable", type = VariableDescription.class),
        @XmlElement(name = "percentGoodDQFAttribute", type = AttributeDescription.class)
    })
    private AbstractFieldDescription percentGoodDQF = null;

    @XmlElements({
        @XmlElement(name = "sceneValue", type = ValueDescription.class),
        @XmlElement(name = "sceneAttribute", type = AttributeDescription.class)
    })
    private AbstractFieldDescription scene;
    
    @XmlElement
    private int validDQF;

    @XmlElement(name = "debug")
    private boolean debugStatus = false;
    
    /** Getters & Setters */
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public VariableDescription getLat() {
        return lat;
    }
    public void setLat(VariableDescription lat) {
        this.lat = lat;
    }

    public VariableDescription getLon() {
        return lon;
    }
    public void setLon(VariableDescription lon) {
        this.lon = lon;
    }

    public VariableDescription getWspd() {
        return wspd;
    }
    public void setWspd(VariableDescription wspd) {
        this.wspd = wspd;
    }

    public VariableDescription getWdir() {
        return wdir;
    }
    public void setWdir(VariableDescription wdir) {
        this.wdir = wdir;
    }

    public VariableDescription getDQF() {
        return dqf;
    }
    public void setDQF(VariableDescription dqf) {
        this.dqf = dqf;
    }

    public VariableDescription getFilter() {
        return filter;
    }
    public void setFilter(VariableDescription filter) {
        this.filter = filter;
    }

    public VariableDescription getChannel() {
        return channel;
    }
    public void setChannel(VariableDescription channel) {
        this.channel = channel;
    }

    public EpochOffsetDateValue getDataTime() {
        return dataTime;
    }
    public void setDataTime(EpochOffsetDateValue dataTime) {
        this.dataTime = dataTime;
    }

    public AbstractFieldDescription getOrbitalSlot() {
        return orbitalSlot;
    }
    public void setOrbitalSlot(AbstractFieldDescription orbitalSlot) {
        this.orbitalSlot = orbitalSlot;
    }

    public AbstractFieldDescription getPercentGoodDQF() {
        return percentGoodDQF;
    }
    public void setPercentGoodDQF(AbstractFieldDescription percentGoodDQF) {
        this.percentGoodDQF = percentGoodDQF;
    }

    public AbstractFieldDescription getScene() {
        return scene;
    }
    public void setScene(AbstractFieldDescription scene) {
        this.scene = scene;
    }

    public int getValidDQF() {
        return validDQF;
    }
    public void setValidDQF(int validDQF) {
        this.validDQF = validDQF;
    }

    public boolean getDebugStatus() {
        return debugStatus;
    }
    public void setDebugStatus(boolean debugStatus) {
        this.debugStatus = debugStatus;
    }

}
