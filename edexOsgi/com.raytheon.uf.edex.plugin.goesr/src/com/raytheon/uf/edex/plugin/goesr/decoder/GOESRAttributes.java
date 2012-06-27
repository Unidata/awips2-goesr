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

import java.util.Date;

import ucar.nc2.Attribute;
import ucar.nc2.NetcdfFile;

import static com.raytheon.uf.edex.plugin.goesr.decoder.GOESRUtil.getAttributeDate;
import static com.raytheon.uf.edex.plugin.goesr.decoder.GOESRUtil.getAttributeDouble;
import static com.raytheon.uf.edex.plugin.goesr.decoder.GOESRUtil.getAttributeInteger;
import static com.raytheon.uf.edex.plugin.goesr.decoder.GOESRUtil.getAttributeString;

/**
 * Extract and make available the GOES-R global attributes from a given
 * NetcdfFile instance.
 * 
 * <pre>
 * 
 *  **** IMPORTANT ****
 *  The attribute values of TILE_ROW_OFFSET and TILE_COL_OFFSET are
 *  transposed in the data. The populateAttributes() method switches
 *  these values so that they match. This problem has been reported.
 *  When this problem is corrected, the code must be modified to
 *  set the values correctly.
 * *************
 *  The satelliteAltitude should be in meters (ICD) but is currently
 *  being reported in kilometers.
 * *************
 *   
 *  SOFTWARE HISTORY
 *  
 *  Date         Ticket#    Engineer    Description
 *  ------------ ---------- ----------- --------------------------
 *  Jun 19, 2012        796 jkorman     Initial creation
 * 
 * </pre>
 * 
 * @author jkorman
 * @version 1.0
 */

public class GOESRAttributes {

    private String conventions;

    private String ICD_version;

    private Integer bit_depth;

    private Float central_wavelength;

    private Integer channel_id;

    private Integer number_product_tiles;

    private Integer periodicity;

    private Float pixel_x_size;

    private Float pixel_y_size;

    private Float product_center_latitude;

    private Float product_center_longitude;

    private Integer product_columns;

    private Integer product_rows;

    private Integer product_tile_height;

    private Integer product_tile_width;

    private Integer product_tile_size;

    private String production_location;

    private String product_name;

    private String projection;

    private Float request_spatial_resolution;

    private Float satellite_altitude;

    private String satellite_id;

    private Float satellite_latitude;

    private Float satellite_longitude;

    private Integer satellite_mode;

    private String source_scene;

    private Float source_spatial_resolution;

    private Date start_date_time;

    private Float tile_center_latitude;

    private Float tile_center_longitude;

    private Integer tile_column_offset;

    private Integer tile_row_offset;

    private String title;

    /**
     * Extract all of the global attributes associated with the given netCDF
     * instance.
     * 
     * @param cdfFile
     *            A netCDF instance containing global attributes.
     */
    public GOESRAttributes(NetcdfFile cdfFile) {
        populateAttributes(cdfFile);
    }

    /**
     * Get the netCDF metadata conventions version used in this data.
     * 
     * @return The conventions version.
     */
    public String getConventions() {
        return conventions;
    }

    /**
     * Get the title of the ICD used to create this data.
     * 
     * @return The ICD version.
     */
    public String getICD_version() {
        return ICD_version;
    }

    /**
     * Get the number of bits (bit depth) used to encode the image grid data.
     * 
     * @return The number of bits (bit depth) used to encode the image grid
     *         data.
     */
    public Integer getBit_depth() {
        return bit_depth;
    }

    /**
     * Get the central wavelength of the image grid data.
     * 
     * @return The central wavelength of the image grid data.
     */
    public Float getCentral_wavelength() {
        return central_wavelength;
    }

    /**
     * Get the satellite channel number.
     * 
     * @return The satellite channel number.
     */
    public Integer getChannel_id() {
        return channel_id;
    }

    /**
     * Get the number of tiles in the image grid data.
     * 
     * @return The number of product tiles.
     */
    public Integer getNumber_product_tiles() {
        return number_product_tiles;
    }

