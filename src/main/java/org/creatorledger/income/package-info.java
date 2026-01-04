/**
 * Income module - income recording and tracking.
 * <p>
 * This module handles income from various sources including
 * event fees, product sales, and other revenue streams.
 * </p>
 * <p>
 * <strong>Public API:</strong> The {@code api} package contains {@code IncomeId}
 * which other modules can safely depend on.
 * </p>
 * <p>
 * <strong>Dependencies:</strong>
 * </p>
 * <ul>
 *   <li>{@code common} - Money value object</li>
 *   <li>{@code user::api} - UserId for income ownership</li>
 *   <li>{@code event::api} - EventId for linking income to events</li>
 * </ul>
 */
@org.springframework.modulith.ApplicationModule(
    displayName = "Income",
    allowedDependencies = {"common", "user :: api", "event :: api"}
)
package org.creatorledger.income;
