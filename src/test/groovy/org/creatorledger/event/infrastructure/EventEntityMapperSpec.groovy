package org.creatorledger.event.infrastructure

import org.creatorledger.event.domain.ClientName
import org.creatorledger.event.domain.Event
import org.creatorledger.event.domain.EventDate
import org.creatorledger.event.api.EventId
import spock.lang.Specification
import java.time.LocalDate

class EventEntityMapperSpec extends Specification {

    def "should map domain event to JPA entity"() {
        given: "a domain event"
        def eventId = EventId.generate()
        def eventDate = EventDate.of(LocalDate.of(2026, 3, 15))
        def clientName = ClientName.of("Acme Corporation")
        def description = "Website redesign workshop"
        def event = Event.create(eventId, eventDate, clientName, description)

        when: "mapping to entity"
        def entity = EventEntityMapper.toEntity(event)

        then: "entity is mapped correctly"
        entity != null
        entity.id == eventId.value()
        entity.eventDate == eventDate.value()
        entity.clientName == clientName.value()
        entity.description == description
    }

    def "should map JPA entity to domain event"() {
        given: "a JPA entity"
        def id = UUID.randomUUID()
        def eventDate = LocalDate.of(2026, 3, 15)
        def clientName = "Acme Corporation"
        def description = "Website redesign workshop"
        def entity = new EventJpaEntity(id, eventDate, clientName, description)

        when: "mapping to domain"
        def event = EventEntityMapper.toDomain(entity)

        then: "event is mapped correctly"
        event != null
        event.id().value() == id
        event.date().value() == eventDate
        event.clientName().value() == clientName
        event.description() == description
    }

    def "should handle null when mapping to entity"() {
        when: "mapping null to entity"
        def result = EventEntityMapper.toEntity(null)

        then: "null is returned"
        result == null
    }

    def "should handle null when mapping to domain"() {
        when: "mapping null to domain"
        def result = EventEntityMapper.toDomain(null)

        then: "null is returned"
        result == null
    }

    def "should round-trip correctly"() {
        given: "a domain event"
        def event = Event.create(
            EventDate.of(LocalDate.of(2026, 3, 15)),
            ClientName.of("Roundtrip Corp"),
            "Roundtrip test event"
        )

        when: "converting to entity and back"
        def entity = EventEntityMapper.toEntity(event)
        def reconstituted = EventEntityMapper.toDomain(entity)

        then: "event is preserved"
        reconstituted.id() == event.id()
        reconstituted.date() == event.date()
        reconstituted.clientName() == event.clientName()
        reconstituted.description() == event.description()
    }
}
