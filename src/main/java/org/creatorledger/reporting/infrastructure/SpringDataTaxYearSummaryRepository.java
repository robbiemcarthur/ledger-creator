package org.creatorledger.reporting.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface SpringDataTaxYearSummaryRepository extends JpaRepository<TaxYearSummaryJpaEntity, UUID> {
}
