package org.creatorledger.event.domain

import spock.lang.Specification
import java.time.LocalDate

class EventDateSpec extends Specification {

    def "should create a valid EventDate from LocalDate"() {
        given: "a valid date"
        def date = LocalDate.of(2026, 1, 15)

        when: "creating an EventDate"
        def eventDate = EventDate.of(date)

        then: "it should contain the date"
        eventDate.value() == date
    }

    def "should create EventDate for today"() {
        given: "today's date"
        def today = LocalDate.now()

        when: "creating an EventDate for today"
        def eventDate = EventDate.today()

        then: "it should be today's date"
        eventDate.value() == today
    }

    def "should reject null date"() {
        when: "creating an EventDate with null"
        EventDate.of(null)

        then: "it should throw IllegalArgumentException"
        def exception = thrown(IllegalArgumentException)
        exception.message == "EventDate cannot be null"
    }

    def "should reject future dates beyond reasonable limit"() {
        given: "a date more than 5 years in the future"
        def farFuture = LocalDate.now().plusYears(6)

        when: "creating an EventDate with far future date"
        EventDate.of(farFuture)

        then: "it should throw IllegalArgumentException"
        def exception = thrown(IllegalArgumentException)
        exception.message == "EventDate cannot be more than 5 years in the future"
    }

    def "should reject dates too far in the past"() {
        given: "a date more than 10 years in the past"
        def distantPast = LocalDate.now().minusYears(11)

        when: "creating an EventDate with distant past date"
        EventDate.of(distantPast)

        then: "it should throw IllegalArgumentException"
        def exception = thrown(IllegalArgumentException)
        exception.message == "EventDate cannot be more than 10 years in the past"
    }

    def "should accept reasonable past dates"() {
        when: "creating an EventDate within the last year"
        def eventDate = EventDate.of(pastDate)

        then: "it should be created successfully"
        eventDate.value() == pastDate

        where:
        pastDate << [
            LocalDate.now().minusDays(30),
            LocalDate.now().minusMonths(6),
            LocalDate.now().minusYears(2)
        ]
    }

    def "should accept reasonable future dates"() {
        when: "creating an EventDate within the next year"
        def eventDate = EventDate.of(futureDate)

        then: "it should be created successfully"
        eventDate.value() == futureDate

        where:
        futureDate << [
            LocalDate.now().plusDays(7),
            LocalDate.now().plusMonths(3),
            LocalDate.now().plusYears(1)
        ]
    }

    def "should be equal when dates are equal"() {
        given: "the same date"
        def date = LocalDate.of(2026, 1, 15)

        when: "creating two EventDates with the same date"
        def eventDate1 = EventDate.of(date)
        def eventDate2 = EventDate.of(date)

        then: "they should be equal"
        eventDate1 == eventDate2
        eventDate1.hashCode() == eventDate2.hashCode()
    }

    def "should compare dates chronologically"() {
        given: "two different dates"
        def earlier = EventDate.of(LocalDate.of(2026, 1, 10))
        def later = EventDate.of(LocalDate.of(2026, 1, 20))

        expect: "earlier date to be before later date"
        earlier.isBefore(later)
        later.isAfter(earlier)
        !earlier.isAfter(later)
        !later.isBefore(earlier)
    }

    def "should have meaningful toString"() {
        given: "an EventDate"
        def date = LocalDate.of(2026, 1, 15)
        def eventDate = EventDate.of(date)

        when: "converting to string"
        def result = eventDate.toString()

        then: "it should contain the date"
        result.contains("2026-01-15")
    }
}
