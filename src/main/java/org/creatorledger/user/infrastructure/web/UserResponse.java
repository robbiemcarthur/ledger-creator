package org.creatorledger.user.infrastructure.web;

import org.creatorledger.user.domain.User;

/**
 * Response DTO for user data in API responses.
 * <p>
 * This is a presentation layer concern for HTTP API communication.
 * It represents user data in a format suitable for API consumers.
 * </p>
 *
 * @param id    the user's unique identifier
 * @param email the user's email address
 */
public record UserResponse(String id, String email) {

    /**
     * Creates a UserResponse from a domain User.
     *
     * @param user the domain user
     * @return a UserResponse containing the user's data
     * @throws IllegalArgumentException if user is null
     */
    public static UserResponse from(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        return new UserResponse(
                user.id().value().toString(),
                user.email().value()
        );
    }
}
