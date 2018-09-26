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

     New Sat RGB function added per SET, created by Jordan Gerth, CIMSS/SSEC, U. Wisc.
     Used in SET locally derived channel difference configurations.  In those files
     Difference is replaced  with this one, DifferenceTwoAdj, better handling of diff values close to zero.
'''
from numpy import logical_and, where
def execute(*args):
    diffArgs = list(args)
    result = diffArgs[0] - diffArgs[1]
    return where(logical_and(sum(diffArgs) != 0.0, result == 0.0), 0.01, result)