    /**
     * Get the periodicity (delivery rate) of the product.
     * 
     * @return The periodicity.
     */
    public Integer getPeriodicity() {
        return periodicity;
    }

    /**
     * Get the x axis pixel size in kilometers.
     * 
     * @return The pixel x size.
     */
    public Float getPixel_x_size() {
        return pixel_x_size;
    }

    /**
     * Get the y axis pixel size in kilometers.
     * 
     * @return The pixel y size.
     */
    public Float getPixel_y_size() {
        return pixel_y_size;
    }

    /**
     * Get the center latitude of the image grid data.
     * 
     * @return The product center latitude.
     */
    public Float getProduct_center_latitude() {
        return product_center_latitude;
    }

    /**
     * Get the center longitude of the image grid data.
     * 
     * @return The product center longitude.
     */
    public Float getProduct_center_longitude() {
        return product_center_longitude;
    }

    /**
     * Get the total number of columns (x axis) in the image grid data.
     * 
     * @return The total product columns.
     */
    public Integer getProduct_columns() {
        return product_columns;
    }

    /**
     * Get the total number of rows (y axis) in the image grid data.
     * 
     * @return The total product rows.
     */
    public Integer getProduct_rows() {
        return product_rows;
    }

    /**
     * Get the height (y axis) of the tile in pixels.
     * 
     * @return The product tile height.
     */
    public Integer getProduct_tile_height() {
        return product_tile_height;
    }

    /**
     * Get the width (x axis) of the tile in pixels.
     * 
     * @return The product tile width.
     */
    public Integer getProduct_tile_width() {
        return product_tile_width;
    }

    /**
     * Get the location where the data were produced.
     * 
     * @return The production location.
     */
    public String getProduction_location() {
        return production_location;
    }

    /**
     * Get the product name. This name (referred to as short name) is an extract
     * of the original data file name.
     * 
     * @return The product name.
     */
    public String getProduct_name() {
        return product_name;
    }

    /**
     * Get the map projection of the data. Current values include
     * <ul>
     * <li>Mercator</li>
     * <li>Lambert Conformal</li>
     * <li>Polar Sterographic</li>
     * <li>Fixed_Grid</li>
     * </ul>
     * Note misspelling of Sterographic. This has been reported.
     * 
     * @return The map projection.
     */
    public String getProjection() {
        return projection;
    }

    /**
     * The spatial resolution (kilometers) used to create the image grid data.
     * 
     * @return The request spatial resolution in kilometers.
     */
    public Float getRequest_spatial_resolution() {
        return request_spatial_resolution;
    }

    /**
     * Get the altitude of the satellite in meters. NOTE that this value is
     * currently being reported in kilometers.
     * 
     * @return The satellite altitude in kilometers.
     */
    public Float getSatellite_altitude() {
        return satellite_altitude;
    }

    /**
     * Get the satellite identifier. Currently GOES-16 or GOES-17.
     * 
     * @return The satellite id.
     */
    public String getSatellite_id() {
        return satellite_id;
    }

    /**
     * Get the latitude of the satellite over the earth.
     * 
     * @return The satellite latitude.
     */
    public Float getSatellite_latitude() {
        return satellite_latitude;
    }

    /**
     * Get the longitude of the satellite over the earth.
     * 
     * @return The satellite longitude.
     */
    public Float getSatellite_longitude() {
        return satellite_longitude;
    }

    /**
     * Get the satellite mode.
     * 
     * @return The satellite mode.
     */
    public Integer getSatellite_mode() {
        return satellite_mode;
    }

    /**
     * Get the source scene that this data was created from.
     * 
     * @return The source scene.
     */
    public String getSource_scene() {
        return source_scene;
    }

    /**
     * Get the source spatial resolution in kilometers. This is the collected
     * size at the nadir point of the satellite.
     * 
     * @return The source spatial resolution in kilometers.
     */
    public Float getSource_spatial_resolution() {
        return source_spatial_resolution;
    }

