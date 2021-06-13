package pl.superjazda.drivingschool.model

import pl.superjazda.drivingschool.api.user.User
import spock.lang.Shared
import spock.lang.Specification

import javax.validation.Validation

class ModelValidationTest extends Specification {
    @Shared
    def validatorFactory = Validation.buildDefaultValidatorFactory()
    @Shared
    def validator = validatorFactory.getValidator()

    def "should have no violations"() {
        def violations = validator.validate(new User("jack", "jack@domain.com", "password", "Jack", "Brown"))

        expect:
            violations.isEmpty()
    }

    def "username is too short"() {
        def violations = validator.validate(new User("ja", "jack@domain.com", "password", "Jack", "Brown"))
        def violation = violations.iterator().next()

        expect:
            violations.size() == 1
            violation.getMessage() == "Username must be at least 3 characters"
            violation.getPropertyPath().toString() == "username"
            violation.getInvalidValue() == "ja"
    }

    def "email is invalid"() {
        def violations = validator.validate(new User("jack", "jack@", "password", "Jack", "Brown"))
        def violation = violations.iterator().next()

        expect:
            violations.size() == 1
            violation.getMessage() == "Invalid email"
    }

    def "password is too short"() {
        def violations = validator.validate(new User("jack", "jack@domain.com", "pass", "Jack", "Brown"))
        def violation = violations.iterator().next()

        expect:
            violations.size() == 1
            violation.getMessage() == "Password is too short"
    }

    def "violations in all fields"() {
        def violations = validator.validate(new User("ja", "jack", "pass", "Jack", "Brown"))

        expect:
            violations.size() == 3
    }

    def cleanupSpec() {
        validatorFactory.close()
    }
}
