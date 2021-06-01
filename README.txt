This utility takes measure spec json files as as input and outputs drl conditions

To generate measure json files from e-spec xml
	1. git checkout https://github.com/projectcypress/health-data-standards
	2. install ruby
	3. from checkout directory in step 1 fire below commands
		3.1 gem install bundler
		3.2 bundle install
	4. This contains a rake file to convert hqmf measure specification xml to json
		4.1 Navigate to root directory of and create folder named "xml"
		4.2 Copy all e-spec xml files to "xml" directory
		4.3 Navigate to health-data-standards-master\lib\tasks folder in command line
		4.5 Exceute rake task to parse and generate json files as below
			rake --trace hqmf:parse_all["xml","2.0"]
	5. At the end of the step 4, it will generate json file corresponding to each measure xml in "tmp\json\all" in root directory of the project
	

Copy measure wise json files generated in above process in this project

Execute MeasureGenerator as main program with respective json file path within this projects context.			

Measure Categories/ Examples:
----------------------------
Single IPP:
	-measures.add("2016/CMS131v4.xml.json")

Multiple IPP:
	- measures.add("2016/CMS69v4.xml.json")
	- measures.add("2016/CMS160v4.xml.json")
	- measures.add("2016/CMS136v5.xml.json")
	- measures.add("2016/CMS145v4.xml.json")
	- measures.add("2016/CMS156v4.xml.json")
	- measures.add("2016/CMS182v5.xml.json")
		
Single IPP + Multi Stratum:
	- measures.add("2016/CMS126v4.xml.json")
	- measures.add("2016/CMS153v4.xml.json")

Multi IPP + Multi Stratum:
	- measures.add("2016/CMS155v4.xml.json")
	
Episode Based: (To Analyze)
	- measures.add("2016/CMS68v5.xml.json")




		 