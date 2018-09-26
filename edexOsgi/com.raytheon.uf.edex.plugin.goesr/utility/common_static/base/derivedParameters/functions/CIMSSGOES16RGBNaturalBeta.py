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

	This code was developed at UW Madison CIMSS for generating GOES-16 true color RGB images on the fly,
        It required no additional information besides the already existing first three GOES16 ABI channels.
        The green band gets generated on the fly and the enhancements applied do not require and further information:

	Algorithm was developed by Kaba Bah at UW-Madison CIMSS.
        Kaba Bah: (kbah@ssec.wisc.edu),Ph:608-262-4462

INPUT PARAMETERS:
        All inputs are expected to be 8bit for theIS Beta version.
 	
    @param physicalElement1: 
		GOES16 ABI band01 (0.47um) data.

 	@param physicalElement2:
		GOES16 ABI band02 (0.64um) data.
 
 	@param physicalElement3:
		GOES16 ABI band03 (0.87um) data.

    @param bnum:
		The band number. (0=blue, 1=red, 2=green)
		
 	@param minValue:
		The expected minimum value for the returned array

 	@param maxValue:
		The expected maximum value for the returned array
 	@param gamma:
		Gamma value used for stretching. (0=n0, 1=yes)


RETURNS:

 	@return: Display values
 	@rtype: numpy array (int12)

DEPENDENCIES:

	* Numpy


MODIFICATIONS:

'''
	
import numpy as np

def execute(physicalElement1, physicalElement2, physicalElement3, bnum, minValue, maxValue, gamma):
    
	#########################
	# Make Green band on the fly is makeGreen=1.
	if (int(bnum) == 1):
		aband	= 2.5*physicalElement1
	if (int(bnum) == 2):
		aband	= 2.5*physicalElement2  
        if (int(bnum) == 3):
		#Make Green band on the fly.
		aband	= 2.5*(0.45*physicalElement1 + 0.45*physicalElement2 + 0.10*physicalElement3) 


	#########################
	# Convert data to 0.0 t0 1.0 for use in generating a SQRT enhancement.
	aband0 =(aband/255.0) 

	#########################

	# Apply a simple sqrt following UW-CIMSS True color RGB (for Natural color).
	aband = (np.sqrt(aband0)*255.0)
      
        ####################################
        #### Apply a simple contrast adjustment enhancement
        #acont=maxValue/10.0
        #amax=maxValue+4 
        #amid=maxValue/2.0
        acont=255.0/10.0
        amax=255.0+4 
        amid=255.0/2.0
        afact=(amax*(acont+maxValue)/(maxValue*(amax-acont)))
        aband = (afact*(aband-amid)+amid)
        aband[aband <= 10] = 0
        aband[aband >=255] = 255

	#########################
	# Convert from float to in8.
	dispByte = np.array(aband, dtype=np.int8)

	#########################
	# Convert values of 0 to 1.
	dispByte[dispByte == 0] = 255

	#########################
	#mask out of range values
	dispByte[(aband == 0)] = 0

	return dispByte



