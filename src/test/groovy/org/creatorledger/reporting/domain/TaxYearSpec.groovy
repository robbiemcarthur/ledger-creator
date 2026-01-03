package org.creatorledger.reporting.domain

import spock.lang.Specification

import java.time.LocalDate

class TaxYearSpec extends Specification {

    def "should create a valid tax year"() {
        given: "a valid start year"
        def startYear = 2024

        when: "a tax year is created"
        def taxYear = TaxYear.of(startYear)

        then: "it is created successfully"
        taxYear != null
        taxYear.startYear() == 2024
    }

    def "should calculate correct start date for UK tax year"() {
        given: "a tax year starting in 2024"
        def taxYear = TaxYear.of(2024)

        when: "the start date is requested"
        def startDate = taxYear.startDate()

        then: "it should be April 6th 2024"
        startDate == LocalDate.of(2024, 4, 6)
    }

    def "should calculate correct end date for UK tax year"() {
        given: "a tax year starting in 2024"
        def taxYear = TaxYear.of(2024)

        when: "the end date is requested"
        def endDate = taxYear.endDate()

        then: "it should be April 5th 2025"
        endDate == LocalDate.of(2025, 4, 5)
    }

    def "should determine if a date falls within the tax year"() {
        given: "a tax year 2024-25"
        def taxYear = TaxYear.of(2024)

        expect: "dates within the range return true"
        taxYear.contains(LocalDate.of(2024, 4, 6))  // start date
        taxYear.contains(LocalDate.of(2024, 7, 15)) // mid-year
        taxYear.contains(LocalDate.of(2025, 4, 5))  // end date

        and: "dates outside the range return false"
        !taxYear.contains(LocalDate.of(2024, 4, 5))  // day before
        !taxYear.contains(LocalDate.of(2025, 4, 6))  // day after
        !taxYear.contains(LocalDate.of(2023, 12, 1)) // previous year
    }

    def "should reject null start year"() {
        when: "a tax year is created with null"
        TaxYear.of(null)

        then: "it throws IllegalArgumentException"
        def exception = thrown(IllegalArgumentException)
        exception.message == "Start year cannot be null"
    }

    def "should reject start year too far in the past"() {
        given: "a start year more than 10 years in the past"
        def tooOldYear = LocalDate.now().year - 11

        when: "a tax year is created"
        TaxYear.of(tooOldYear)

        then: "it throws IllegalArgumentException"
        def exception = thrown(IllegalArgumentException)
        exception.message.contains("must be within")
    }

    def "should reject start year too far in the future"() {
        given: "a start year more than 5 years in the future"
        def tooFutureYear = LocalDate.now().year + 6

        when: "a tax year is created"
        TaxYear.of(tooFutureYear)

        then: "it throws IllegalArgumentException"
        def exception = thrown(IllegalArgumentException)
        exception.message.contains("must be within")
    }

    def "should provide readable toString format"() {
        given: "a tax year 2024-25"
        def taxYear = TaxYear.of(2024)

        when: "toString is called"
        def result = taxYear.toString()

        then: "it returns a readable format"
        result == "TaxYear[2024-2025]"
    }

    def "should be equal when start years are the same"() {
        given: "two tax years with the same start year"
        def taxYear1 = TaxYear.of(2024)
        def taxYear2 = TaxYear.of(2024)

        expect: "they are equal"
        taxYear1 == taxYear2
        taxYear1.hashCode() == taxYear2.hashCode()
    }

    def "should not be equal when start years differ"() {
        given: "two tax years with different start years"
        def taxYear1 = TaxYear.of(2024)
        def taxYear2 = TaxYear.of(2023)

        expect: "they are not equal"
        taxYear1 != taxYear2
    }
}
