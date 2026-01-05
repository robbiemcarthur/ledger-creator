package org.creatorledger.reporting.infrastructure;

import org.creatorledger.reporting.api.TaxYearSummaryId;
import org.creatorledger.reporting.application.TaxYearSummaryRepository;
import org.creatorledger.reporting.domain.TaxYearSummary;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaTaxYearSummaryRepository implements TaxYearSummaryRepository {

    private final SpringDataTaxYearSummaryRepository springDataRepository;

    public JpaTaxYearSummaryRepository(final SpringDataTaxYearSummaryRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public TaxYearSummary save(final TaxYearSummary summary) {
        final TaxYearSummaryJpaEntity entity = TaxYearSummaryEntityMapper.toEntity(summary);
        final TaxYearSummaryJpaEntity saved = springDataRepository.save(entity);
        return TaxYearSummaryEntityMapper.toDomain(saved);
    }

    @Override
    public Optional<TaxYearSummary> findById(final TaxYearSummaryId id) {
        return springDataRepository.findById(id.value())
                .map(TaxYearSummaryEntityMapper::toDomain);
    }

    @Override
    public boolean existsById(final TaxYearSummaryId id) {
        return springDataRepository.existsById(id.value());
    }

    @Override
    public void delete(final TaxYearSummary summary) {
        springDataRepository.deleteById(summary.id().value());
    }
}
