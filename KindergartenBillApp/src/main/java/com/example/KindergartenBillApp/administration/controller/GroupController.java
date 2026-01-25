package com.example.KindergartenBillApp.administration.controller;

import com.example.KindergartenBillApp.administration.model.Group;
import com.example.KindergartenBillApp.administration.services.GroupService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing Groups entities.
 * Provides CRUD operations and pagination support.
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/groups")
public class GroupController {

    private final GroupService groupService;

    /**
     * Creates a new Groups entity.
     * Validates the input model before saving.
     * If a group with the same name already exists, throws ApiExceptions with status 409 Conflict.
     * @param model Groups object to be created
     * @return ResponseEntity containing the created Groups entity and HTTP status 201 Created
     */
    @PostMapping()
    public ResponseEntity<Group> create(
            @RequestBody
            @Valid
            Group model
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(groupService.create(model));
    }

    /**
     * Retrieves a paginated list of Groups entities.
     * @param page the page number (0-based index, must be >= 0)
     * @param size the number of records per page (must be >= 1)
     * @return ResponseEntity containing a Page of Groups and HTTP status 200 OK
     */
    @GetMapping()
    public ResponseEntity<Page<Group>> findAll(
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page index must be zero or positive")
            int page,
            @Min(value = 1, message = "Page size must be at least 1")
            @RequestParam(defaultValue = "10")
            int size
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(groupService.findAll(page, size));
    }

    /**
     * Retrieves a Groups entity by its unique ID.
     * If the entity does not exist, throws ApiExceptions with status 404 Not Found.
     * @param id unique identifier of the Groups entity (must be greater than zero)
     * @return ResponseEntity containing the Groups entity and HTTP status 200 OK
     */
    @GetMapping("/{id}")
    public ResponseEntity<Group> findById(
            @PathVariable
            @NotNull(message = "id can not be null")
            @Min(value = 1, message = "id must be greater then zero")
            Integer id) {

        return ResponseEntity.status(HttpStatus.OK).body(groupService.findById(id));
    }

    /**
     * Retrieves a Groups entity by its unique name.
     * If the entity does not exist, throws ApiExceptions with status 404 Not Found.
     * @param name unique name of the Groups entity (must not be blank)
     * @return ResponseEntity containing the Groups entity and HTTP status 200 OK
     */
    @GetMapping("name/{name}")
    public ResponseEntity<Group> findByName(
            @PathVariable
            @NotBlank(message = "name can not be empty or null")
            String name) {
        return ResponseEntity.status(HttpStatus.OK).body(groupService.findByName(name));
    }

    /**
     * Updates an existing Groups entity by its ID.
     * Supports partial updates: only non-null fields from the provided model are applied.
     * If the entity does not exist, throws ApiExceptions with status 404 Not Found.
     * If the new name conflicts with an existing group, throws ApiExceptions with status 409 Conflict.
     * @param model Groups object containing updated values
     * @param id unique identifier of the Groups entity (must be greater than zero)
     * @return ResponseEntity containing the updated Groups entity and HTTP status 200 OK
     */
    @PutMapping("/{id}")
    public ResponseEntity<Group> update(
            @RequestBody
            Group model,
            @PathVariable
            @NotNull(message = "id can not be null")
            @Min(value = 1, message = "id must be greater then zero")
            Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(groupService.update(model, id));
    }

    /**
     * Deletes a Groups entity by its unique ID.
     * Retrieves the entity first in the service layer to allow audit logging before deletion.
     * If the entity does not exist, throws ApiExceptions with status 404 Not Found.
     * @param id unique identifier of the Groups entity (must be greater than zero)
     * @return ResponseEntity with HTTP status 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable
            @NotNull(message = "id can not be null")
            @Min(value = 1, message = "id must be greater then zero")
            Integer id)
    {
        groupService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
