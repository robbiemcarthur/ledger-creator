package org.creatorledger.income.infrastructure.web;

import java.time.LocalDate;

/**
 * Request DTO for income update endpoint.
 * <p>
 * This is a presentation layer concern for HTTP API communication.
 * It is converted to UpdateIncomeCommand for application layer processing.
 * </p>
 *
 * @param amount the new income amount as a string
 * @param currency the currency code (e.g., "GBP", "USD")
 * @param description the new income description
 * @param receivedDate the new received date
 */
public record UpdateIncomeRequest(
        String amount,
        String currency,
        String description,
        LocalDate receivedDate
) {

    /**
     * Creates a new UpdateIncomeRequest.
     *
     * @throws IllegalArgumentException if any field is null or invalid
     */
    public UpdateIncomeRequest {
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
