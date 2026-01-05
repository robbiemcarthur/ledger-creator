package org.creatorledger.expense.application;

import org.creatorledger.expense.api.ExpenseCategory;
import org.creatorledger.user.api.UserId;

import java.time.LocalDate;

public record RecordExpenseCommand(
    UserId userId,
    String amount,
    String currency,
    ExpenseCategory category,
    String description,
    LocalDate incurredDate
) {

    public RecordExpenseCommand {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        if (amount == null) {
            throw new IllegalArgumentException("Amount cannot be null");
        }
        if (amount.isBlank()) {
            throw new IllegalArgumentException("Amount cannot be blank");
        }
        if (currency == null) {
            throw new IllegalArgumentException("Currency cannot be null");
        }
        if (currency.isBlank()) {
            throw new IllegalArgumentException("Currency cannot be blank");
        }
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
        if (description == null) {
            throw new IllegalArgumentException("Description cannot be null");
        }
        if (description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be blank");
        }
        if (incurredDate == null) {
            throw new IllegalArgumentException("Incurred date cannot be null");
        }
    }
}
