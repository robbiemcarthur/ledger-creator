package org.creatorledger.expense.api;

import org.creatorledger.user.api.UserId;

import java.time.LocalDate;
import java.util.List;

/**
 * Public API for querying expense data across module boundaries.
 * <p>
 * This interface is part of the expense module's published language,
 * allowing other modules (e.g., reporting) to query expense data
 * without violating Spring Modulith module boundaries.
 * </p>
 */
public interface ExpenseQueryService {

    /**
     * Find all expense records for a user within a date range.
     *
     * @param userId the user ID
     * @param startDate the start date (inclusive)
     * @param endDate the end date (inclusive)
     * @return list of expense data
     */
    List<ExpenseData> findByUserIdAndDateRange(UserId userId, LocalDate startDate, LocalDate endDate);
}
