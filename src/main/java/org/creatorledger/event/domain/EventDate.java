package org.creatorledger.event.domain;

import java.time.LocalDate;

/**
 * Value object representing an event date.
 * Validates that dates are within reasonable business limits.
 */
public record EventDate(LocalDate value) {

    private static final int MAX_YEARS_IN_PAST = 10;
    private static final int MAX_YEARS_IN_FUTURE = 5;

    /**
     * Creates an EventDate from a LocalDate.
     * Validates that the date is within reasonable business limits.
     *
     * @param value the date value
     * @return a new EventDate
     * @throws IllegalArgumentException if value is null or outside valid range
     */
    public static EventDate of(LocalDate value) {
        if (value == null) {
            throw new IllegalArgumentException("EventDate cannot be null");
        }

        LocalDate now = LocalDate.now();
        LocalDate earliestValid = now.minusYears(MAX_YEARS_IN_PAST);
        LocalDate latestValid = now.plusYears(MAX_YEARS_IN_FUTURE);

        if (value.isBefore(earliestValid)) {
            throw new IllegalArgumentException(
                "EventDate cannot be more than " + MAX_YEARS_IN_PAST + " years in the past"
            );
        }

        if (value.isAfter(latestValid)) {
            throw new IllegalArgumentException(
                "EventDate cannot be more than " + MAX_YEARS_IN_FUTURE + " years in the future"
            );
        }

        return new EventDate(value);
    }

    /**
     * Creates an EventDate for today.
     *
     * @return a new EventDate with today's date
     */
    public static EventDate today() {
        return new EventDate(LocalDate.now());
    }

    /**
     * Checks if this event date is before another event date.
     *
     * @param other the other event date
     * @return true if this date is before the other date
     */
    public boolean isBefore(EventDate other) {
        return value.isBefore(other.value);
    }

    /**
     * Checks if this event date is after another event date.
     *
     * @param other the other event date
     * @return true if this date is after the other date
     */
    public boolean isAfter(EventDate other) {
        return value.isAfter(other.value);
    }

    @Override
    public String toString() {
        return "EventDate[" + value + "]";
    }
}
