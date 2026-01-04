package org.creatorledger.event.infrastructure.web

import org.creatorledger.event.application.EventApplicationService
import org.creatorledger.event.domain.ClientName
import org.creatorledger.event.domain.Event
import org.creatorledger.event.domain.EventDate
import org.creatorledger.event.api.EventId
import org.springframework.http.HttpStatus
import spock.lang.Specification
import java.time.LocalDate

class EventControllerUnitSpec extends Specification {

    EventApplicationService eventApplicationService
    EventController controller

    def setup() {
        eventApplicationService = Mock(EventApplicationService)
        controller = new EventController(eventApplicationService)
    }

    def "should create a new event"() {
        given: "a create event request"
        def request = new CreateEventRequest(
            LocalDate.of(2026, 3, 15),
            "Acme Corporation",
            "Website redesign workshop"
        )

        and: "the application service will return an event ID"
        def eventId = EventId.generate()
        eventApplicationService.create(_) >> eventId

        when: "creating the event"
        def response = controller.create(request)

        then: "the response is 201 Created with location header"
        response.statusCode == HttpStatus.CREATED
        response.headers.getLocation().toString() == "/api/events/${eventId.value()}"
    }

    def "should return 400 when creation fails with invalid data"() {
        given: "an invalid request"
        def request = new CreateEventRequest(
            LocalDate.of(2026, 3, 15),
            "Acme Corporation",
            "Workshop"
        )

        and: "the application service throws exception"
        eventApplicationService.create(_) >> { throw new IllegalArgumentException("Invalid date") }

        when: "creating the event"
        def response = controller.create(request)

        then: "the response is 400 Bad Request"
        response.statusCode == HttpStatus.BAD_REQUEST
    }

    def "should update an existing event"() {
        given: "an update event request"
        def eventId = EventId.generate()
        def request = new UpdateEventRequest(
            LocalDate.of(2026, 4, 20),
            "Beta Industries",
            "Updated description"
        )

        when: "updating the event"
        def response = controller.update(eventId.value().toString(), request)

        then: "the application service is called"
        1 * eventApplicationService.update(_)

        and: "the response is 204 No Content"
        response.statusCode == HttpStatus.NO_CONTENT
    }

    def "should return 400 when update fails"() {
        given: "an update request"
        def eventId = EventId.generate()
        def request = new UpdateEventRequest(
            LocalDate.of(2026, 4, 20),
            "Beta Industries",
            "Updated description"
        )

        and: "the application service throws exception"
        eventApplicationService.update(_) >> { throw new IllegalStateException("Event not found") }

        when: "updating the event"
        def response = controller.update(eventId.value().toString(), request)

        then: "the response is 400 Bad Request"
        response.statusCode == HttpStatus.BAD_REQUEST
    }

    def "should get event by ID"() {
        given: "an existing event"
        def eventId = EventId.generate()
        def event = Event.create(
            eventId,
            EventDate.of(LocalDate.of(2026, 3, 15)),
            ClientName.of("Acme Corporation"),
            "Website redesign workshop"
        )

        and: "the application service returns the event"
        eventApplicationService.findById(eventId) >> Optional.of(event)

        when: "getting the event"
        def response = controller.getEvent(eventId.value().toString())

        then: "the response is 200 OK with event data"
        response.statusCode == HttpStatus.OK
        response.body.id() == eventId.value().toString()
        response.body.date() == LocalDate.of(2026, 3, 15).toString()
        response.body.clientName() == "Acme Corporation"
        response.body.description() == "Website redesign workshop"
    }

    def "should return 404 when event not found"() {
        given: "a non-existent event ID"
        def eventId = EventId.generate()

        and: "the application service returns empty"
        eventApplicationService.findById(eventId) >> Optional.empty()

        when: "getting the event"
        def response = controller.getEvent(eventId.value().toString())

        then: "the response is 404 Not Found"
        response.statusCode == HttpStatus.NOT_FOUND
    }

    def "should return 400 for invalid UUID format"() {
        when: "getting event with invalid UUID"
        def response = controller.getEvent("invalid-uuid")

        then: "the response is 400 Bad Request"
        response.statusCode == HttpStatus.BAD_REQUEST
    }

    def "should check if event exists"() {
        given: "an existing event ID"
        def eventId = EventId.generate()

        and: "the application service confirms existence"
        eventApplicationService.existsById(eventId) >> true

        when: "checking existence"
        def response = controller.existsById(eventId.value().toString())

        then: "the response is 200 OK with true"
        response.statusCode == HttpStatus.OK
        response.body == true
    }

    def "should return false when event does not exist"() {
        given: "a non-existent event ID"
        def eventId = EventId.generate()

        and: "the application service returns false"
        eventApplicationService.existsById(eventId) >> false

        when: "checking existence"
        def response = controller.existsById(eventId.value().toString())

        then: "the response is 200 OK with false"
        response.statusCode == HttpStatus.OK
        response.body == false
    }
}
