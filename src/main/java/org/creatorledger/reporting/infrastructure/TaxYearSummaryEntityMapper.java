package org.creatorledger.reporting.infrastructure;

import org.creatorledger.common.Money;
import org.creatorledger.expense.api.ExpenseCategory;
import org.creatorledger.reporting.api.TaxYearSummaryId;
import org.creatorledger.reporting.domain.CategoryTotals;
import org.creatorledger.reporting.domain.TaxYear;
import org.creatorledger.reporting.domain.TaxYearSummary;
import org.creatorledger.user.api.UserId;

import java.util.HashMap;
import java.util.Map;

public class TaxYearSummaryEntityMapper {

    public static TaxYearSummaryJpaEntity toEntity(final TaxYearSummary summary) {
        if (summary == null) {
            return null;
        }
        final String categoryTotalsJson = serializeCategoryTotals(summary.categoryTotals());

        return new TaxYearSummaryJpaEntity(
                summary.id().value(),
                summary.userId().value(),
                summary.taxYear().startYear(),
                summary.totalIncome().amount(),
                summary.totalIncome().currency(),
                summary.totalExpenses().amount(),
                summary.totalExpenses().currency(),
                categoryTotalsJson
        );
    }

    public static TaxYearSummary toDomain(final TaxYearSummaryJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        final Money totalIncome = Money.of(entity.getTotalIncomeAmount(), entity.getTotalIncomeCurrency());
        final Money totalExpenses = Money.of(entity.getTotalExpensesAmount(), entity.getTotalExpensesCurrency());
        final CategoryTotals categoryTotals = deserializeCategoryTotals(entity.getCategoryTotalsJson());

        return TaxYearSummary.generate(
                TaxYearSummaryId.of(entity.getId()),
                UserId.of(entity.getUserId()),
                TaxYear.of(entity.getTaxYearStart()),
                totalIncome,
                totalExpenses,
                categoryTotals
        );
    }

    private static String serializeCategoryTotals(final CategoryTotals categoryTotals) {
        if (categoryTotals.isEmpty()) {
            return "{}";
        }
        final StringBuilder json = new StringBuilder("{");
        boolean first = true;
        for (final ExpenseCategory category : categoryTotals.categories()) {
            if (!first) {
                json.append(",");
            }
            final Money amount = categoryTotals.totalFor(category);
            json.append("\"").append(category.name()).append("\":");
            json.append("\"").append(amount.amount()).append(" ").append(amount.currency()).append("\"");
            first = false;
        }
        json.append("}");
        return json.toString();
    }

    private static CategoryTotals deserializeCategoryTotals(final String json) {
        if (json == null || json.equals("{}")) {
            return CategoryTotals.empty();
        }

        final Map<ExpenseCategory, Money> totals = new HashMap<>();
        final String content = json.substring(1, json.length() - 1);
        if (content.isEmpty()) {
            return CategoryTotals.empty();
        }

        final String[] pairs = content.split(",");
        for (final String pair : pairs) {
            final String[] keyValue = pair.split(":");
            final String categoryName = keyValue[0].replace("\"", "").trim();
            final String amountStr = keyValue[1].replace("\"", "").trim();
            final String[] amountParts = amountStr.split(" ");

            final ExpenseCategory category = ExpenseCategory.valueOf(categoryName);
            final Money money = Money.of(new java.math.BigDecimal(amountParts[0]), amountParts[1]);
            totals.put(category, money);
        }

        return CategoryTotals.of(totals);
    }
}
