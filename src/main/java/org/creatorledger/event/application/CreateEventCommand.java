package org.creatorledger.event.application;

import java.time.LocalDate;

/**
 * Command to create a new event.
 * <p>
 * This command is used by the application layer to encapsulate the intent
 * to create a new event in the system.
 * </p>
 *
 * @param date the event date
 * @param clientName the client name
 * @param description the event description
 */
public record CreateEventCommand(LocalDate date, String clientName, String description) {

    /**
     * Creates a new CreateEventCommand.
     *
     * @throws IllegalArgumentException if any field is null or invalid
     */
    public CreateEventCommand {
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
        return "CreateEventCommand[date=%s, clientName=%s, description=%s]"
            .formatted(date, clientName, description);
    }
}
