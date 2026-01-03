package org.creatorledger.reporting.domain;

import java.time.LocalDate;

/**
 * Value object representing a UK tax year.
 * <p>
 * UK tax years run from April 6th of one year to April 5th of the following year.
 * For example, tax year 2024-2025 runs from April 6, 2024 to April 5, 2025.
 * </p>
 *
 * @param startYear the year in which the tax year starts (e.g., 2024 for 2024-2025)
 */
public record TaxYear(Integer startYear) {

    private static final int APRIL = 4;
    private static final int TAX_YEAR_START_DAY = 6;
    private static final int TAX_YEAR_END_DAY = 5;
    private static final int MAX_YEARS_IN_PAST = 10;
    private static final int MAX_YEARS_IN_FUTURE = 5;

    /**
     * Creates a new tax year for the given start year.
     *
     * @param startYear the year in which the tax year starts (e.g., 2024 for 2024-2025)
     * @return a new TaxYear instance
     * @throws IllegalArgumentException if startYear is null or out of valid range
     */
    public static TaxYear of(Integer startYear) {
        validateStartYear(startYear);
        return new TaxYear(startYear);
    }

    /**
     * Returns the start date of this tax year (April 6th).
     *
     * @return the start date
     */
    public LocalDate startDate() {
        return LocalDate.of(startYear, APRIL, TAX_YEAR_START_DAY);
    }

    /**
     * Returns the end date of this tax year (April 5th of the following year).
     *
     * @return the end date
     */
    public LocalDate endDate() {
        return LocalDate.of(startYear + 1, APRIL, TAX_YEAR_END_DAY);
    }

    /**
     * Checks if the given date falls within this tax year.
     *
     * @param date the date to check
     * @return true if the date is within this tax year (inclusive), false otherwise
     */
    public boolean contains(LocalDate date) {
        if (date == null) {
            return false;
        }
        return !date.isBefore(startDate()) && !date.isAfter(endDate());
    }

    @Override
    public String toString() {
        return "TaxYear[%d-%d]".formatted(startYear, startYear + 1);
    }

    private static void validateStartYear(Integer startYear) {
        if (startYear == null) {
            throw new IllegalArgumentException("Start year cannot be null");
        }

        int currentYear = LocalDate.now().getYear();
        int minYear = currentYear - MAX_YEARS_IN_PAST;
        int maxYear = currentYear + MAX_YEARS_IN_FUTURE;

        if (startYear < minYear || startYear > maxYear) {
            throw new IllegalArgumentException(
                    "Start year must be within %d years in the past and %d years in the future (got %d)"
                            .formatted(MAX_YEARS_IN_PAST, MAX_YEARS_IN_FUTURE, startYear)
            );
        }
    }
}
