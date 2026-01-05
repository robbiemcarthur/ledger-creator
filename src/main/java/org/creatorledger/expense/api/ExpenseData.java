package org.creatorledger.expense.api;

import org.creatorledger.common.Money;
import org.creatorledger.expense.domain.Expense;
import org.creatorledger.user.api.UserId;

import java.time.LocalDate;

/**
 * Data transfer object for Expense across module boundaries.
 * <p>
 * This is part of the expense module's published API, allowing
 * other modules to consume expense data without depending on
 * internal domain types.
 * </p>
 */
public record ExpenseData(
    ExpenseId id,
    UserId userId,
    Money amount,
    ExpenseCategory category,
    String description,
    LocalDate incurredDate
) {

    public static ExpenseData from(final Expense expense) {
        return new ExpenseData(
            expense.id(),
            expense.userId(),
            expense.amount(),
            expense.category(),
            expense.description(),
            expense.incurredDate()
        );
    }
}
