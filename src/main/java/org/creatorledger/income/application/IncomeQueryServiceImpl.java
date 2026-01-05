package org.creatorledger.income.application;

import org.creatorledger.income.api.IncomeData;
import org.creatorledger.income.api.IncomeQueryService;
import org.creatorledger.user.api.UserId;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class IncomeQueryServiceImpl implements IncomeQueryService {

    private final IncomeRepository incomeRepository;

    public IncomeQueryServiceImpl(final IncomeRepository incomeRepository) {
        this.incomeRepository = incomeRepository;
    }

    @Override
    public List<IncomeData> findByUserIdAndDateRange(final UserId userId, final LocalDate startDate, final LocalDate endDate) {
        return incomeRepository.findByUserIdAndDateRange(userId, startDate, endDate)
                .stream()
                .map(IncomeData::from)
                .toList();
    }
}
