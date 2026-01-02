package org.creatorledger.user.domain

import spock.lang.Specification

class EmailSpec extends Specification {

    def "should create a valid Email"() {
        given: "a valid email address"
        def emailAddress = "user@example.com"

        when: "creating an Email"
        def email = Email.of(emailAddress)

        then: "it should contain the email address"
        email.value() == emailAddress
    }

    def "should accept various valid email formats"() {
        when: "creating an Email with a valid format"
        def email = Email.of(validEmail)

        then: "it should be created successfully"
        email.value() == validEmail

        where:
        validEmail << [
            "simple@example.com",
            "user.name@example.com",
            "user+tag@example.co.uk",
            "user_name@subdomain.example.com",
            "123@example.com"
        ]
    }

    def "should reject null email"() {
        when: "creating an Email with null"
        Email.of(null)

        then: "it should throw IllegalArgumentException"
        def exception = thrown(IllegalArgumentException)
        exception.message == "Email cannot be null or blank"
    }

    def "should reject blank email"() {
        when: "creating an Email with blank string"
        Email.of(blankEmail)

        then: "it should throw IllegalArgumentException"
        def exception = thrown(IllegalArgumentException)
        exception.message == "Email cannot be null or blank"

        where:
        blankEmail << ["   ", ""]
    }

    def "should reject invalid email formats"() {
        when: "creating an Email with invalid format"
        Email.of(invalidEmail)

        then: "it should throw IllegalArgumentException"
        def exception = thrown(IllegalArgumentException)
        exception.message == "Invalid email format"

        where:
        invalidEmail << [
            "notanemail",
            "@example.com",
            "user@",
            "user @example.com",
            "user@.com",
            "user..name@example.com"
        ]
    }

    def "should be equal when email addresses are equal"() {
        given: "the same email address"
        def emailAddress = "user@example.com"

        when: "creating two Emails with the same address"
        def email1 = Email.of(emailAddress)
        def email2 = Email.of(emailAddress)

        then: "they should be equal"
        email1 == email2
        email1.hashCode() == email2.hashCode()
    }

    def "should normalize email to lowercase"() {
        when: "creating an Email with mixed case"
        def email = Email.of("User@EXAMPLE.COM")

        then: "it should be normalized to lowercase"
        email.value() == "user@example.com"
    }

    def "should have meaningful toString"() {
        given: "an Email"
        def emailAddress = "user@example.com"
        def email = Email.of(emailAddress)

        when: "converting to string"
        def result = email.toString()

        then: "it should contain the email address"
        result.contains(emailAddress)
    }
}
