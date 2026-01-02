package org.creatorledger.expense.domain;

import org.creatorledger.common.domain.Money;
import org.creatorledger.user.domain.UserId;

import java.time.Instant;
import java.time.LocalDate;

public record ExpenseRecorded(
    ExpenseId expenseId,
    UserId userId,
    Money amount,
    ExpenseCategory category,
    String description,
    LocalDate incurredDate,
    Instant occurredAt
) {

    public static ExpenseRecorded of(ExpenseId expenseId, UserId userId, Money amount, ExpenseCategory category, String description, LocalDate incurredDate) {
        return new ExpenseRecorded(expenseId, userId, amount, category, description, incurredDate, Instant.now());
    }

    @Override
    public String toString() {
        return "ExpenseRecorded[expenseId=" + expenseId + ", userId=" + userId +
               ", amount=" + amount + ", category=" + category + ", description=" + description +
               ", incurredDate=" + incurredDate + ", occurredAt=" + occurredAt + "]";
    }
}
