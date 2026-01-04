package org.creatorledger.income.infrastructure.web;

import org.creatorledger.event.api.EventId;
import org.creatorledger.income.api.IncomeId;
import org.creatorledger.income.application.IncomeApplicationService;
import org.creatorledger.income.application.RecordIncomeCommand;
import org.creatorledger.income.application.UpdateIncomeCommand;
import org.creatorledger.user.api.UserId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

/**
 * REST controller for income-related endpoints.
 * <p>
 * This controller provides HTTP API access to income functionality,
 * translating HTTP requests/responses to application commands/queries.
 * </p>
 */
@RestController
@RequestMapping("/api/income")
public class IncomeController {

    private final IncomeApplicationService incomeApplicationService;

    public IncomeController(IncomeApplicationService incomeApplicationService) {
        this.incomeApplicationService = incomeApplicationService;
    }

    /**
     * Records new income.
     *
     * @param request the record request containing income details
     * @return 201 Created with Location header pointing to the new income
     */
    @PostMapping
    public ResponseEntity<Void> record(@RequestBody RecordIncomeRequest request) {
        try {
            RecordIncomeCommand command = new RecordIncomeCommand(
                    UserId.of(request.userId()),
                    EventId.of(request.eventId()),
                    request.amount(),
                    request.currency(),
                    request.description(),
                    request.receivedDate()
            );
            IncomeId incomeId = incomeApplicationService.record(command);

            URI location = URI.create("/api/income/" + incomeId.value());
            return ResponseEntity.created(location).build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Updates an existing income.
     *
     * @param id the income ID
     * @param request the update request containing new income details
     * @return 204 No Content if successful, 400 Bad Request if income not found or invalid
     */
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable String id, @RequestBody UpdateIncomeRequest request) {
        try {
            UUID uuid = UUID.fromString(id);
            IncomeId incomeId = IncomeId.of(uuid);

            UpdateIncomeCommand command = new UpdateIncomeCommand(
                    incomeId,
                    request.amount(),
                    request.currency(),
                    request.description(),
                    request.receivedDate()
            );
            incomeApplicationService.update(command);

            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Gets an income by its ID.
     *
     * @param id the income ID
     * @return 200 OK with income data if found, 404 Not Found otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<IncomeResponse> getIncome(@PathVariable String id) {
        try {
            UUID uuid = UUID.fromString(id);
            IncomeId incomeId = IncomeId.of(uuid);

            return incomeApplicationService.findById(incomeId)
                    .map(IncomeResponse::from)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Checks if an income exists with the given ID.
     *
     * @param id the income ID
     * @return 200 OK with boolean indicating existence
     */
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> existsById(@PathVariable String id) {
        try {
            UUID uuid = UUID.fromString(id);
            IncomeId incomeId = IncomeId.of(uuid);

            boolean exists = incomeApplicationService.existsById(incomeId);
            return ResponseEntity.ok(exists);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Marks an income as paid.
     *
     * @param id the income ID
     * @return 204 No Content if successful, 400 Bad Request if income not found or invalid
     */
    @PutMapping("/{id}/mark-as-paid")
    public ResponseEntity<Void> markAsPaid(@PathVariable String id) {
        try {
            UUID uuid = UUID.fromString(id);
            IncomeId incomeId = IncomeId.of(uuid);

            incomeApplicationService.markAsPaid(incomeId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Marks an income as overdue.
     *
     * @param id the income ID
     * @return 204 No Content if successful, 400 Bad Request if income not found or invalid
     */
    @PutMapping("/{id}/mark-as-overdue")
    public ResponseEntity<Void> markAsOverdue(@PathVariable String id) {
        try {
            UUID uuid = UUID.fromString(id);
            IncomeId incomeId = IncomeId.of(uuid);

            incomeApplicationService.markAsOverdue(incomeId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Cancels an income.
     *
     * @param id the income ID
     * @return 204 No Content if successful, 400 Bad Request if income not found or invalid
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(@PathVariable String id) {
        try {
            UUID uuid = UUID.fromString(id);
            IncomeId incomeId = IncomeId.of(uuid);

            incomeApplicationService.cancel(incomeId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
