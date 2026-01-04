package org.creatorledger.income.infrastructure.web;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Request DTO for income recording endpoint.
 * <p>
 * This is a presentation layer concern for HTTP API communication.
 * It is converted to RecordIncomeCommand for application layer processing.
 * </p>
 *
 * @param userId the user ID
 * @param eventId the associated event ID
 * @param amount the income amount as a string
 * @param currency the currency code (e.g., "GBP", "USD")
 * @param description the income description
 * @param receivedDate the date income was received
 */
public record RecordIncomeRequest(
        UUID userId,
        UUID eventId,
        String amount,
        String currency,
        String description,
        LocalDate receivedDate
) {

    /**
     * Creates a new RecordIncomeRequest.
     *
     * @throws IllegalArgumentException if any field is null or invalid
     */
    public RecordIncomeRequest {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (eventId == null) {
            throw new IllegalArgumentException("Event ID cannot be null");
        }
        if (amount == null || amount.isBlank()) {
            throw new IllegalArgumentException("Amount cannot be null or blank");
        }
        if (currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("Currency cannot be null or blank");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be null or blank");
        }
        if (receivedDate == null) {
            throw new IllegalArgumentException("Received date cannot be null");
        }
    }
}
