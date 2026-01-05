package org.creatorledger.expense.application

import org.creatorledger.expense.api.ExpenseCategory
import org.creatorledger.expense.api.ExpenseId
import spock.lang.Specification

import java.time.LocalDate

class UpdateExpenseCommandSpec extends Specification {

    def "should create command with valid parameters"() {
        given: "valid update command parameters"
        def expenseId = ExpenseId.generate()
        def amount = "200.00"
        def currency = "GBP"
        def category = ExpenseCategory.SOFTWARE
        def description = "Adobe Creative Cloud"
        def incurredDate = LocalDate.of(2026, 2, 1)

        when: "creating the command"
        def command = new UpdateExpenseCommand(expenseId, amount, currency, category, description, incurredDate)

        then: "the command should have all values"
        command.expenseId() == expenseId
        command.amount() == amount
        command.currency() == currency
        command.category() == category
        command.description() == description
        command.incurredDate() == incurredDate
    }

    def "should reject null expenseId"() {
        when: "creating command with null expenseId"
        new UpdateExpenseCommand(null, "200.00", "GBP", ExpenseCategory.SOFTWARE, "Description", LocalDate.now())

        then: "it should throw IllegalArgumentException"
        def exception = thrown(IllegalArgumentException)
        exception.message == "Expense ID cannot be null"
    }

    def "should reject null amount"() {
        given: "null amount"
        def expenseId = ExpenseId.generate()

        when: "creating command with null amount"
        new UpdateExpenseCommand(expenseId, null, "GBP", ExpenseCategory.SOFTWARE, "Description", LocalDate.now())

        then: "it should throw IllegalArgumentException"
        def exception = thrown(IllegalArgumentException)
        exception.message == "Amount cannot be null"
    }

    def "should reject blank amount"() {
        given: "blank amount"
        def expenseId = ExpenseId.generate()

        when: "creating command with blank amount"
        new UpdateExpenseCommand(expenseId, "   ", "GBP", ExpenseCategory.SOFTWARE, "Description", LocalDate.now())

        then: "it should throw IllegalArgumentException"
        def exception = thrown(IllegalArgumentException)
        exception.message == "Amount cannot be blank"
    }

    def "should reject null currency"() {
        given: "null currency"
        def expenseId = ExpenseId.generate()

        when: "creating command with null currency"
        new UpdateExpenseCommand(expenseId, "200.00", null, ExpenseCategory.SOFTWARE, "Description", LocalDate.now())

        then: "it should throw IllegalArgumentException"
        def exception = thrown(IllegalArgumentException)
        exception.message == "Currency cannot be null"
    }

    def "should reject blank currency"() {
        given: "blank currency"
        def expenseId = ExpenseId.generate()

        when: "creating command with blank currency"
        new UpdateExpenseCommand(expenseId, "200.00", "   ", ExpenseCategory.SOFTWARE, "Description", LocalDate.now())

        then: "it should throw IllegalArgumentException"
        def exception = thrown(IllegalArgumentException)
        exception.message == "Currency cannot be blank"
    }

    def "should reject null category"() {
        given: "null category"
        def expenseId = ExpenseId.generate()

        when: "creating command with null category"
        new UpdateExpenseCommand(expenseId, "200.00", "GBP", null, "Description", LocalDate.now())

        then: "it should throw IllegalArgumentException"
        def exception = thrown(IllegalArgumentException)
        exception.message == "Category cannot be null"
    }

    def "should reject null description"() {
        given: "null description"
        def expenseId = ExpenseId.generate()

        when: "creating command with null description"
        new UpdateExpenseCommand(expenseId, "200.00", "GBP", ExpenseCategory.SOFTWARE, null, LocalDate.now())

        then: "it should throw IllegalArgumentException"
        def exception = thrown(IllegalArgumentException)
        exception.message == "Description cannot be null"
    }

    def "should reject blank description"() {
        given: "blank description"
        def expenseId = ExpenseId.generate()

        when: "creating command with blank description"
        new UpdateExpenseCommand(expenseId, "200.00", "GBP", ExpenseCategory.SOFTWARE, "   ", LocalDate.now())

        then: "it should throw IllegalArgumentException"
        def exception = thrown(IllegalArgumentException)
        exception.message == "Description cannot be blank"
    }

    def "should reject null incurredDate"() {
        given: "null incurredDate"
        def expenseId = ExpenseId.generate()

        when: "creating command with null incurredDate"
        new UpdateExpenseCommand(expenseId, "200.00", "GBP", ExpenseCategory.SOFTWARE, "Description", null)

        then: "it should throw IllegalArgumentException"
        def exception = thrown(IllegalArgumentException)
        exception.message == "Incurred date cannot be null"
    }
}
