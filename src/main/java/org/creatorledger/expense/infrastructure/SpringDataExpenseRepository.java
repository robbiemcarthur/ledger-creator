package org.creatorledger.expense.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

interface SpringDataExpenseRepository extends JpaRepository<ExpenseJpaEntity, UUID> {

    @Query("SELECT e FROM ExpenseJpaEntity e WHERE e.userId = :userId AND e.incurredDate BETWEEN :startDate AND :endDate")
    List<ExpenseJpaEntity> findByUserIdAndIncurredDateBetween(
            @Param("userId") UUID userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
