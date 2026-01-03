package org.creatorledger.user.infrastructure.web

import spock.lang.Specification

class RegisterUserRequestSpec extends Specification {

    def "should create a valid register user request"() {
        given: "a valid email"
        def email = "user@example.com"

        when: "a request is created"
        def request = new RegisterUserRequest(email)

        then: "it is created successfully"
        request != null
        request.email() == email
    }

    def "should reject null email"() {
        when: "creating request with null email"
        new RegisterUserRequest(null)

        then: "it throws IllegalArgumentException"
        def exception = thrown(IllegalArgumentException)
        exception.message == "Email cannot be null or blank"
    }

    def "should reject blank email"() {
        when: "creating request with blank email"
        new RegisterUserRequest("   ")

        then: "it throws IllegalArgumentException"
        def exception = thrown(IllegalArgumentException)
        exception.message == "Email cannot be null or blank"
    }

    def "should be equal when emails are the same"() {
        given: "two requests with the same email"
        def request1 = new RegisterUserRequest("user@example.com")
        def request2 = new RegisterUserRequest("user@example.com")

        expect: "they are equal"
        request1 == request2
        request1.hashCode() == request2.hashCode()
    }
}
