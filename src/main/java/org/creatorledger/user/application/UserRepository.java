package org.creatorledger.user.application;

import org.creatorledger.user.domain.User;
import org.creatorledger.user.api.UserId;

import java.util.Optional;

/**
 * Repository interface for User aggregate persistence.
 * <p>
 * This is a port (in hexagonal architecture) that defines the contract
 * for persisting and retrieving User aggregates. The infrastructure layer
 * provides the actual implementation (adapter).
 * </p>
 */
public interface UserRepository {
    User save(User user);
    Optional<User> findById(UserId id);
    boolean existsById(UserId id);
    void delete(User user);
}
