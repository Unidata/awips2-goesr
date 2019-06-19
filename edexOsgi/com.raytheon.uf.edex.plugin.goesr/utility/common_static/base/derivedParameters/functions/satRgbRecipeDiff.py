'''
   <!-- 
        This is an absolute override file, indicating that a higher priority 
        version of the file will completely replace a lower priority version
        of the file. 
    -->
<!-- TOWRdocs Header
  Derived Parameter Python file for implementing channel difference RGB recipes.
-->
<!-- TOWRdocs Description
  This method implements channel difference RGB recipes outlined in EUMETSAT's "Best
  Practices for RGB Displays of Multispectral Imagery" document 
  (http://oiswww.eumetsat.int/~idds/html/doc/best_practices.pdf). Two arrays
  of satellite data are passed to this method along with various recipe options 
  outlined below. The channel difference is calculated as physicalElement1 - 
  physicalElement2. An array of display values is returned that corresponds to a 
  single color component of an RGB product.
-->
<!-- TOWRdocs Status
  This is a new derived parameters file.
  It is used to implement channel difference RGB recipes.
-->
<!-- TOWRdocs POC
  Kevin M. McGrath
-->

CONTACTS: 

	This code was co-developed via the AWIPS II Experimental Products Development 
	Team (EPDT) by personnel from NASA SPoRT, CIRA, and the NWS:

		Jason Burks/CIRA/MDL (jason.burks@noaa.gov)
		Nancy Eustice/NWS (nancy.eustice@noaa.gov)
		Kevin McGrath/NASA SPoRT/Jacobs (kevin.m.mcgrath@nasa.gov)
		Deb Molenar/NOAA/NESDIS/RAMMB (Debra.Molenar@noaa.gov)
		Matt Smith/NASA SPoRT/UAH (matthew.r.smith@nasa.gov)
		Nate Smith/NWS (nate.smith@noaa.gov)

INPUT PARAMETERS:

 	@param physicalElement1:
		Array of satellite data.

 	@param physicalElement2:
		Array of satellite data.

 	@param calNumber:
		This integer corresponds to methods in satRgbRecipeCalibration.py,
		which converts satellite data values to physical values(e.g., 
		IR counts to brightness temperatures). For most data, calNumber is 0 
		(no calibration required) because these values are already calibrated 
		when passed to this method. The units of the data passed to this method
		is controlled by the the "unit=" option in the derived parameter definition
		.xml files. As an example with GOES-R IR data, we need the data to be calibrated
		to brightness temperature in units of Kelvin. The following line in a derived
		parameter definition .xml file would accomplish this:

			<Field abbreviation="CH-08-6.19um" unit="K"/> 

		Here's an example requesting GOES-13 IR data (unit="IRPixel") in units of Kelvin:

			<Field abbreviation="Imager 11 micron IR" unit="IRPixel"/>

		In the case of visible imagery, we want the data to be in albedo from 0 to 100.
		The following line in a derived	parameter definition .xml file would accomplish this
		for GOES-R visible data (unit="%"):

			<Field abbreviation="CH-06-2.25um" unit="%"/> 

		If no "unit=" option is included in the "Field abbreviation" derived parameter
		definition, raw values will be passed to this method.

 	@param minCalibratedValue:
		The calibrated satellite data values are clipped to this minimum 
		value.

 	@param maxCalibratedValue:
		The calibrated satellite data values are clipped to this maximum 
		value.

 	@param gamma:
		Gamma value used for stretching.

 	@param invert:
		Invert the final display values (255 - value)?  (0 = no, 1 = yes)

RETURNS:

 	@return: Display values
 	@rtype: numpy array (int8)

DEPENDENCIES:

	* Numpy

	* The satRgbRecipeCalibration module found in satRgbRecipeCalibration.py:
		This is only required and imported if we need to calibrate the satellite
		data (calNumber != 0).

MODIFICATIONS:

'''
	
import numpy as np

def execute(physicalElement1, physicalElement2, calNum, minCalibratedValue, maxCalibratedValue, gamma,  invert):
    
	#########################
	# Create calibrated arrays.
	if (int(calNum) == 0):
		# No need to calibrate the data.
		a1_calibrated	= physicalElement1
		a2_calibrated	= physicalElement2
	else:

		# Import the calibration module.
		import satRgbRecipeCalibration as calibration

		# Define calibration method to call.
		calTypeString		= 'calType_' + str(int(calNum))
		try:
		    calMethodToCall = getattr(calibration, calTypeString)
		except AttributeError:
		    return(0)

		# Calibrate the data by calling calType_<calNum> method.
		a1_calibrated	=   calMethodToCall(physicalElement1)
		a2_calibrated	=   calMethodToCall(physicalElement2)

	#########################
	# Calculate the difference between a1_calibrated and a2_calibrated.
	diff_calibrated = a1_calibrated - a2_calibrated

	#########################
	# Clip the calibrated values.
	diff_calibrated_clipped = np.clip(diff_calibrated, minCalibratedValue, maxCalibratedValue)

	#########################
	# Generate display values by implement EUMETSAT recipe.
	dispValue_float  = 255.*(np.power( (diff_calibrated_clipped - minCalibratedValue)/(maxCalibratedValue - minCalibratedValue), (1./gamma)))

	#########################
	# Invert, if selected.
	if (invert):
		dispValue_float = 255. - dispValue_float

	#########################
	# Convert from float to int8.
	dispValue_byte = np.array(dispValue_float, dtype=np.int8)

	#########################
	# CAVE interprets byte values of 0 as "No Data".  Force values of 0 to 1.
	dispValue_byte[dispValue_byte == 0] = 1

	#########################
	# For any pixels where physicalElement1 or physicalElement2 is zero (no data), set
	# the displayValue_byte to 0 (no data) so that the pixel is transparent.
	dispValue_byte[(physicalElement1 == 0) | (physicalElement2 == 0)] = 0

	return dispValue_byte
