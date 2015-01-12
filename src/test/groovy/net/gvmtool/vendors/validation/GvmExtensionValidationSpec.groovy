package net.gvmtool.vendors.validation

import net.gvmtool.vendors.GvmExtension
import spock.lang.Specification

import javax.validation.ConstraintViolation
import javax.validation.Validation
import javax.validation.Validator

class GvmExtensionValidationSpec extends Specification {

    static validatorFactory

    Validator validator

    GvmExtension config = new GvmExtension(
        apiUrl: "https://release-dev.cfapps.io",
        clientId: "release_client_id",
        clientSecret: "82V6G63PsgL0zn3eOvCLO9Ro",
        username: "release_admin",
        password: "some_password",
        candidate: "grails",
        version: "2.4.4",
        url: "http://dist.springframework.org.s3.amazonaws.com/release/GRAILS/grails-2.4.4.zip"
    )

    void setupSpec() {
        validatorFactory = Validation.buildDefaultValidatorFactory()
    }

    void setup() {
        validator = validatorFactory.validator
    }

    void "should validate apiUrl"() {
        setup:
        config.apiUrl = url
        def constraints = validator.validate(config)

        expect:
        constraints.size() == size
        extractMessage(constraints) == message

        where:
        url                             | size | message
        null                            |    1 | "may not be null"
        "invalid_url"                   |    1 | "must be a valid URL"
        "https://release-dev.cfapps.io" |    0 | "no message"
    }

    void "should validate clientId is not null"() {
        given:
        config.clientId = null

        when:
        def constraints = validator.validate(config)

        then:
        constraints.size() == 1
        extractMessage(constraints) == "may not be null"
    }

    void "should validate clientSecret"() {
        given:
        config.clientSecret = clientSecret
        def constraints = validator.validate(config)

        expect:
        constraints.size() == size
        extractMessage(constraints) == message

        where:
        clientSecret | size | message
        null                         |    1 | "may not be null"
        "abcdef"                     |    1 | "must be 24 characters long"
        "82V6G63PsgL0zn3eOvCLO9Ro1"  |    1 | "must be 24 characters long"
        "82V6G63PsgL0zn3eOvCLO9Ro"   |    0 | "no message"
    }

    void "should validate username is not null"() {
        given:
        config.username = null

        when:
        def constraints = validator.validate(config)

        then:
        constraints.size() == 1
        extractMessage(constraints) == "may not be null"
    }

    void "should validate password is not null"() {
        given:
        config.password = null

        when:
        def constraints = validator.validate(config)

        then:
        constraints.size() == 1
        extractMessage(constraints) == "may not be null"
    }

    void "should validate candidate is not null"() {
        given:
        config.candidate = null

        when:
        def constraints = validator.validate(config)

        then:
        constraints.size() == 1
        extractMessage(constraints) == "may not be null"
    }

    void "should validate candidate version is not null"() {
        given:
        config.version = null

        when:
        def constraints = validator.validate(config)

        then:
        constraints.size() == 1
        extractMessage(constraints) == "may not be null"
    }

    void "should validate candidate version url"() {
        setup:
        config.url = url
        def constraints = validator.validate(config)

        expect:
        constraints.size() == size
        extractMessage(constraints) == message

        where:
        url                                                                               | size | message
        null                                                                              |    1 | "may not be null"
        "invalid_url"                                                                     |    1 | "must be a valid URL"
        "http://dist.springframework.org.s3.amazonaws.com/release/GRAILS/grails-2.4.4.zip"|    0 | "no message"
    }

    private static extractMessage(Set<ConstraintViolation> constraints) {
        (constraints.size() > 0) ? constraints.first().message : "no message"
    }
}
