package org.creatorledger.income.api;

import org.creatorledger.user.api.UserId;

import java.time.LocalDate;
import java.util.List;

/**
 * Public API for querying income data across module boundaries.
 * <p>
 * This interface is part of the income module's published language,
 * allowing other modules (e.g., reporting) to query income data
 * without violating Spring Modulith module boundaries.
 * </p>
 */
public interface IncomeQueryService {

    /**
     * Find all income records for a user within a date range.
     *
     * @param userId the user ID
     * @param startDate the start date (inclusive)
     * @param endDate the end date (inclusive)
     * @return list of income data
     */
    List<IncomeData> findByUserIdAndDateRange(UserId userId, LocalDate startDate, LocalDate endDate);
}
