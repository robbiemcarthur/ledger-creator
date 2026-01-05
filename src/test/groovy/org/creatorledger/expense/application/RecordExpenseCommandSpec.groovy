package org.creatorledger.expense.application

import org.creatorledger.expense.api.ExpenseCategory
import org.creatorledger.user.api.UserId
import spock.lang.Specification

import java.time.LocalDate

class RecordExpenseCommandSpec extends Specification {

    def "should create command with valid parameters"() {
        given: "valid expense command parameters"
        def userId = UserId.generate()
        def amount = "150.00"
        def currency = "GBP"
        def category = ExpenseCategory.EQUIPMENT
        def description = "MacBook Pro laptop"
        def incurredDate = LocalDate.of(2026, 1, 15)

        when: "creating the command"
        def command = new RecordExpenseCommand(userId, amount, currency, category, description, incurredDate)

        then: "the command should have all values"
        command.userId() == userId
        command.amount() == amount
        command.currency() == currency
        command.category() == category
        command.description() == description
        command.incurredDate() == incurredDate
    }

    def "should reject null userId"() {
        when: "creating command with null userId"
        new RecordExpenseCommand(null, "150.00", "GBP", ExpenseCategory.EQUIPMENT, "Description", LocalDate.now())

        then: "it should throw IllegalArgumentException"
        def exception = thrown(IllegalArgumentException)
        exception.message == "User ID cannot be null"
    }

    def "should reject null amount"() {
        given: "null amount"
        def userId = UserId.generate()

        when: "creating command with null amount"
        new RecordExpenseCommand(userId, null, "GBP", ExpenseCategory.EQUIPMENT, "Description", LocalDate.now())

        then: "it should throw IllegalArgumentException"
        def exception = thrown(IllegalArgumentException)
        exception.message == "Amount cannot be null"
    }

    def "should reject blank amount"() {
        given: "blank amount"
        def userId = UserId.generate()

        when: "creating command with blank amount"
        new RecordExpenseCommand(userId, "   ", "GBP", ExpenseCategory.EQUIPMENT, "Description", LocalDate.now())

        then: "it should throw IllegalArgumentException"
        def exception = thrown(IllegalArgumentException)
        exception.message == "Amount cannot be blank"
    }

    def "should reject null currency"() {
        given: "null currency"
        def userId = UserId.generate()

        when: "creating command with null currency"
        new RecordExpenseCommand(userId, "150.00", null, ExpenseCategory.EQUIPMENT, "Description", LocalDate.now())

        then: "it should throw IllegalArgumentException"
        def exception = thrown(IllegalArgumentException)
        exception.message == "Currency cannot be null"
    }

    def "should reject blank currency"() {
        given: "blank currency"
        def userId = UserId.generate()

        when: "creating command with blank currency"
        new RecordExpenseCommand(userId, "150.00", "   ", ExpenseCategory.EQUIPMENT, "Description", LocalDate.now())

        then: "it should throw IllegalArgumentException"
        def exception = thrown(IllegalArgumentException)
        exception.message == "Currency cannot be blank"
    }

    def "should reject null category"() {
        given: "null category"
        def userId = UserId.generate()

        when: "creating command with null category"
        new RecordExpenseCommand(userId, "150.00", "GBP", null, "Description", LocalDate.now())

        then: "it should throw IllegalArgumentException"
        def exception = thrown(IllegalArgumentException)
        exception.message == "Category cannot be null"
    }

    def "should reject null description"() {
        given: "null description"
        def userId = UserId.generate()

        when: "creating command with null description"
        new RecordExpenseCommand(userId, "150.00", "GBP", ExpenseCategory.EQUIPMENT, null, LocalDate.now())

        then: "it should throw IllegalArgumentException"
        def exception = thrown(IllegalArgumentException)
        exception.message == "Description cannot be null"
    }

    def "should reject blank description"() {
        given: "blank description"
        def userId = UserId.generate()

        when: "creating command with blank description"
        new RecordExpenseCommand(userId, "150.00", "GBP", ExpenseCategory.EQUIPMENT, "   ", LocalDate.now())

        then: "it should throw IllegalArgumentException"
        def exception = thrown(IllegalArgumentException)
        exception.message == "Description cannot be blank"
    }

    def "should reject null incurredDate"() {
        given: "null incurredDate"
        def userId = UserId.generate()

        when: "creating command with null incurredDate"
        new RecordExpenseCommand(userId, "150.00", "GBP", ExpenseCategory.EQUIPMENT, "Description", null)

        then: "it should throw IllegalArgumentException"
        def exception = thrown(IllegalArgumentException)
        exception.message == "Incurred date cannot be null"
    }
}
