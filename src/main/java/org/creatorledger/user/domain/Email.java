package org.creatorledger.user.domain;

import java.util.regex.Pattern;

/**
 * Value object representing an email address.
 * Validates format and normalizes to lowercase.
 */
public record Email(String value) {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,}$"
    );

    /**
     * Creates an Email from a string.
     * Validates format and normalizes to lowercase.
     *
     * @param value the email address
     * @return a new Email
     * @throws IllegalArgumentException if value is null, blank, or invalid format
     */
    public static Email of(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }

        String normalized = value.trim().toLowerCase();

        if (!EMAIL_PATTERN.matcher(normalized).matches()) {
            throw new IllegalArgumentException("Invalid email format");
        }

        return new Email(normalized);
    }

    @Override
    public String toString() {
        return "Email[" + value + "]";
    }
}
