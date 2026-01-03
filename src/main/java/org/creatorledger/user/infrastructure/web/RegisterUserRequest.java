package org.creatorledger.user.infrastructure.web;

/**
 * Request DTO for user registration endpoint.
 * <p>
 * This is a presentation layer concern for HTTP API communication.
 * It is converted to RegisterUserCommand for application layer processing.
 * </p>
 *
 * @param email the email address for the new user
 */
public record RegisterUserRequest(String email) {

    /**
     * Creates a new RegisterUserRequest.
     *
     * @throws IllegalArgumentException if email is null or blank
     */
    public RegisterUserRequest {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
    }
}
