Files for testing the plugin:

	GOES-R:
		- Files are coming in over SBN
		- /data_store/goes-r/yyyymmdd/hh/IXTU99* 	(any files starting with IXTU99 are GOES-R DMW)

	Himawari:
		- Files are posted to the anonymous ftp site "ftp://ncoh8_dev@ftp.star.nesdis.noaa.gov/AHI_Winds/nc/"
		- All files located on that site are Himawari DMW Files

Plugin information:

	- Postgres table name : "dmw"
	- Original plugin/modifications at AWIPS2_GOES-R/edexOsgi/
	- dependency on gov.noaa.nws.obs.uf.edex.plugin.goesr.dmw.description (../AWIPS2_NWS/edex)
		- contains description files (../AWIPS2_NWS/gov.noaa.nws.obs....../utility/common_static/satellite/dmw/descriptions) and other configuration files
		- Separate description files for GOES-R (goesr-dmwDescriptions.xml) and Himawari (ahi-dmwDescriptions.xml)
		- Separate menu/bundles for GOES-R and Himawari

Testing In CAVE:

	- Separate menu indexes for GOES-R and Himawari

	** Simplest way to test both is to uncomment create GOES-R & Himawari Top level menu **
		1. in "../AWIPS2_GOES-R/edexOsgi/com.raytheon.uf.edex.plugin.goesr/utility/common_static/base/menus/satellite/goesr/goesr-index.xml"
			- Comment out "<include>" tag under "<!-- Default install location is nowhere, only want menu at specific sites. -->"
			- Uncomment full "<include>" tag under "<!-- Uncomment this element to include goesr as top level menu in D2D -->" to create top-level GOES-R Menu

		2. in "../AWIPS2_GOES-R/edexOsgi/com.raytheon.uf.edex.plugin.goesr/utility/common_static/base/menus/satellite/himawari/himawari-index.xml"
			- Comment out "<include>" tag under "<!-- Default install location is nowhere, only want menu at specific sites. -->"
			- Uncomment full "<include>" tag under "<!-- Uncomment this element to include goesr as top level menu in D2D -->" to create top-level Himawari Menu

		3. DMW Menu Indexes for GOES-R/Himawari will automatically install under their respective top-level menu
			- GOES-R Installs to top-level GOES-R Menu (if exists) under section header "------ Derived Motion Winds ------"
			- Himawari Installs to top-level Himawari Menu (if exists) under section header "------ Derived Motion Winds ------"

Questions/Comments

	email Matthew.Comerford@noaa.gov for any questions (testing, test data acquisition, etc.)