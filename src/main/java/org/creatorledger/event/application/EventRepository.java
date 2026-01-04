package org.creatorledger.event.application;

import org.creatorledger.event.domain.Event;
import org.creatorledger.event.api.EventId;

import java.util.Optional;

/**
 * Repository interface for Event aggregate persistence.
 * <p>
 * This is a port (in hexagonal architecture) that defines the contract
 * for persisting and retrieving Event aggregates. The infrastructure layer
 * provides the actual implementation (adapter).
 * </p>
 */
public interface EventRepository {

    /**
     * Saves an event to the repository.
     * If the event already exists (based on ID), it will be updated.
     *
     * @param event the event to save
     * @return the saved event
     */
    Event save(Event event);

    /**
     * Finds an event by its unique identifier.
     *
     * @param id the event ID
     * @return an Optional containing the event if found, empty otherwise
     */
    Optional<Event> findById(EventId id);

    /**
     * Checks if an event exists with the given ID.
     *
     * @param id the event ID
     * @return true if an event exists, false otherwise
     */
    boolean existsById(EventId id);

    /**
     * Deletes an event from the repository.
     *
     * @param event the event to delete
     */
    void delete(Event event);
}
