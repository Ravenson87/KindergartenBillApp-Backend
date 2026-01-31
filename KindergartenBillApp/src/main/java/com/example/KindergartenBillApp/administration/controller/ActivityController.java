package com.example.KindergartenBillApp.administration.controller;

import com.example.KindergartenBillApp.administration.model.Activity;
import com.example.KindergartenBillApp.administration.model.dto.ActivityDto;
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
     * Retrieves a paginated list of Activity DTOs.
     *
     * <p>This method fetches a page of Activity entities from the service layer,
     * maps each entity to an {@link ActivityDto}, and returns the result wrapped
     * in a {@link ResponseEntity}.</p>
     *
     * <p>If no activities exist, an empty page is returned with HTTP status 200 OK.</p>
     *
     * @param page the page number (0-based index, must be >= 0)
     * @param size the number of records per page (must be >= 1)
     * @return ResponseEntity containing a Page of ActivityDto objects and HTTP status 200 OK
     */
    @GetMapping()
    public ResponseEntity<Page<ActivityDto>> findAll(
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page index must be zero or positive")
            int page,
            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Page size must be at least 1")
            int size
    ) {
        Page<Activity> activityPage = activityService.findAll(page, size);

        Page<ActivityDto> dtoPage = activityPage.map(a ->
                new ActivityDto(a.getId(), a.getName(), a.getPrice(), a.getStatus())
        );

        return ResponseEntity.status(HttpStatus.OK).body(dtoPage);
    }


    /**
     * Retrieves a single Activity as a DTO by its unique identifier.
     *
     * <p>This method fetches an Activity entity from the service layer,
     * maps it to an {@link ActivityDto}, and returns the result wrapped
     * in a {@link ResponseEntity}.</p>
     *
     * <p>If the Activity with the given id is not found,
     * an error response with HTTP status 404 is returned.</p>
     *
     * @param id the unique identifier of the Activity (must be >= 1)
     * @return ResponseEntity containing the ActivityDto object and HTTP status 200 OK
     */
    @GetMapping("/{id}")
    public ResponseEntity<ActivityDto> findById(
            @PathVariable
            @NotNull(message = "id can not be null")
            @Min(value = 1, message = "id must be greater than zero")
            Integer id
    ) {
        Activity a = activityService.findById(id);
        ActivityDto dto = new ActivityDto(a.getId(), a.getName(), a.getPrice(), a.getStatus());
        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }


    /**
     * Retrieves a single Activity as a DTO by its unique name.
     *
     * <p>This method fetches an Activity entity from the service layer,
     * maps it to an {@link ActivityDto}, and returns the result wrapped
     * in a {@link ResponseEntity}.</p>
     *
     * <p>If the Activity with the given name is not found,
     * an error response with HTTP status 404 is returned.</p>
     *
     * @param name the unique name of the Activity (must not be blank)
     * @return ResponseEntity containing the ActivityDto object and HTTP status 200 OK
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<ActivityDto> findByName(
            @PathVariable
            @NotBlank(message = "name can not be empty or null")
            String name
    ) {
        Activity a = activityService.findByName(name);
        ActivityDto dto = new ActivityDto(a.getId(), a.getName(), a.getPrice(), a.getStatus());
        return ResponseEntity.status(HttpStatus.OK).body(dto);
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
