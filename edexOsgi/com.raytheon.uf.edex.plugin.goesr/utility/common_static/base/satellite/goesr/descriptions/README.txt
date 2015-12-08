This directory and it's subdirectories contain configuration files for the
GOES-R netcdf imagery decoder. Each configuration is an xml file that contains
one or more description elements that can be applied to a product. A
description consists of three parts, the match elements, the data elements,
and the descriptive elements.

The match elements of a description are used to determine if the description
applies to a particular product. It consists of an attribute_name that matches
a global attribute on the netcdf file and a pattern that is a regular
expression matched to the value of that attribute. If all match elements on a
description match attributes in a file then that description will be used to
describe the satellite data in the file.

A data element is optional in a description, if present there can be only one.
If a data element is specified then it should contain the name of the variable
containing the numeric, gridded data for the satellite image. An
alternative to specifying a variable name within the data element is to specify
a bitset containing multiple variables that will be joined together, one bit per
product. Products containing data in more than 2 directions can include a
verticalDimension attribute which should be the name of the variable describing
the 3rd dimension.

The descriptive elements are used to specify the physicalElement,
creatingEntity, source, sectorID, satHeight, and units of a
SatelliteRecord. Each description can be a constant value, an attribute or
variable whose value will be stored in the correct attribute, or a format and a
list of constants, attributes, and variables whose values will be used within
the format.

The dataTime element has a refTime element that requires a value, attribute, or 
variable that holds the refTime as either a formatted String or as an offset 
from an epoch. If the refTime value is a formatted String, it also requires a 
date format that will be used to parse a date from an the specified field. If 
the refTime value is an offset from an epoch, the epoch must be specified as 
well as the offset units.

For netcdf files that contain multiple products, a description containing a data
element should be created for each product. The descriptive for a description
with a data element will only be applied to products created with the data. Any
description without a data element will be applied to all the satellite records
that are generated from the file.

The directory structure of this directory is purely for organization, the
decoder does not do anything special with specific files/subdirectories. A
single product can match descriptions in multiple files as easily as multiple
descriptions in a single file.