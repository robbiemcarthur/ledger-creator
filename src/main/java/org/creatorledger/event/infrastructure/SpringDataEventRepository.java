package org.creatorledger.event.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/**
 * Spring Data JPA repository interface for EventJpaEntity.
 * <p>
 * Spring Data automatically provides the implementation at runtime.
 * </p>
 */
interface SpringDataEventRepository extends JpaRepository<EventJpaEntity, UUID> {
}
