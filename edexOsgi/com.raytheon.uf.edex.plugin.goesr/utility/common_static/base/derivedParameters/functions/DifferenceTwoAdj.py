'''
   <!-- 
        This is an absolute override file, indicating that a higher priority 
        version of the file will completely replace a lower priority version
        of the file. 
    -->
   <!-- TOWRdocs Header
         Satellite Enhancement Team (SET) DifferenceTwoAdj.py
    -->
    <!-- TOWRdocs Description
  Used in GOES-16 channel differences in place of Difference.py, takes into
  account differences close to zero (within 0.01) so that those pixels
  are not assigned an undefined value
    -->
    <!-- TOWRdocs Status
         New Channel Difference function added per SET, created by Jordan Gerth, CIMSS/SSEC, U. Wisc.
         Used in SET locally derived channel difference configurations.  In those files
     Difference is replaced  with this one, DifferenceTwoAdj, better handling of diff values close to zero.
   -->
    <!-- TOWRdocs POC
         Lee Byerle 12/20/2017
    -->
'''
from numpy import logical_and, where
def execute(*args):
    diffArgs = list(args)
    result = diffArgs[0] - diffArgs[1]
    return where(logical_and(sum(diffArgs) != 0.0, result == 0.0), 0.01, result)
