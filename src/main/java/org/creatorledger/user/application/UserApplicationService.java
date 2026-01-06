package org.creatorledger.user.application;

import org.creatorledger.user.api.UserId;
import org.creatorledger.user.domain.Email;
import org.creatorledger.user.domain.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Application service for user-related use cases.
 * <p>
 * This service orchestrates domain operations and coordinates with the repository
 * to persist changes. It serves as the entry point for user-related commands
 * and queries from the presentation layer.
 * </p>
 */
@Service
public class UserApplicationService {

    private final UserRepository userRepository;

    public UserApplicationService(UserRepository userRepository) {
        if (userRepository == null) {
            throw new IllegalArgumentException("User repository cannot be null");
        }
        this.userRepository = userRepository;
    }

    public UserId register(final RegisterUserCommand command) {
        if (command == null) {
            throw new IllegalArgumentException("Command cannot be null");
        }

        Email email = Email.of(command.email());
        User user = User.register(email);

        userRepository.save(user);

        return user.id();
    }

    public Optional<User> findById(final UserId userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return userRepository.findById(userId);
    }

    public boolean existsById(final UserId userId) {
        if (userId == null) {
            throw new IllegalArgumentException("User ID cannot be null");
        }
        return userRepository.existsById(userId);
    }
}
