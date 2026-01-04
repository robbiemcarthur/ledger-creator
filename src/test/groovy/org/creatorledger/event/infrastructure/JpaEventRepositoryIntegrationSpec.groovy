package org.creatorledger.event.infrastructure

import org.creatorledger.event.api.EventId
import org.creatorledger.event.domain.ClientName
import org.creatorledger.event.domain.Event
import org.creatorledger.event.domain.EventDate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.transaction.annotation.Transactional
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.spock.Testcontainers
import spock.lang.Shared
import spock.lang.Specification
import java.time.LocalDate

@SpringBootTest
@Transactional
@Testcontainers
class JpaEventRepositoryIntegrationSpec extends Specification {

    @Shared
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test")

    @Autowired
    JpaEventRepository repository

    def "should save and retrieve an event"() {
        given: "a new event"
        def event = Event.create(
            EventDate.of(LocalDate.of(2026, 3, 15)),
            ClientName.of("Acme Corporation"),
            "Website redesign workshop"
        )

        when: "saving the event"
        def saved = repository.save(event)

        and: "retrieving the event by ID"
        def retrieved = repository.findById(event.id())

        then: "the event is persisted and retrieved correctly"
        saved != null
        saved.id() == event.id()
        saved.date() == event.date()
        saved.clientName() == event.clientName()
        saved.description() == event.description()

        and: "retrieved event matches saved event"
        retrieved.isPresent()
        retrieved.get().id() == event.id()
        retrieved.get().date() == event.date()
        retrieved.get().clientName() == event.clientName()
        retrieved.get().description() == event.description()
    }

    def "should return empty when event not found"() {
        given: "a non-existent event ID"
        def eventId = EventId.generate()

        when: "finding by ID"
        def result = repository.findById(eventId)

        then: "empty is returned"
        result.isEmpty()
    }

    def "should check event existence"() {
        given: "a saved event"
        def event = Event.create(
            EventDate.of(LocalDate.of(2026, 3, 15)),
            ClientName.of("Existence Corp"),
            "Test event"
        )
        repository.save(event)

        when: "checking if event exists"
        def exists = repository.existsById(event.id())

        then: "true is returned"
        exists
    }

    def "should return false for non-existent event"() {
        given: "a non-existent event ID"
        def eventId = EventId.generate()

        when: "checking existence"
        def exists = repository.existsById(eventId)

        then: "false is returned"
        !exists
    }

    def "should delete an event"() {
        given: "a saved event"
        def event = Event.create(
            EventDate.of(LocalDate.of(2026, 3, 15)),
            ClientName.of("Delete Corp"),
            "Event to delete"
        )
        repository.save(event)

        when: "deleting the event"
        repository.delete(event)

        and: "checking if event still exists"
        def exists = repository.existsById(event.id())

        then: "event is deleted"
        !exists
    }

    def "should update an existing event"() {
        given: "a saved event"
        def eventId = EventId.generate()
        def originalEvent = Event.create(
            eventId,
            EventDate.of(LocalDate.of(2026, 3, 15)),
            ClientName.of("Original Corp"),
            "Original description"
        )
        repository.save(originalEvent)

        when: "updating the event with new details"
        def updatedEvent = Event.create(
            eventId,
            EventDate.of(LocalDate.of(2026, 4, 20)),
            ClientName.of("Updated Corp"),
            "Updated description"
        )
        repository.save(updatedEvent)

        and: "retrieving the event"
        def retrieved = repository.findById(eventId)

        then: "the event is updated"
        retrieved.isPresent()
        retrieved.get().date() == EventDate.of(LocalDate.of(2026, 4, 20))
        retrieved.get().clientName() == ClientName.of("Updated Corp")
        retrieved.get().description() == "Updated description"
    }
}
