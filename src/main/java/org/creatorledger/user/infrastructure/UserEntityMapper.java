package org.creatorledger.user.infrastructure;

import org.creatorledger.user.domain.Email;
import org.creatorledger.user.domain.User;
import org.creatorledger.user.api.UserId;

public class UserEntityMapper {

    public static UserJpaEntity toEntity(User user) {
        if (user == null) {
            return null;
        }
        return new UserJpaEntity(
                user.id().value(),
                user.email().value()
        );
    }

    public static User toDomain(UserJpaEntity entity) {
        if (entity == null) {
            return null;
        }
        return User.register(
                UserId.of(entity.getId()),
                Email.of(entity.getEmail())
        );
    }
}
