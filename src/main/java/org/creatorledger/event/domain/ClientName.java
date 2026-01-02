package org.creatorledger.event.domain;

/**
 * Value object representing a client name.
 * Validates length and ensures the name is not blank.
 */
public record ClientName(String value) {

    private static final int MAX_LENGTH = 200;

    /**
     * Creates a ClientName from a string.
     * Trims whitespace and validates length.
     *
     * @param value the client name
     * @return a new ClientName
     * @throws IllegalArgumentException if value is null, blank, or too long
     */
    public static ClientName of(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("ClientName cannot be null or blank");
        }

        String trimmed = value.trim();

        if (trimmed.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("ClientName cannot exceed " + MAX_LENGTH + " characters");
        }

        return new ClientName(trimmed);
    }

    @Override
    public String toString() {
        return "ClientName[" + value + "]";
    }
}
