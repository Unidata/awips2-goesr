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
package com.raytheon.uf.viz.satellite.goesr.legacyprofile;

import javax.measure.Measure;
import javax.measure.quantity.Dimensionless;
import javax.measure.quantity.Temperature;
import javax.measure.unit.NonSI;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;

import com.raytheon.uf.viz.d2d.nsharp.SoundingLayerBuilder;

/**
 * 
 * This class exists as a workaround to the following reported problems:
 * 
 * <pre>
 * RODO #4444 SoundingLayerBuilder.addRelativeHumidity() sets the specific humidity 
 * RODO #4445 SoundingLayerBuilder should derive dewpoint from temperature and relative humidity
 * </pre>
 * 
 * If the {@link SoundingLayerBuilder} is updated to correct these problems then
 * this class can be repalced with SoundingLayerBuilder.
 * 
 * <pre>
 * 
 * SOFTWARE HISTORY
 * 
 * Date          Ticket#  Engineer    Description
 * ------------- -------- ----------- --------------------------
 * Apr 30, 2015  4335     bsteffen    Initial creation
 * 
 * </pre>
 * 
 * @author bsteffen
 * @version 1.0
 */
public class GoesrSoundingLayerBuilder extends SoundingLayerBuilder {

    private Measure<?, Temperature> temperature;

    private Measure<?, Dimensionless> relativeHumidity;

    @Override
    public GoesrSoundingLayerBuilder addTemperature(
            Measure<?, Temperature> temperature) {
        this.temperature = temperature;
        if (this.relativeHumidity != null) {
            calculateDewpoint();
        }
        super.addTemperature(temperature);
        return this;
    }

    @Override
    public GoesrSoundingLayerBuilder addTemperature(double temperature,
            Unit<Temperature> unit) {
        return addTemperature(Measure.valueOf(temperature, unit));
    }

    @Override
    public GoesrSoundingLayerBuilder addRelativeHumidity(
            Measure<?, Dimensionless> relativeHumidity) {
        this.relativeHumidity = relativeHumidity;
        if (this.temperature != null) {
            calculateDewpoint();
        }
        super.addRelativeHumidity(relativeHumidity);
        return this;
    }

    @Override
    public GoesrSoundingLayerBuilder addRelativeHumidity(
            double relativeHumidity,
            Unit<Dimensionless> unit) {
        return addRelativeHumidity(Measure.valueOf(relativeHumidity, unit));

    }

    /**
     * Algorithm to calculate dewpoint from temperature and relative humidity.
     * Algorithm was ported from derived parameter python files. The derived
     * parameters python files was a converted fortran algorithm from the A!
     * meteoLib.
     */
    private void calculateDewpoint() {
        double t = this.temperature.doubleValue(SI.KELVIN);
        double rh = this.relativeHumidity.doubleValue(NonSI.PERCENT);
        double b = 0.0091379024 * t;
        b += 6106.396 / t;
        b -= Math.log(rh / 100);
        double val = b * b;
        val -= 223.1986;
        val = Math.sqrt(val);
        double dewpoint = b - val;
        dewpoint /= 0.0182758048;
        addDewpoint(dewpoint, SI.KELVIN);
    }

}
