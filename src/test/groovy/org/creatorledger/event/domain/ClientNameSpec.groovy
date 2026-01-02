package org.creatorledger.event.domain

import spock.lang.Specification

class ClientNameSpec extends Specification {

    def "should create a valid ClientName"() {
        given: "a valid client name"
        def name = "Acme Corporation"

        when: "creating a ClientName"
        def clientName = ClientName.of(name)

        then: "it should contain the name"
        clientName.value() == name
    }

    def "should accept various valid client names"() {
        when: "creating a ClientName with a valid format"
        def clientName = ClientName.of(validName)

        then: "it should be created successfully"
        clientName.value() == validName

        where:
        validName << [
            "John Smith",
            "ABC Ltd",
            "Company 123",
            "O'Brien & Associates",
            "Smith-Jones Consulting",
            "Café Deluxe",
            "T"  // Single character is valid
        ]
    }

    def "should reject null client name"() {
        when: "creating a ClientName with null"
        ClientName.of(null)

        then: "it should throw IllegalArgumentException"
        def exception = thrown(IllegalArgumentException)
        exception.message == "ClientName cannot be null or blank"
    }

    def "should reject blank client name"() {
        when: "creating a ClientName with blank string"
        ClientName.of(blankName)

        then: "it should throw IllegalArgumentException"
        def exception = thrown(IllegalArgumentException)
        exception.message == "ClientName cannot be null or blank"

        where:
        blankName << ["", "   ", "\t", "\n"]
    }

    def "should reject client names that are too long"() {
        given: "a name longer than 200 characters"
        def tooLongName = "A" * 201

        when: "creating a ClientName with too long name"
        ClientName.of(tooLongName)

        then: "it should throw IllegalArgumentException"
        def exception = thrown(IllegalArgumentException)
        exception.message == "ClientName cannot exceed 200 characters"
    }

    def "should trim whitespace from client name"() {
        when: "creating a ClientName with extra whitespace"
        def clientName = ClientName.of("  Acme Corp  ")

        then: "it should trim the whitespace"
        clientName.value() == "Acme Corp"
    }

    def "should be equal when names are equal"() {
        given: "the same client name"
        def name = "Acme Corporation"

        when: "creating two ClientNames with the same name"
        def clientName1 = ClientName.of(name)
        def clientName2 = ClientName.of(name)

        then: "they should be equal"
        clientName1 == clientName2
        clientName1.hashCode() == clientName2.hashCode()
    }

    def "should have meaningful toString"() {
        given: "a ClientName"
        def name = "Acme Corporation"
        def clientName = ClientName.of(name)

        when: "converting to string"
        def result = clientName.toString()

        then: "it should contain the name"
        result.contains(name)
    }
}
