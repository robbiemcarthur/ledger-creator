/**
 * Reporting module - tax year summaries and financial reporting.
 * <p>
 * This module aggregates income and expense data to generate
 * UK tax year summaries for self-employed creatives.
 * </p>
 * <p>
 * <strong>Public API:</strong> The {@code api} package contains {@code TaxYearSummaryId}
 * which other modules can safely depend on.
 * </p>
 * <p>
 * <strong>Dependencies:</strong>
 * </p>
 * <ul>
 *   <li>{@code common} - Money value object</li>
 *   <li>{@code user::api} - UserId for report ownership</li>
 *   <li>{@code income::api} - IncomeQueryService for aggregation</li>
 *   <li>{@code expense::api} - ExpenseQueryService and ExpenseCategory</li>
 * </ul>
 */
@org.springframework.modulith.ApplicationModule(
    displayName = "Reporting",
    allowedDependencies = {"common", "user :: api", "income :: api", "expense :: api"}
)
package org.creatorledger.reporting;
