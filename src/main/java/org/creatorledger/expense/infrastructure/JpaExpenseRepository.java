package org.creatorledger.expense.infrastructure;

import org.creatorledger.expense.api.ExpenseId;
import org.creatorledger.expense.application.ExpenseRepository;
import org.creatorledger.expense.domain.Expense;
import org.creatorledger.user.api.UserId;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaExpenseRepository implements ExpenseRepository {

    private final SpringDataExpenseRepository springDataRepository;

    public JpaExpenseRepository(final SpringDataExpenseRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public Expense save(final Expense expense) {
        final ExpenseJpaEntity entity = ExpenseEntityMapper.toEntity(expense);
        final ExpenseJpaEntity saved = springDataRepository.save(entity);
        return ExpenseEntityMapper.toDomain(saved);
    }

    @Override
    public Optional<Expense> findById(final ExpenseId id) {
        return springDataRepository.findById(id.value())
                .map(ExpenseEntityMapper::toDomain);
    }

    @Override
    public boolean existsById(final ExpenseId id) {
        return springDataRepository.existsById(id.value());
    }

    @Override
    public void delete(final Expense expense) {
        springDataRepository.deleteById(expense.id().value());
    }

    @Override
    public List<Expense> findByUserIdAndDateRange(final UserId userId, final LocalDate startDate, final LocalDate endDate) {
        return springDataRepository.findByUserIdAndIncurredDateBetween(
                        userId.value(),
                        startDate,
                        endDate
                )
                .stream()
                .map(ExpenseEntityMapper::toDomain)
                .toList();
    }
}