    /**
     * Get the start time of the tile. Note that all tiles making up an image
     * grid will have the same start time. The time is valid to the nearest
     * second.
     * 
     * @return The start date time.
     */
    public Date getStart_date_time() {
        return start_date_time;
    }

    /**
     * Get the latitude of the center of the tile.
     * 
     * @return The tile center latitude.
     */
    public Float getTile_center_latitude() {
        return tile_center_latitude;
    }

    /**
     * Get the longitude of the center of the tile.
     * 
     * @return The tile center longitude.
     */
    public Float getTile_center_longitude() {
        return tile_center_longitude;
    }

    /**
     * Get the column offset of the (0,0) pixel relative to the total image
     * grid.
     * 
     * @return The tile column offset.
     */
    public Integer getTile_column_offset() {
        return tile_column_offset;
    }

    /**
     * Get the row offset of the (0,0) pixel relative to the total image grid.
     * 
     * @return The tile row offset.
     */
    public Integer getTile_row_offset() {
        return tile_row_offset;
    }

    /**
     * Get the descriptive title of this data file.
     * 
     * @return The descriptive title of this data file.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Extract all
     * 
     * @param cdfFile
     */
    private void populateAttributes(NetcdfFile cdfFile) {
        String nullString = null;

        Attribute attr = cdfFile
                .findGlobalAttribute(GOESRConstants.CONVENTIONS);
        conventions = getAttributeString(attr, nullString);
        // string(CONVENTIONS)
        attr = cdfFile.findGlobalAttribute(GOESRConstants.ICD_VERSION);
        ICD_version = getAttributeString(attr, nullString);
        // string(ICD_VERSION)
        attr = cdfFile.findGlobalAttribute(GOESRConstants.TITLE);
        title = getAttributeString(attr, nullString);
        // string(TITLE)
        attr = cdfFile.findGlobalAttribute(GOESRConstants.PRODUCT_NAME);
        product_name = getAttributeString(attr, nullString);
        // string(PRODUCT_NAME)
        attr = cdfFile.findGlobalAttribute(GOESRConstants.PRODUCTION_LOCATION);
        production_location = getAttributeString(attr, nullString);
        // string(PRODUCTION_LOCATION, WCDAS, RBU)
        attr = cdfFile.findGlobalAttribute(GOESRConstants.START_DATETIME);
        start_date_time = getAttributeDate(attr, new Date(0));
        // integer(START_DATETIME)

        attr = cdfFile.findGlobalAttribute(GOESRConstants.SATELLITE_ID);
        satellite_id = getAttributeString(attr, "");
        // string(SATELLITE_ID. GOES-16, GOES-17)

        attr = cdfFile.findGlobalAttribute(GOESRConstants.SATELLITE_MODE);
        satellite_mode = getAttributeInteger(attr, -1);
        // integer(SATELLITE_MODE, 3, 4)

        attr = cdfFile.findGlobalAttribute(GOESRConstants.PERIODICITY);
        periodicity = getAttributeInteger(attr, -1);
        // integer(PERIODICITY)

        attr = cdfFile.findGlobalAttribute(GOESRConstants.BIT_DEPTH);
        bit_depth = getAttributeInteger(attr, -1);
        // integer(BIT_DEPTH, 8..14)
        attr = cdfFile.findGlobalAttribute(GOESRConstants.CHANNEL_ID);
        channel_id = getAttributeInteger(attr, -1);
        // integer(CHANNEL_ID, 1..16)

        attr = cdfFile.findGlobalAttribute(GOESRConstants.CENTRAL_WV_LEN);
        central_wavelength = (float) getAttributeDouble(attr, -1);
        // double(CENTRAL_WV_LEN,,,,,)

        attr = cdfFile.findGlobalAttribute(GOESRConstants.SOURCE_SCENE);
        source_scene = getAttributeString(attr, nullString);
        // String(SOURCE_SCENE, Full Disk, CONUS, Mesoscale-1, Mesoscale-2)
        attr = cdfFile.findGlobalAttribute(GOESRConstants.SRC_SPATIAL_RES);
        source_spatial_resolution = (float) getAttributeDouble(attr, -1);
        // double(SRC_SPATIAL_RES, 0.5, 1.0, 2.0)
        attr = cdfFile.findGlobalAttribute(GOESRConstants.REQ_SPATIAL_RES);
        request_spatial_resolution = (float) getAttributeDouble(attr, -1);
        // double(REQ_SPATIAL_RES, 0.5..28)

        attr = cdfFile.findGlobalAttribute(GOESRConstants.SATELLITE_LATITUDE);
        satellite_latitude = (float) getAttributeDouble(attr, -1);
        // double(SATELLITE_LATITUDE, 0)
        attr = cdfFile.findGlobalAttribute(GOESRConstants.SATELLITE_LONGITUDE);
        satellite_longitude = (float) getAttributeDouble(attr, -1);
        // double(SATELLITE_LONGITUDE, -180..180)
        attr = cdfFile.findGlobalAttribute(GOESRConstants.SATELLITE_ALTITUDE);
        // TODO : Adjust to meters per ICD.
        satellite_altitude = (float) getAttributeDouble(attr, -1) * 1000;
        // double(SATELLITE_ALTITUDE, 35786023)

        attr = cdfFile.findGlobalAttribute(GOESRConstants.PROJECTION);
        projection = getAttributeString(attr, nullString);
        // string(PROJECTION, Mercator, Lambert Conformal, Polar Stereographic,
        // Fixed_Grid)

        attr = cdfFile.findGlobalAttribute(GOESRConstants.PRODUCT_CENTER_LAT);
        product_center_latitude = (float) getAttributeDouble(attr, -1);
        // double(PRODUCT_CENTER_LAT, -90..90)
        attr = cdfFile.findGlobalAttribute(GOESRConstants.PRODUCT_CENTER_LON);
        product_center_longitude = (float) getAttributeDouble(attr, -1);
        // double(PRODUCT_CENTER_LON, -180..180)

        attr = cdfFile.findGlobalAttribute(GOESRConstants.PIXEL_X_SIZE);
        pixel_x_size = (float) getAttributeDouble(attr, -1);
        // double(PIXEL_X_SIZE, 0.5..28)

        attr = cdfFile.findGlobalAttribute(GOESRConstants.PIXEL_Y_SIZE);
        pixel_y_size = (float) getAttributeDouble(attr, -1);
        // double(PIXEL_Y_SIZE, 0.5..28)

        attr = cdfFile.findGlobalAttribute(GOESRConstants.PRODUCT_ROWS);
        product_rows = getAttributeInteger(attr, -1);
        // integer(PRODUCT_ROWS, 1..32000)

        attr = cdfFile.findGlobalAttribute(GOESRConstants.PRODUCT_COLS);
        product_columns = getAttributeInteger(attr, -1);
        // integer(PRODUCT_COLS, 1..32000)

        attr = cdfFile.findGlobalAttribute(GOESRConstants.PRODUCT_TILE_WIDTH);
        product_tile_width = getAttributeInteger(attr, -1);
        // integer(PRODUCT_TILE_WIDTH, 256..2048)
        attr = cdfFile.findGlobalAttribute(GOESRConstants.PRODUCT_TILE_HEIGHT);
        product_tile_height = getAttributeInteger(attr, -1);
        // integer(PRODUCT_TILE_HEIGHT, 256..2048)

        attr = cdfFile.findGlobalAttribute(GOESRConstants.PRODUCT_TILE_SIZE);
        product_tile_size = getAttributeInteger(attr, -1);
        // integer(PRODUCT_TILE_SIZE, 256..2048)

        attr = cdfFile.findGlobalAttribute(GOESRConstants.NUM_PRODUCT_TILES);
        number_product_tiles = getAttributeInteger(attr, -1);
        // integer(NUM_PRODUCT_TILES, 0..999)

        // ********************************
        // * Note that the following two attribute values are transposed in
        // * the data files. These need to be changed back once corrected.
        // ********************************
        attr = cdfFile.findGlobalAttribute(GOESRConstants.TILE_ROW_OFFSET);
        tile_column_offset = getAttributeInteger(attr, -1);
        // integer(TILE_ROW_OFFSET, 0..(product_rows - 1))
        attr = cdfFile.findGlobalAttribute(GOESRConstants.TILE_COL_OFFSET);
        tile_row_offset = getAttributeInteger(attr, -1);
        // integer(TILE_COL_OFFSET, 0..(product_columns - 1))
        // *
        // ********************************

        attr = cdfFile.findGlobalAttribute(GOESRConstants.TILE_CENTER_LAT);
        tile_center_latitude = (float) getAttributeDouble(attr, -1);
        // double(TILE_CENTER_LAT,-90..90)
        attr = cdfFile.findGlobalAttribute(GOESRConstants.TILE_CENTER_LON);
        tile_center_longitude = (float) getAttributeDouble(attr, -1);
        // double(TILE_CENTER_LON, -180..180)
    }

