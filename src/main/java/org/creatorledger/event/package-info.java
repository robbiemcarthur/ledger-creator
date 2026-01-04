/**
 * Event module - creator events and engagements.
 * <p>
 * This module manages professional events, workshops, performances,
 * and other engagements for creatives.
 * </p>
 * <p>
 * <strong>Public API:</strong> The {@code api} package contains {@code EventId}
 * which other modules can safely depend on.
 * </p>
 * <p>
 * <strong>Dependencies:</strong>
 * </p>
 * <ul>
 *   <li>{@code user::api} - UserId for event ownership</li>
 * </ul>
 */
@org.springframework.modulith.ApplicationModule(
    displayName = "Event",
    allowedDependencies = {"user :: api"}
)
package org.creatorledger.event;
