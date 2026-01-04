package org.creatorledger.event.application;

import org.creatorledger.event.api.EventId;
import org.creatorledger.event.domain.ClientName;
import org.creatorledger.event.domain.Event;
import org.creatorledger.event.domain.EventDate;

import java.util.Optional;

/**
 * Application service for event-related use cases.
 * <p>
 * This service orchestrates domain operations and coordinates with the repository
 * to persist changes. It serves as the entry point for event-related commands
 * and queries from the presentation layer.
 * </p>
 * <p>
 * Note: @Service annotation will be added when infrastructure layer is implemented.
 * </p>
 */
public class EventApplicationService {

    private final EventRepository eventRepository;

    public EventApplicationService(EventRepository eventRepository) {
        if (eventRepository == null) {
            throw new IllegalArgumentException("Event repository cannot be null");
        }
        this.eventRepository = eventRepository;
    }

    /**
     * Creates a new event in the system.
     *
     * @param command the create command containing event details
     * @return the ID of the newly created event
     * @throws IllegalArgumentException if command is null
     */
    public EventId create(CreateEventCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("Command cannot be null");
        }

        EventDate date = EventDate.of(command.date());
        ClientName clientName = ClientName.of(command.clientName());
        Event event = Event.create(date, clientName, command.description());

        eventRepository.save(event);

        return event.id();
    }

    /**
     * Updates an existing event with new details.
     *
     * @param command the update command containing event ID and new details
     * @throws IllegalArgumentException if command is null
     * @throws IllegalStateException if event is not found
     */
    public void update(UpdateEventCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("Command cannot be null");
        }

        Event existingEvent = eventRepository.findById(command.eventId())
            .orElseThrow(() -> new IllegalStateException("Event not found: " + command.eventId()));

        EventDate date = EventDate.of(command.date());
        ClientName clientName = ClientName.of(command.clientName());
        Event updatedEvent = existingEvent.update(date, clientName, command.description());

        eventRepository.save(updatedEvent);
    }

    /**
     * Finds an event by its unique identifier.
     *
     * @param eventId the event ID
     * @return an Optional containing the event if found, empty otherwise
     * @throws IllegalArgumentException if eventId is null
     */
    public Optional<Event> findById(EventId eventId) {
        if (eventId == null) {
            throw new IllegalArgumentException("Event ID cannot be null");
        }
        return eventRepository.findById(eventId);
    }

    /**
     * Checks if an event exists with the given ID.
     *
     * @param eventId the event ID
     * @return true if an event exists, false otherwise
     * @throws IllegalArgumentException if eventId is null
     */
    public boolean existsById(EventId eventId) {
        if (eventId == null) {
            throw new IllegalArgumentException("Event ID cannot be null");
        }
        return eventRepository.existsById(eventId);
    }
}
