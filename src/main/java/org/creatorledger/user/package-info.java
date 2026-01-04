/**
 * User module - user registration and management.
 * <p>
 * This module handles user identity and provides the foundation
 * for multi-tenancy across all other modules.
 * </p>
 * <p>
 * <strong>Public API:</strong> The {@code api} package contains {@code UserId}
 * which other modules can safely depend on.
 * </p>
 * <p>
 * <strong>Dependencies:</strong> None - this is a foundation module.
 * </p>
 */
@org.springframework.modulith.ApplicationModule(
    displayName = "User"
)
package org.creatorledger.user;
