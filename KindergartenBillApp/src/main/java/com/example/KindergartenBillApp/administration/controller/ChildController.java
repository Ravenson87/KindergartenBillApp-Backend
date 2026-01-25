package com.example.KindergartenBillApp.administration.controller;

import com.example.KindergartenBillApp.administration.model.Child;
import com.example.KindergartenBillApp.administration.model.dto.ActivitiesIdsDto;
import com.example.KindergartenBillApp.administration.services.ChildService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * REST controller for managing Child entities.
 * Provides CRUD operations and pagination support.
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/child")
public class ChildController {

    private final ChildService childService;

    /**
     * Creates a new Child entity.
     * Validates the request body using Bean Validation annotations.
     * If validation passes, delegates creation to the ChildService.
     * Returns HTTP 201 Created with the created Child entity in the response body.
     *
     * @param model Child object to be created, validated with @Valid
     * @return ResponseEntity containing the created Child entity and HTTP status 201 Created
     */
    @PostMapping()
    public ResponseEntity<Child> create(
            @RequestBody
            @Valid
            Child model
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(childService.create(model));
    }

    /**
     * Retrieves a paginated list of Child entities.
     * Accepts page and size parameters with default values (page=0, size=10).
     * Validates that page is non-negative and size is at least 1.
     * Returns HTTP 200 OK with a Page object containing Child entities and pagination metadata.
     *
     * @param page the page number (0-based index, must be >= 0)
     * @param size the number of records per page (must be >= 1)
     * @return ResponseEntity containing a Page of Child entities and HTTP status 200 OK
     */
    @GetMapping()
    public ResponseEntity<Page<Child>> findAll(
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page index must be zero or positive")
            int page,
            @Min(value = 1, message = "Page size must be at least 1")
            @RequestParam(defaultValue = "10")
            int size
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(childService.findAll(page, size));
    }

    /**
     * Retrieves a Child entity by its unique ID.
     * Validates that the ID is not null and greater than zero.
     * If the entity does not exist, ChildService throws ApiExceptions with status 404 Not Found.
     * Returns HTTP 200 OK with the Child entity in the response body if found.
     *
     * @param id unique identifier of the Child entity (must be >= 1)
     * @return ResponseEntity containing the Child entity and HTTP status 200 OK
     */
    @GetMapping("/{id}")
    public ResponseEntity<Child> findById(
            @PathVariable
            @NotNull(message = "id can not be null")
            @Min(value = 1, message = "id must be greater then zero")
            Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(childService.findById(id));
    }

    /**
     * Updates an existing Child entity by its ID.
     * Allows partial updates: only non-null fields from the input model are applied.
     * Validates that the ID is not null and greater than zero.
     * Delegates update logic to ChildService, which performs validation of referenced entities
     * and uniqueness checks.
     * Returns HTTP 200 OK with the updated Child entity in the response body.
     *
     * @param model Child object containing updated fields
     * @param id    unique identifier of the Child entity to update (must be >= 1)
     * @return ResponseEntity containing the updated Child entity and HTTP status 200 OK
     */
    @PutMapping("/{id}")
    public ResponseEntity<Child> update(
            @RequestBody
            Child model,
            @PathVariable
            @NotNull(message = "id can not be null")
            @Min(value = 1, message = "id must be greater then zero")
            Integer id
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(childService.update(model, id));
    }

    /**
     * Deletes a Child entity by its unique ID.
     * Validates that the ID is not null and greater than zero.
     * Delegates deletion to ChildService, which ensures the entity exists before deleting.
     * Returns HTTP 204 No Content if deletion is successful.
     *
     * @param id unique identifier of the Child entity to delete (must be >= 1)
     * @return ResponseEntity with HTTP status 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable
            @NotNull(message = "id can not be null")
            @Min(value = 1, message = "id must be greater then zero")
            Integer id
    ) {
        childService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Adds one or more activities to a child.
     *
     * <p>This endpoint retrieves the child by its ID, then looks up all activities
     * based on the provided IDs and adds them to the child's activity set.
     * Existing activities remain linked to the child. If the child or any of the
     * activities cannot be found, the service throws ApiExceptions with the appropriate
     * HTTP status code.</p>
     *
     * @param childId      unique identifier of the Child entity (must be >= 1)
     * @param activitiesId set of activity IDs to be added to the child, validated individually
     * @return ResponseEntity containing the updated Child entity and HTTP status 200 OK
     */
    @PostMapping("/{childId}/activities")
    public ResponseEntity<Child> addActivitiesToChild(
            @PathVariable
            @NotNull(message = "child id can not be null")
            @Min(value = 1, message = "child id must be greater than zero")
            Integer childId,
            @RequestBody
            Set<@Valid ActivitiesIdsDto> activitiesId
    ) {
        Set<Integer> ids = activitiesId.stream()
                .map(ActivitiesIdsDto::getActivitiesId)
                .collect(Collectors.toSet());
        return ResponseEntity.status(HttpStatus.OK).body(childService.addActivitiesToChild(childId, ids));
    }

    /**
     * Removes one or more activities from a child.
     *
     * <p>This endpoint retrieves the child by its ID, then looks up all activities
     * based on the provided IDs and removes them from the child's activity set.
     * If the child or any of the activities cannot be found, the service throws
     * ApiExceptions with the appropriate HTTP status code.</p>
     *
     * @param childId      unique identifier of the Child entity (must be >= 1)
     * @param activitiesId set of activity IDs to be removed from the child, validated individually
     * @return ResponseEntity containing the updated Child entity without the removed activities and HTTP status 200 OK
     */
    @DeleteMapping("/{childId}/activities")
    public ResponseEntity<Child> removeActivitiesFromChild(
            @PathVariable
            @NotNull(message = "child id can not be null")
            @Min(value = 1, message = "child id must be greater than zero")
            Integer childId,
            @RequestBody
            Set<@Valid ActivitiesIdsDto> activitiesId
    ) {
        Set<Integer> ids = activitiesId.stream()
                .map(ActivitiesIdsDto::getActivitiesId)
                .collect(Collectors.toSet());
        return ResponseEntity.status(HttpStatus.OK).body(childService.removeActivitiesFromChild(childId, ids));
    }

    /**
     * Clears all activities associated with a child.
     *
     * <p>This endpoint retrieves the child by its ID and removes all activities
     * from its activity set. If the child cannot be found, the service throws
     * ApiExceptions with status 404 Not Found.</p>
     *
     * @param childId unique identifier of the Child entity (must be >= 1)
     * @return ResponseEntity containing the updated Child entity without any activities and HTTP status 200 OK
     */
    @DeleteMapping("/{childId}/activities/clear")
    public ResponseEntity<Child> clearActivitiesFromChild(
            @PathVariable
            @NotNull(message = "child id can not be null")
            @Min(value = 1, message = "child id must be greater than zero")
            Integer childId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(childService.clearActivitiesFromChild(childId));
    }
}

