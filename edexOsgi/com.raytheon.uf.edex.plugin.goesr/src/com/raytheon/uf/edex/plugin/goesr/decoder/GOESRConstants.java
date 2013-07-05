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

import java.text.SimpleDateFormat;

import com.raytheon.uf.common.time.util.TimeUtil;

/**
 * Expose various constants used within the decoder.
 * 
 * <pre>
 * 
 * SOFTWARE HISTORY
 * 
 * Date         Ticket#    Engineer    Description
 * ------------ ---------- ----------- --------------------------
 * Jun 1, 2012        796  jkorman     Initial creation
 * Jul  5, 2013       2123 mschenke    Gave attributes more descriptive names
 * 
 * </pre>
 * 
 * @author jkorman
 * @version 1.0
 */

public class GOESRConstants {

    /**
     * Format for the reported data/time data - Timezone in UTC.</br>
     * "yyyydddhhmmss" where
     * <table>
     * <tr>
     * <td>yyyy</td>
     * <td>year</td>
     * </tr>
     * <tr>
     * <td>ddd</td>
     * <td>day of the year</td>
     * </tr>
     * <tr>
     * <td>hh</td>
     * <td>hour of the day</td>
     * </tr>
     * <tr>
     * <td>mm</td>
     * <td>minutes of the hour</td>
     * </tr>
     * <tr>
     * <td>ss</td>
     * <td>seconds</td>
     * </tr>
     * </table>
     */
    public static final SimpleDateFormat GOESR_DT = new SimpleDateFormat(
            "yyyyDDDHHmmss");

    /**
     * General date time format - Timezone in UTC.
     */
    public static final SimpleDateFormat GOESR_DATETIME = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");
    static {
        GOESR_DT.setTimeZone(TimeUtil.GMT_TIME_ZONE);
        GOESR_DATETIME.setTimeZone(TimeUtil.GMT_TIME_ZONE);
    }

    /**
     * GOES-R Source name - used to identify the data in the dataURI
     */
    public static final String SOURCE_GOESR = "GOESR";

    /**
     * Name of the satellite component.
     */
    public static final String SAT_PLUGIN_NAME = "satellite";

    public static final int BIT_DEPTH_08 = 8;

    public static final int BIT_DEPTH_09 = 9;

    public static final int BIT_DEPTH_10 = 10;

    public static final int BIT_DEPTH_11 = 11;

    public static final int BIT_DEPTH_12 = 12;

    public static final int BIT_DEPTH_13 = 13;

    public static final int BIT_DEPTH_14 = 14;

    public static final int MESO_SCENE_1 = 1;

    public static final int MESO_SCENE_2 = 2;

    /*
     * GOES-R variable and attribute names.
     */

    /**
     * GOES-R Global conventions metadata attribute.
     */
    public static final String GLOBAL_ATTR_CONVENTIONS_ID = "Conventions";

    /**
     * GOES-R Global ICD version metadata attribute.
     */
    public static final String GLOBAL_ATTR_ICD_VERSION_ID = "ICD_version";

    /**
     * GOES-R Globaltitle metadata attribute.
     */
    public static final String GLOBAL_ATTR_TITLE_ID = "title";

    /**
     * GOES-R Global product name metadata attribute.
     */
    public static final String GLOBAL_ATTR_PRODUCT_NAME_ID = "product_name";

    /**
     * GOES-R Global production location metadata attribute.
     */
    public static final String GLOBAL_ATTR_PRODUCTION_LOCATION_ID = "production_location";

    /**
     * GOES-R Global start date time metadata attribute.
     */
    public static final String GLOBAL_ATTR_START_DATETIME_ID = "start_date_time";

    /**
     * GOES-R Global satellite id metadata attribute.
     */
    public static final String GLOBAL_ATTR_SATELLITE_ID_ID = "satellite_id";

    /**
     * GOES-R Global periodicity metadata attribute.
     */
    public static final String GLOBAL_ATTR_PERIODICITY_ID = "periodicity";

    /**
     * GOES-R Global bit depth metadata attribute.
     */
    public static final String GLOBAL_ATTR_BIT_DEPTH_ID = "bit_depth";

    /**
     * GOES-R Global channel id metadata attribute.
     */
    public static final String GLOBAL_ATTR_CHANNEL_ID_ID = "channel_id";

