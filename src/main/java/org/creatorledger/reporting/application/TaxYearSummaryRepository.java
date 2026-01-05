package org.creatorledger.reporting.application;

import org.creatorledger.reporting.api.TaxYearSummaryId;
import org.creatorledger.reporting.domain.TaxYearSummary;

import java.util.Optional;

public interface TaxYearSummaryRepository {
    TaxYearSummary save(TaxYearSummary summary);
    Optional<TaxYearSummary> findById(TaxYearSummaryId id);
    boolean existsById(TaxYearSummaryId id);
    void delete(TaxYearSummary summary);
}
