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


Here are some useful turorials, which were used for the setup of this spring boot application.


Spring Boot
https://jaxenter.de/spring-boot-tutorial-54020

Spring Security
https://www.codesandnotes.be/2014/10/31/restful-authentication-using-spring-security-on-spring-boot-and-jquery-as-a-web-client/

Spring Login
https://www.mkyong.com/spring-security/spring-security-custom-login-form-annotation-example/

Spring JPA
https://spring.io/blog/2011/02/10/getting-started-with-spring-data-jpa/

Spring JPA and QueryDSL
https://spring.io/blog/2011/04/26/advanced-spring-data-jpa-specifications-and-querydsl/
http://www.baeldung.com/querydsl-with-jpa-tutorial

Spring Authentication against Database with custom UserService
http://www.baeldung.com/spring-security-authentication-with-a-database

Spring and HSQLDB
https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-sql.html



**If you still encounter problems or have any questions, please ask.**

