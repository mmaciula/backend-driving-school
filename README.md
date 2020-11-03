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
## Tests
Run `mvn test` to execute unit and integration tests. The contact form test is skipped, to run it, remove `@Ignore`
annotation from the `src/test/groovy/pl/superjazda/drivingschool/contact/ContactControllerTest`
and enter the email address of the message sender in `email` field as well as configure the
recipient's credentials (`spring.mail.username`, `spring.mail.password`) in the `application.properties`
file licated in `src/test/resources/`.
## Technology
* Java 8,
* Spring (Boot, Security, Data JPA),
* Groovy,
* Spock,
* JUnit,
* Mockito,
* JSON Web Token (JWT),
* PostrgeSQL,
* Maven