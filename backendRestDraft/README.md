## Spring Setup Instructions

1) install java jdk1.8

2) install maven
	- start a NEW command line
	- run **mvn -v**
	- if this works and the java home is set corretly, you are fine so far.
	
3) install sts
    - Take the 64 bit version (if you are on a 64bit system), because 32 might not start at all.
4) install lombok
	- save lombok.jar parallel to STS.exe
	- run **java -jar lombok.jar**
		- select STS.exe
		- install/update
		
5) create workspace outside of git repo
	- import "Existing Maven Project"
	- select the pom.xml

6) install m2e-apt
	- goto Eclipse Marketplace
		- search **m2e-apt**
		- install
		- restart eclipse

You should be able to build the project now. Simply right click the project -> Run as ... -> Spring Boot App

If you have errors in eclipse, go to the folder containing the pom.xml, open a command line and run *mvn clean package*. If this builds successfully, you only have a problem within eclipse.

**If you still encounter problems, please ask.**