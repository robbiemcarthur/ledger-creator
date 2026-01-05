package org.creatorledger.expense.application;

import org.creatorledger.expense.domain.Expense;
import org.creatorledger.expense.api.ExpenseId;

import java.util.Optional;

public interface ExpenseRepository {
    Expense save(Expense expense);
    Optional<Expense> findById(ExpenseId id);
    boolean existsById(ExpenseId id);
    void delete(Expense expense);
}