    /**
     * Create a String representation of the GOES-R global attributes.
     * 
     * @return A String representation of the GOES-R global attributes.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("conventions : ");
        sb.append(conventions);
        sb.append("\nICD_version : ");
        sb.append(ICD_version);
        sb.append("\nbit_depth : ");
        sb.append(bit_depth);
        sb.append(" bits\ncentral_wavelength : ");
        sb.append(central_wavelength);
        sb.append(" um\nchannel_id : ");
        sb.append(channel_id);
        sb.append("\nnumber_product_tiles : ");
        sb.append(number_product_tiles);
        sb.append("\nperiodicity : ");
        sb.append(periodicity);
        sb.append("\npixel_x_size : ");
        sb.append(pixel_x_size);
        sb.append(" km\npixel_y_size : ");
        sb.append(pixel_y_size);
        sb.append(" km\nproduct_center_latitude : ");
        sb.append(product_center_latitude);
        sb.append(" deg\nproduct_center_longitude : ");
        sb.append(product_center_longitude);
        sb.append(" deg\nproduct_columns : ");
        sb.append(product_columns);
        sb.append("\nproduct_rows : ");
        sb.append(product_rows);
        sb.append("\nproduct_tile_height : ");
        sb.append(product_tile_height);
        sb.append("\nproduct_tile_width : ");
        sb.append(product_tile_width);
        sb.append("\nproduct_tile_size : ");
        sb.append(product_tile_size);
        sb.append("\nproduction_location : ");
        sb.append(production_location);
        sb.append("\nproduct_name : ");
        sb.append(product_name);
        sb.append("\nprojection : ");
        sb.append(projection);
        sb.append("\nrequest_spatial_resolution : ");
        sb.append(request_spatial_resolution);
        sb.append(" km\nsatellite_altitude : ");
        sb.append(satellite_altitude);
        sb.append(" km\nsatellite_id : ");
        sb.append(satellite_id);
        sb.append("\nsatellite_latitude : ");
        sb.append(satellite_latitude);
        sb.append(" deg\nsatellite_longitude : ");
        sb.append(satellite_longitude);
        sb.append(" deg\nsatellite_mode : ");
        sb.append(satellite_mode);
        sb.append("\nsource_scene : ");
        sb.append(source_scene);
        sb.append("\nsource_spatial_resolution : ");
        sb.append(source_spatial_resolution);
        sb.append(" km\nstart_date_time : ");
        synchronized(GOESRConstants.GOESR_DATETIME) {
            sb.append(GOESRConstants.GOESR_DATETIME.format(start_date_time));
        }
        sb.append(" UTC\ntile_center_latitude : ");
        sb.append(tile_center_latitude);
        sb.append(" deg\ntile_center_longitude : ");
        sb.append(tile_center_longitude);
        sb.append(" deg\ntile_column_offset : ");
        sb.append(tile_column_offset);
        sb.append("\ntile_row_offset : ");
        sb.append(tile_row_offset);
        sb.append("\ntitle : ");
        sb.append(title);
        sb.append("\n");
        return sb.toString();
    }

}
