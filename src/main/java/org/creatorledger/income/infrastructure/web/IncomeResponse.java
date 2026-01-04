package org.creatorledger.income.infrastructure.web;

import org.creatorledger.income.domain.Income;

/**
 * Response DTO for income data in API responses.
 * <p>
 * This is a presentation layer concern for HTTP API communication.
 * It represents income data in a format suitable for API consumers.
 * </p>
 *
 * @param id the income's unique identifier
 * @param userId the user ID
 * @param eventId the associated event ID
 * @param amount the income amount
 * @param currency the currency code
 * @param description the income description
 * @param receivedDate the date income was received (ISO-8601 format)
 * @param status the payment status
 */
public record IncomeResponse(
        String id,
        String userId,
        String eventId,
        String amount,
        String currency,
        String description,
        String receivedDate,
        String status
) {

    /**
     * Creates an IncomeResponse from a domain Income.
     *
     * @param income the domain income
     * @return an IncomeResponse containing the income's data
     * @throws IllegalArgumentException if income is null
     */
    public static IncomeResponse from(Income income) {
        if (income == null) {
            throw new IllegalArgumentException("Income cannot be null");
        }
        return new IncomeResponse(
                income.id().value().toString(),
                income.userId().value().toString(),
                income.eventId().value().toString(),
                income.amount().amount().toString(),
                income.amount().currency(),
                income.description(),
                income.receivedDate().toString(),
                income.status().name()
        );
    }
}
