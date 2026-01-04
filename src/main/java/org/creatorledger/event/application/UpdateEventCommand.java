package org.creatorledger.event.application;

import org.creatorledger.event.api.EventId;

import java.time.LocalDate;

/**
 * Command to update an existing event.
 * <p>
 * This command is used by the application layer to encapsulate the intent
 * to update an event's details.
 * </p>
 *
 * @param eventId the ID of the event to update
 * @param date the new event date
 * @param clientName the new client name
 * @param description the new event description
 */
public record UpdateEventCommand(EventId eventId, LocalDate date, String clientName, String description) {

    /**
     * Creates a new UpdateEventCommand.
     *
     * @throws IllegalArgumentException if any field is null or invalid
     */
    public UpdateEventCommand {
        if (eventId == null) {
            throw new IllegalArgumentException("Event ID cannot be null");
        }
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        if (clientName == null || clientName.isBlank()) {
            throw new IllegalArgumentException("Client name cannot be null or blank");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be null or blank");
        }
    }

    @Override
    public String toString() {
        return "UpdateEventCommand[eventId=%s, date=%s, clientName=%s, description=%s]"
            .formatted(eventId, date, clientName, description);
    }
}