    /**
     * GOES-R Global central wavelength metadata attribute.
     */
    public static final String GLOBAL_ATTR_CENTRAL_WV_LEN_ID = "central_wavelength";

    /**
     * GOES-R Global source scene metadata attribute.
     */
    public static final String GLOBAL_ATTR_SOURCE_SCENE_ID = "source_scene";

    /**
     * GOES-R Global source spatial resolution metadata attribute.
     */
    public static final String GLOBAL_ATTR_SRC_SPATIAL_RES_ID = "source_spatial_resolution";

    /**
     * GOES-R Global request spatial resolution metadata attribute.
     */
    public static final String GLOBAL_ATTR_REQ_SPATIAL_RES_ID = "request_spatial_resolution";

    /**
     * GOES-R Global satellite latitude metadata attribute.
     */
    public static final String GLOBAL_ATTR_SATELLITE_LATITUDE_ID = "satellite_latitude";

    /**
     * GOES-R Global satellite longitude metadata attribute.
     */
    public static final String GLOBAL_ATTR_SATELLITE_LONGITUDE_ID = "satellite_longitude";

    /**
     * GOES-R Global satellite altitude metadata attribute.
     */
    public static final String GLOBAL_ATTR_SATELLITE_ALTITUDE_ID = "satellite_altitude";

    /**
     * GOES-R Global projection metadata attribute.
     */
    public static final String GLOBAL_ATTR_PROJECTION_ID = "projection";

    /**
     * GOES-R Global product center latitude metadata attribute.
     */
    public static final String GLOBAL_ATTR_PRODUCT_CENTER_LAT_ID = "product_center_latitude";

    /**
     * GOES-R Global product center longitude metadata attribute.
     */
    public static final String GLOBAL_ATTR_PRODUCT_CENTER_LON_ID = "product_center_longitude";

    /**
     * GOES-R Global pixel x size metadata attribute.
     */
    public static final String GLOBAL_ATTR_PIXEL_X_SIZE_ID = "pixel_x_size";

    /**
     * GOES-R Global pixel y size metadata attribute.
     */
    public static final String GLOBAL_ATTR_PIXEL_Y_SIZE_ID = "pixel_y_size";

    /**
     * GOES-R Global number of product rows metadata attribute.
     */
    public static final String GLOBAL_ATTR_PRODUCT_ROWS_ID = "product_rows";

    /**
     * GOES-R Global number of product columns metadata attribute.
     */
    public static final String GLOBAL_ATTR_PRODUCT_COLS_ID = "product_columns";

    /**
     * GOES-R Global product tile width metadata attribute.
     */
    public static final String GLOBAL_ATTR_PRODUCT_TILE_WIDTH_ID = "product_tile_width";

    /**
     * GOES-R Global product tile height metadata attribute.
     */
    public static final String GLOBAL_ATTR_PRODUCT_TILE_HEIGHT_ID = "product_tile_height";

    /**
     * GOES-R Global number of product tiles metadata attribute.
     */
    public static final String GLOBAL_ATTR_NUM_PRODUCT_TILES_ID = "number_product_tiles";

    /**
     * GOES-R Global tile row offset metadata attribute.
     */
    public static final String GLOBAL_ATTR_TILE_ROW_OFFSET_ID = "tile_row_offset";

    /**
     * GOES-R Global tile column offset metadata attribute.
     */
    public static final String GLOBAL_ATTR_TILE_COL_OFFSET_ID = "tile_column_offset";

    /**
     * GOES-R Global tile center latitude metadata attribute.
     */
    public static final String GLOBAL_ATTR_TILE_CENTER_LAT_ID = "tile_center_latitude";

    /**
     * GOES-R Global tile center longitude metadata attribute.
     */
    public static final String GLOBAL_ATTR_TILE_CENTER_LON_ID = "tile_center_longitude";

    /**
     * GOES-R image sectorized grid data variable name.
     */
    public static final String VARIABLE_CMI_DATA_ID = "CMI";

    /**
     * GOES-R image sectorized grid data variable name.
     */
    public static final String VARIABLE_SECTORIZED_CMI_DATA_ID = "Sectorized_CMI";

