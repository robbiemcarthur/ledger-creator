package org.creatorledger.event.infrastructure;

import org.creatorledger.event.application.EventRepository;
import org.creatorledger.event.domain.Event;
import org.creatorledger.event.api.EventId;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * JPA implementation of the EventRepository.
 * <p>
 * This is an adapter that bridges the application layer's repository port
 * with Spring Data JPA infrastructure. It handles conversion between domain
 * objects and JPA entities using the EventEntityMapper.
 * </p>
 */
@Repository
public class JpaEventRepository implements EventRepository {

    private final SpringDataEventRepository springDataRepository;

    public JpaEventRepository(SpringDataEventRepository springDataRepository) {
        this.springDataRepository = springDataRepository;
    }

    @Override
    public Event save(Event event) {
        EventJpaEntity entity = EventEntityMapper.toEntity(event);
        EventJpaEntity saved = springDataRepository.save(entity);
        return EventEntityMapper.toDomain(saved);
    }

    @Override
    public Optional<Event> findById(EventId id) {
        return springDataRepository.findById(id.value())
                .map(EventEntityMapper::toDomain);
    }

    @Override
    public boolean existsById(EventId id) {
        return springDataRepository.existsById(id.value());
    }

    @Override
    public void delete(Event event) {
        springDataRepository.deleteById(event.id().value());
    }
}
