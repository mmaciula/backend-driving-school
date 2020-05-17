# Driving school
## Build project

To be able to run the application you need to build it first. To build and package
an app use the below command from the project folder which contains the `pom.xml`
file.
```$xslt
mvn package
```
You can also use:
```$xslt
mvn install
```
or
```$xslt
mvn clean install
```
## Run
You can use Maven plugin to run the application by using the below example:
```$xslt
mvn spring-boot:run
```
## Technology
* Java 8,
* Spring Boot (Spring Security, Spring Data JPA),
* Groovy,
* Spock,
* JSON Web Token (JWT),
* PostrgeSQL,
* Maven