    /**
     * GOES-R image grid data, data name metadata attribute.
     */
    public static final String CMI_ATTR_STANDARD_NAME_ID = "standard_name";

    /**
     * GOES-R image grid data, data units metadata attribute.
     */
    public static final String CMI_ATTR_UNITS_ID = "units";

    /**
     * GOES-R image grid data, grid mapping metadata attribute.
     */
    public static final String CMI_ATTR_GRID_MAPPING_ID = "grid_mapping";

    /**
     * GOES-R image grid data, grid mapping metadata attribute.
     */
    public static final String CMI_ATTR_ADD_OFFSET_ID = "add_offset";

    /**
     * GOES-R image grid data, grid mapping metadata attribute.
     */
    public static final String CMI_ATTR_SCALE_FACTOR_ID = "scale_factor";

    /**
     * GOES-R image grid data, fill value metadata attribute.
     */
    public static final String CMI_ATTR_FILL_VALUE_ID = "_FillValue";

    /**
     * GOES-R image grid data, minimum valid value metadata attribute.
     */
    public static final String CMI_ATTR_VALID_MIN_ID = "valid_min";

    /**
     * GOES-R image grid data, maximum valid value metadata attribute.
     */
    public static final String CMI_ATTR_VALID_MAX_ID = "valid_max";

    /**
     * GOES-R lambert conformal projection variable name.
     */
    public static final String PROJECTION_LAMBERT_ID = "lambert_projection";

    /**
     * GOES-R mercator projection variable name.
     */
    public static final String PROJECTION_MERCATOR_ID = "mercator_projection";

    /**
     * GOES-R polar projection variable name.
     */
    public static final String PROJECTION_POLAR_ID = "polar_projection";

    /**
     * GOES-R fixed grid projection variable name.
     */
    public static final String PROJECTION_FIXEDGRID_ID = "fixedgrid_projection";

    /**
     * GOES-R projection variable, grid mapping name metadata attribute.
     */
    public static final String PROJ_ATTR_GRID_MAPPING_NAME_ID = "grid_mapping_name";

    /**
     * GOES-R projection variable, false easting metadata attribute.
     */
    public static final String PROJ_ATTR_FALSE_EASTING_ID = "false_easting";

    /**
     * GOES-R projection variable, false northing metadata attribute.
     */
    public static final String PROJ_ATTR_FALSE_NORTHING_ID = "false_northing";

    /**
     * GOES-R projection variable, semi minor axis metadata attribute.
     */
    public static final String PROJ_ATTR_SEMI_MINOR_ID = "semi_minor";

    /**
     * GOES-R projection variable, semi major axis metadata attribute.
     */
    public static final String PROJ_ATTR_SEMI_MAJOR_ID = "semi_major";

    /**
     * GOES-R projection variable, standard parallel metadata attribute.
     */
    public static final String PROJ_ATTR_STANDARD_PARALLEL_ID = "standard_parallel";

    /**
     * GOES-R projection variable, longitude of projection origin metadata
     * attribute.
     */
    public static final String PROJ_ATTR_CENTRAL_MERIDIAN_ID = "longitude_of_central_meridian";

    /**
     * GOES-R projection variable, latitude of projection origin metadata
     * attribute.
     */
    public static final String PROJ_ATTR_ORIGIN_LAT_ID = "latitude_of_projection_origin";

    /**
     * GOES-R projection variable, longitude of projection origin metadata
     * attribute.
     */
    public static final String PROJ_ATTR_ORIGIN_LON_ID = "longitude_of_projection_origin";

    /**
     * GOES-R polar projection variable, straight longitude line from pole
     */
    public static final String PROJ_POLAR_ATTR_LON_POLE_ID = "straight_vertical_longitude_from_pole";

    /**
     * GOES-R fixed grid projection variable, perspective point height metadata
     * attribute.
     */
    public static final String PROJ_FIXEDGRID_ATTR_PERSPECTIVE_POINT_HEIGHT_ID = "perspective_point_height";

    /**
     * GOES-R fixed grid projection variable, sweep angle axis metadata
     * attribute.
     */
    public static final String PROJ_FIXEDGRID_ATTR_SWEEP_ANGLE_AXIS_ID = "sweep_angle_axis";

    /**
     * Hide the default constructor
     */
    private GOESRConstants() {
    }
}
