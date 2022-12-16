'''
        This_software_was_developed_and_/_or_modified_by_Raytheon_Company,
        pursuant_to_Contract_DG133W-05-CQ-1067_with_the_US_Government.
        
        U.S._EXPORT_CONTROLLED_TECHNICAL_DATA
        This_software_product_contains_export-restricted_data_whose
        export/transfer/disclosure_is_restricted_by_U.S._law._Dissemination
        to_non-U.S._persons_whether_in_the_United_States_or_abroad_requires
        an_export_license_or_other_authorization.
        
        Contractor_Name:________Raytheon_Company
        Contractor_Address:_____6825_Pine_Street,_Suite_340
        ________________________Mail_Stop_B8
        ________________________Omaha,_NE_68106
        ________________________402.291.0100
        
        See_the_AWIPS_II_Master_Rights_File_("Master_Rights_File.pdf")_for
        further_licensing_information.
    -->
  For converting raw satellite data values into calibrated values.
  For the RGB generation methods found in satRgbRecipeDiff.py and satRgbRecipeSingleChannel.py.
  Kevin M. McGrath, NASA SpORT

  This code was co-developed via the AWIPS II Experimental Products Development 
  Team (EPDT) by personnel from NASA SPoRT, CIRA, and the NWS:

		Jason Burks/CIRA/MDL (jason.burks@noaa.gov)
		Nancy Eustice/NWS (nancy.eustice@noaa.gov)
		Kevin McGrath/NASA SPoRT/Jacobs (kevin.m.mcgrath@nasa.gov)
		Deb Molenar/NOAA/NESDIS/RAMMB (Debra.Molenar@noaa.gov)
		Matt Smith/NASA SPoRT/UAH (matthew.r.smith@nasa.gov)
		Nate Smith/NWS (nate.smith@noaa.gov)

INPUT PARAMETERS:

 	@param physicalElement:
		Array of satellite data to be calibrated.

RETURNS:

 	@return: Calibrated satellite data values
 	@rtype: numpy array (float32)

DEPENDENCIES:

	* Numpy

MODIFICATIONS:

'''

import numpy as np

#########################
# 8-bit IR Calibration. Converts uint8 (0-255) to brightness temperature [K]. Returns Tb (float32).
def calType_1(physicalElement):

	a_byte = np.array(physicalElement, dtype=np.uint8)

	# Create a new array of the same size as a. Fill with zeros.
	a_Tb=np.zeros(a_byte.shape,'float32')

	# Create a mask where a > 176.
	mask1a = (a_byte > 176)

	# Where a > 176, convert to Tb.
	a_Tb[mask1a] = 418.-a_byte[mask1a]

	# Create a mask where a <= 176.
	mask1b = (a_byte <= 176)

	# Where a <= 176, convert to Tb.s
	a_Tb[mask1b] = 0.5 *(660. - a_byte[mask1b])

	return(a_Tb)

#########################
# 8-bit vis data. Scales uint8 (0-255) to non-linear (^2) albedo from 0 to 100. Returns non-linear Albedo (float32).
def calType_2(physicalElement):

	a_byte = np.array(physicalElement, dtype=np.uint8)

	# Scale data from 0 to 100 (non-linear)
	return( ((a_byte / 255.)**2) * 100.).astype(np.float32)

#########################
# 8-bit vis data. Scales uint8 (0-255) to linear albedo from 0 to 100. Returns linear Albedo (float32).
def calType_3(physicalElement):

	a_byte = np.array(physicalElement, dtype=np.uint8)

	# Scale data from 0 to 100 (linear)
	return((a_byte / 255.) * 100.).astype(np.float32)

#########################
# Solar Zenith Angle. Scales uint8 (0-255) to SZA from 0 to 180 degrees. Returns SZA (float32).
def calType_4(physicalElement):

	a_byte = np.array(physicalElement, dtype=np.uint8)

	# Scale data from 0 to 180.
	return((a_byte * (180./255.))).astype(np.float32)
