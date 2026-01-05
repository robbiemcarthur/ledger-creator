package org.creatorledger.reporting.infrastructure;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "tax_year_summaries")
public class TaxYearSummaryJpaEntity {

    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "tax_year_start", nullable = false)
    private Integer taxYearStart;

    @Column(name = "total_income_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalIncomeAmount;

    @Column(name = "total_income_currency", nullable = false, length = 3)
    private String totalIncomeCurrency;

    @Column(name = "total_expenses_amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal totalExpensesAmount;

    @Column(name = "total_expenses_currency", nullable = false, length = 3)
    private String totalExpensesCurrency;

    @Column(name = "category_totals_json", columnDefinition = "TEXT")
    private String categoryTotalsJson;

    protected TaxYearSummaryJpaEntity() {
    }

    public TaxYearSummaryJpaEntity(
            UUID id,
            UUID userId,
            Integer taxYearStart,
            BigDecimal totalIncomeAmount,
            String totalIncomeCurrency,
            BigDecimal totalExpensesAmount,
            String totalExpensesCurrency,
            String categoryTotalsJson
    ) {
        this.id = id;
        this.userId = userId;
        this.taxYearStart = taxYearStart;
        this.totalIncomeAmount = totalIncomeAmount;
        this.totalIncomeCurrency = totalIncomeCurrency;
        this.totalExpensesAmount = totalExpensesAmount;
        this.totalExpensesCurrency = totalExpensesCurrency;
        this.categoryTotalsJson = categoryTotalsJson;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public Integer getTaxYearStart() {
        return taxYearStart;
    }

    public void setTaxYearStart(Integer taxYearStart) {
        this.taxYearStart = taxYearStart;
    }

    public BigDecimal getTotalIncomeAmount() {
        return totalIncomeAmount;
    }

    public void setTotalIncomeAmount(BigDecimal totalIncomeAmount) {
        this.totalIncomeAmount = totalIncomeAmount;
    }

    public String getTotalIncomeCurrency() {
        return totalIncomeCurrency;
    }

    public void setTotalIncomeCurrency(String totalIncomeCurrency) {
        this.totalIncomeCurrency = totalIncomeCurrency;
    }

    public BigDecimal getTotalExpensesAmount() {
        return totalExpensesAmount;
    }

    public void setTotalExpensesAmount(BigDecimal totalExpensesAmount) {
        this.totalExpensesAmount = totalExpensesAmount;
    }

    public String getTotalExpensesCurrency() {
        return totalExpensesCurrency;
    }

    public void setTotalExpensesCurrency(String totalExpensesCurrency) {
        this.totalExpensesCurrency = totalExpensesCurrency;
    }

    public String getCategoryTotalsJson() {
        return categoryTotalsJson;
    }

    public void setCategoryTotalsJson(String categoryTotalsJson) {
        this.categoryTotalsJson = categoryTotalsJson;
    }
}
