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
import java.util.TimeZone;

/**
 * Expose various constants used within the decoder.
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

public class GOESRConstants {

    /**
     * UTC timezone constant.
     */
    public static final TimeZone UTC = TimeZone.getTimeZone("UTC");

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
        GOESR_DT.setTimeZone(UTC);
        GOESR_DATETIME.setTimeZone(UTC);
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

    /**
     * Number of bytes to pre-read while attempting to discover the HDF
     * signature.
     */
    public static final int PRE_READ_SIZE = 1024;

    private static char[] C_HDF_SIG = new char[] { (char) 0x89, 'H', 'D', 'F',
            (char) 0x0D, (char) 0x0A, (char) 0x1A, (char) 0x0A };

    /**
     * The HDF signature.
     */
    public static final String HDF_SIG = new String(C_HDF_SIG);

    /*
     * GOES-R variable and attribute names.
     */

    /**
     * GOES-R Global conventions metadata attribute.
     */
    public static final String CONVENTIONS = "Conventions";

    /**
     * GOES-R Global ICD version metadata attribute.
     */
    public static final String ICD_VERSION = "ICD_version";

    /**
     * GOES-R Globaltitle metadata attribute.
     */
    public static final String TITLE = "title";

    /**
     * GOES-R Global product name metadata attribute.
     */
    public static final String PRODUCT_NAME = "production_name";

    /**
     * GOES-R Global production location metadata attribute.
     */
    public static final String PRODUCTION_LOCATION = "production_location";

    /**
     * GOES-R Global start date time metadata attribute.
     */
    public static final String START_DATETIME = "start_date_time";

    /**
     * GOES-R Global satellite id metadata attribute.
     */
    public static final String SATELLITE_ID = "satellite_id";

    /**
     * GOES-R Global satellite mode metadata attribute.
     */
    public static final String SATELLITE_MODE = "satellite_mode";

    /**
     * GOES-R Global periodicity metadata attribute.
     */
    public static final String PERIODICITY = "periodicity";

    /**
     * GOES-R Global bit depth metadata attribute.
     */
    public static final String BIT_DEPTH = "bit_depth";

    /**
     * GOES-R Global channel id metadata attribute.
     */
    public static final String CHANNEL_ID = "channel_id";

    /**
     * GOES-R Global central wavelength metadata attribute.
     */
    public static final String CENTRAL_WV_LEN = "central_wavelength";

    /**
     * GOES-R Global source scene metadata attribute.
     */
    public static final String SOURCE_SCENE = "source_scene";

    /**
     * GOES-R Global source spatial resolution metadata attribute.
     */
    public static final String SRC_SPATIAL_RES = "source_spatial_resolution";

    /**
     * GOES-R Global request spatial resolution metadata attribute.
     */
    public static final String REQ_SPATIAL_RES = "request_spatial_resolution";

    /**
     * GOES-R Global satellite latitude metadata attribute.
     */
    public static final String SATELLITE_LATITUDE = "satellite_latitude";

    /**
     * GOES-R Global satellite longitude metadata attribute.
     */
    public static final String SATELLITE_LONGITUDE = "satellite_longitude";

    /**
     * GOES-R Global satellite altitude metadata attribute.
     */
    public static final String SATELLITE_ALTITUDE = "satellite_altitude";

    /**
     * GOES-R Global projection metadata attribute.
     */
    public static final String PROJECTION = "projection";

    /**
     * GOES-R Global product center latitude metadata attribute.
     */
    public static final String PRODUCT_CENTER_LAT = "product_center_latitude";

    /**
     * GOES-R Global product center longitude metadata attribute.
     */
    public static final String PRODUCT_CENTER_LON = "product_center_longitude";

    /**
     * GOES-R Global pixel x size metadata attribute.
     */
    public static final String PIXEL_X_SIZE = "pixel_x_size";

    /**
     * GOES-R Global pixel y size metadata attribute.
     */
    public static final String PIXEL_Y_SIZE = "pixel_y_size";

    /**
     * GOES-R Global number of product rows metadata attribute.
     */
    public static final String PRODUCT_ROWS = "product_rows";

    /**
     * GOES-R Global number of product columns metadata attribute.
     */
    public static final String PRODUCT_COLS = "product_columns";

    /**
     * GOES-R Global product tile width metadata attribute.
     */
    public static final String PRODUCT_TILE_WIDTH = "product_tile_width";

    /**
     * GOES-R Global product tile height metadata attribute.
     */
    public static final String PRODUCT_TILE_HEIGHT = "product_tile_height";

    /**
     * GOES-R Global product tile size metadata attribute.
     */
    public static final String PRODUCT_TILE_SIZE = "product_tile_size";

    /**
     * GOES-R Global number of product tiles metadata attribute.
     */
    public static final String NUM_PRODUCT_TILES = "number_product_tiles";

    /**
     * GOES-R Global tile row offset metadata attribute.
     */
    public static final String TILE_ROW_OFFSET = "tile_row_offset";

    /**
     * GOES-R Global tile column offset metadata attribute.
     */
    public static final String TILE_COL_OFFSET = "tile_column_offset";

    /**
     * GOES-R Global tile center latitude metadata attribute.
     */
    public static final String TILE_CENTER_LAT = "tile_center_latitude";

    /**
     * GOES-R Global tile center longitude metadata attribute.
     */
    public static final String TILE_CENTER_LON = "tile_center_longitude";

    /**
     * GOES-R image grid data variable name.
     */
    public static final String GRIDDED_DATA = "sectorized_cmi";

    /**
     * GOES-R image grid data, data name metadata attribute.
     */
    public static final String GD_STANDARD_NAME = "standard_name";

    /**
     * GOES-R image grid data, data units metadata attribute.
     */
    public static final String GD_UNITS = "units";

    /**
     * GOES-R image grid data, grid mapping metadata attribute.
     */
    public static final String GD_GRID_MAPPING = "grid_mapping";

    /**
     * GOES-R image grid data, grid mapping metadata attribute.
     */
    public static final String GD_ADD_OFFSET = "add_offset";

    /**
     * GOES-R image grid data, grid mapping metadata attribute.
     */
    public static final String GD_SCALE_FACTOR = "scale_factor";

    /**
     * GOES-R image grid data, fill value metadata attribute.
     */
    public static final String GD_FILL_VALUE = "_FillValue";

    /**
     * GOES-R image grid data, minimum valid value metadata attribute.
     */
    public static final String GD_VALID_MIN = "valid_min";

    /**
     * GOES-R image grid data, maximum valid value metadata attribute.
     */
    public static final String GD_VALID_MAX = "valid_max";

    /**
     * GOES-R lambert conformal projection variable name.
     */
    public static final String PROJ_LAMBERT = "lambert_projection";

    /**
     * GOES-R mercator projection variable name.
     */
    public static final String PROJ_MERCATOR = "mercator_projection";

    /**
     * GOES-R polar projection variable name.
     */
    public static final String PROJ_POLAR = "polar_projection";

    /**
     * GOES-R fixed grid projection variable name.
     */
    public static final String PROJ_FIXEDGRID = "fixedgrid_projection";

    /**
     * GOES-R common projection variable, grid mapping name metadata attribute.
     */
    public static final String PROJ_GRID_MAPPING_NAME = "grid_mapping_name";

    /**
     * GOES-R common projection variable, false easting metadata attribute.
     */
    public static final String PROJ_FALSE_EASTING = "false_easting";

    /**
     * GOES-R common projection variable, false northing metadata attribute.
     */
    public static final String PROJ_FALSE_NORTHING = "false_northing";

    /**
     * GOES-R common projection variable, semi minor axis metadata attribute.
     */
    public static final String PROJ_SEMI_MINOR = "semi_major";

    /**
     * GOES-R common projection variable, semi major axis metadata attribute.
     */
    public static final String PROJ_SEMI_MAJOR = "semi_minor";

    /**
     * GOES-R lambert conformal variable, standard parallel metadata attribute.
     */
    public static final String LAMBERT_STANDARD_PARALLEL = "standard_parallel";

    /**
     * GOES-R lambert conformal variable, longitude of projection origin
     * metadata attribute.
     */
    public static final String LAMBERT_CENTRAL_MERIDIAN = "longitude_of_central_meridian";

    /**
     * GOES-R lambert conformal variable, latitude of projection origin metadata
     * attribute.
     */
    public static final String LAMBERT_PROJ_ORIGIN_LAT = "latitude_of_projection_origin";

    /**
     * Hide the default constructor
     */
    private GOESRConstants() {
    }
}
