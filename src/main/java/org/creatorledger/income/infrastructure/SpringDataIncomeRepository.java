package org.creatorledger.income.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

interface SpringDataIncomeRepository extends JpaRepository<IncomeJpaEntity, UUID> {

    @Query("SELECT i FROM IncomeJpaEntity i WHERE i.userId = :userId AND i.receivedDate BETWEEN :startDate AND :endDate")
    List<IncomeJpaEntity> findByUserIdAndReceivedDateBetween(
            @Param("userId") UUID userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
