package org.creatorledger.user.domain;

import org.creatorledger.user.api.UserId;

/**
 * Aggregate root representing a user in the system.
 * Users are identified by their UserId and have an email address.
 */
public record User(UserId id, Email email) {
    public static User register(final Email email) {
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        return new User(UserId.generate(), email);
    }

    public static User register(final UserId id, final Email email) {
        if (id == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        return new User(id, email);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof User other)) return false;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "User[id=" + id + ", email=" + email.value() + "]";
    }
}
