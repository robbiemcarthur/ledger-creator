package org.creatorledger.expense.application;

import org.creatorledger.expense.domain.Expense;
import org.creatorledger.expense.api.ExpenseId;
import org.creatorledger.user.api.UserId;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository {
    Expense save(Expense expense);
    Optional<Expense> findById(ExpenseId id);
    boolean existsById(ExpenseId id);
    void delete(Expense expense);
    List<Expense> findByUserIdAndDateRange(UserId userId, LocalDate startDate, LocalDate endDate);
}
