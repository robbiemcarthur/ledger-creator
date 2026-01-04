/**
 * Expense module - manages business expenses and deductions.
 * <p>
 * This module provides expense recording and categorization capabilities
 * following HMRC guidelines for self-employed creatives.
 * </p>
 * <p>
 * <strong>Public API:</strong> The {@code api} package contains types that
 * other modules can safely depend on, such as {@code ExpenseCategory} and {@code ExpenseId}.
 * </p>
 * <p>
 * <strong>Dependencies:</strong>
 * </p>
 * <ul>
 *   <li>{@code common} - Money value object</li>
 *   <li>{@code user::api} - UserId for expense ownership</li>
 * </ul>
 */
@org.springframework.modulith.ApplicationModule(
    displayName = "Expense",
    allowedDependencies = {"common", "user :: api"}
)
package org.creatorledger.expense;
