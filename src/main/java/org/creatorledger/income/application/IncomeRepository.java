package org.creatorledger.income.application;

import org.creatorledger.income.domain.Income;
import org.creatorledger.income.api.IncomeId;
import org.creatorledger.user.api.UserId;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IncomeRepository {
    Income save(Income income);
    Optional<Income> findById(IncomeId id);
    boolean existsById(IncomeId id);
    void delete(Income income);
    List<Income> findByUserIdAndDateRange(UserId userId, LocalDate startDate, LocalDate endDate);
}
