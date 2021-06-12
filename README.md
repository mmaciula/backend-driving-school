# Driving school
## Build project

To be able to run the application you need to build it first. To build and package
an app use the below command from the project folder which contains the `pom.xml`
file.
```$xslt
mvn compile
```
You can also use:
```$xslt
mvn install
```
or
```$xslt
mvn clean install
```

## Profiles
To select which profile to run (`dev` or `prod`), set the appropriate property in the
`src/main/java/resources/application.properties` file, e.g. for the `dev` profile
```$xslt
spring.profiles.active=dev
```
or called from the level of parameters passed in `VM options` by adding the `-D`
prefix
```$xslt
-Dspring.profiles.active=dev
```

## Jasypt encryption
The project uses Jasypt, which uses the `secret key` to encrypt database passwords
in the `src/main/reources/application-dev.properties` and `src/main/reources/application-prod.properties`,
as well as login credentials for smtp mail server in the `src/main/resources/application.properties`
and `src/test/resources/application.properties`.

To generate encrypted key, download tha jasypt `jar` file from the maven repository
and run it through the following command for version `2.1.2`:
```$xslt
java -cp //jasypt-2.1.2/lib/jasypt-2.1.2.jar org.jasypt.intf.cli.JasyptPBEStringEncryptionCLI input=”value to encrypt″ password=secretkey algorithm=PBEWithMD5AndDES
```

Add the encrypted key in the `*.properties` files in following format to make Jasypt
aware of encrypted values e.g.
```$xslt
spring.datasource.password=ENC(encrypted key)
```

Make Jasypt aware of the secret key which was used to form the encrypted value by
passing it as a property in the config file
```$xslt
jasypt.encryptor.password=secretkey
```
or run the project with following command
```$xslt
mvn -Djasypt.encryptor.password=secretkey spring-boot:run
```

## Tests
Run `mvn test` to execute unit and integration tests. The contact form test is ignored, to run it, remove `@Ignore`
annotation from the `src/test/groovy/pl/superjazda/drivingschool/contact/ContactControllerTest`
and enter the email address of the message sender in `email` field as well as configure the
recipient's credentials (`spring.mail.username`, `spring.mail.password`) in the `application.properties`
file located in `src/test/resources/`.
## Technology
* Java 11,
* Spring (Boot, Security, Data JPA),
* Groovy,
* Spock,
* JUnit,
* Mockito,
* JSON Web Token (JWT),
* PostrgeSQL,
* Maven,
* Jasypt 2.1.2