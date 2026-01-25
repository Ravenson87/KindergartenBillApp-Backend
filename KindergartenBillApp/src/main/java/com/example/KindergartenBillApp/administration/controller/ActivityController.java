package com.example.KindergartenBillApp.administration.controller;

import com.example.KindergartenBillApp.administration.model.Activity;
import com.example.KindergartenBillApp.administration.services.ActivityService;
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
 * REST controller for managing Activities entities.
 * Provides CRUD operations and pagination support.
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/activities")
public class ActivityController {

    private final ActivityService activityService;

    /**
     * Creates a new Activities entity.
     * Validates the input model and ensures the activity name is unique.
     * @param model Activities object to be created
     * @return ResponseEntity containing the created Activities and HTTP status 201 Created
     */
    @PostMapping()
    public ResponseEntity<Activity> create(
            @RequestBody
            @Valid
            Activity model){
        return ResponseEntity.status(HttpStatus.CREATED).body(activityService.create(model));
    }

    /**
     * Retrieves a paginated list of Activities entities.
     * @param page the page number (0-based index, must be >= 0)
     * @param size the number of records per page (must be >= 1)
     * @return ResponseEntity containing a Page of Activities and HTTP status 200 OK
     */
    @GetMapping()
    public ResponseEntity<Page<Activity>> findAll(
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page index must be zero or positive")
            int page,
            @Min(value = 1, message = "Page size must be at least 1")
            @RequestParam(defaultValue = "10")
            int size
    ){
        return ResponseEntity.status(HttpStatus.OK).body(activityService.findAll(page, size));
    }

    /**
     * Retrieves an Activities entity by its unique ID.
     * If the entity does not exist, throws ApiExceptions with status 404 Not Found.
     * @param id unique identifier of the Activities entity
     * @return ResponseEntity containing the Activities entity and HTTP status 200 OK
     */
    @GetMapping("/{id}")
    public ResponseEntity<Activity> findById(
            @PathVariable
            @NotNull(message = "id can not be null")
            @Min(value = 1, message = "id must be greater then zero")
            Integer id){
        return ResponseEntity.status(HttpStatus.OK).body(activityService.findById(id));
    }

    /**
     * Retrieves an Activities entity by its unique name.
     * If the entity does not exist, throws ApiExceptions with status 404 Not Found.
     * @param name unique name of the Activities entity (must not be blank)
     * @return ResponseEntity containing the Activities entity and HTTP status 200 OK
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<Activity> findByName(
            @PathVariable
            @NotBlank(message = "name can not be empty or null")
            String name){
        return ResponseEntity.status(HttpStatus.OK).body(activityService.findByName(name));
    }

    /**
     * Updates an existing Activities entity by its ID.
     * Supports partial updates: only non-null fields are applied.
     * @param model Activities object containing updated values
     * @param id unique identifier of the Activities entity (must be > 0)
     * @return ResponseEntity containing the updated Activities entity and HTTP status 200 OK
     */
    @PutMapping("/{id}")
    public ResponseEntity<Activity> update(
            @RequestBody
            Activity model,
            @PathVariable
            @NotNull(message = "id can not be null")
            @Min(value = 1, message = "id must be greater then zero")
            Integer id) {
        return ResponseEntity.status(HttpStatus.OK).body(activityService.update(model, id));
    }

    /**
     * Deletes an Activities entity by its unique ID.
     * If the entity does not exist, throws ApiExceptions with status 404 Not Found.
     * @param id unique identifier of the Activities entity (must be > 0)
     * @return ResponseEntity with HTTP status 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable
            @NotNull(message = "id can not be null")
            @Min(value = 1, message = "id must be greater then zero")
            Integer id) {
        activityService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
