package com.example.KindergartenBillApp.administration.controller;

import com.example.KindergartenBillApp.administration.model.Kindergarten;
import com.example.KindergartenBillApp.administration.model.dto.ActivitiesIdsDto;
import com.example.KindergartenBillApp.administration.model.dto.GroupsIdsDto;
import com.example.KindergartenBillApp.administration.services.KindergartenService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * REST controller for managing Kindergarten entities.
 * Provides CRUD operations and pagination support.
 */

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/kindergarten")
public class KindergartenController {

    private final KindergartenService kindergartenService;

    /**
     * Creates a new Kindergarten.
     * If name or email already exist, throws ApiExceptions with status 409 Conflict.
     * If account id is missing or invalid, throws ApiExceptions with status 400 Bad Request or 404 Not Found.
     * @param model Kindergarten object to be created
     * @return ResponseEntity containing the created Kindergarten and HTTP status 201 Created
     */
    @PostMapping()
    public ResponseEntity<Kindergarten> create(
            @RequestBody
            @Valid
            Kindergarten model
    ){
      return ResponseEntity.status(HttpStatus.CREATED).body(kindergartenService.create(model));
    }

    /**
     * Retrieves a paginated list of Kindergarten entities.
     * @param page the page number (0-based index, must be >= 0)
     * @param size the number of records per page (must be >= 1)
     * @return ResponseEntity containing a Page of Kindergarten and HTTP status 200 OK
     */
    @GetMapping()
    public ResponseEntity<Page<Kindergarten>> getAll(
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page index must be zero or positive")
            int page,
            @Min(value = 1, message = "Page size must be at least 1")
            @RequestParam(defaultValue = "10")
            int size
    ){
        return ResponseEntity.status(HttpStatus.OK).body(kindergartenService.findAll(page, size));
    }

    /**
     * Retrieves a Kindergarten by its unique ID.
     * If the Kindergarten does not exist, the service throws ApiExceptions with status 404 Not Found.
     * @param id the unique identifier of the Kindergarten (must be greater than zero)
     * @return ResponseEntity containing the Kindergarten and HTTP status 200 OK
     */
    @GetMapping("/{id}")
    public ResponseEntity<Kindergarten>findById(
            @PathVariable
            @NotNull(message = "id can not be null")
            @Min(value = 1, message = "id must be greater then zero")
            Integer id
    ){
        return ResponseEntity.status(HttpStatus.OK).body(kindergartenService.findById(id));
    }

    /**
     * Retrieves a Kindergarten by its unique name.
     * If the Kindergarten does not exist, the service throws ApiExceptions with status 404 Not Found.
     * @param name the unique name of the Kindergarten (must not be blank)
     * @return ResponseEntity containing the Kindergarten and HTTP status 200 OK
     */
    @GetMapping("/name/{name}")
    public ResponseEntity<Kindergarten> findByName(
            @PathVariable
            @NotBlank(message = "name can not be empty or null")
            String name
    ){
        return ResponseEntity.status(HttpStatus.OK).body(kindergartenService.findByName(name));
    }

    /**
     * Retrieves a Kindergarten by its unique email address.
     * If the Kindergarten does not exist, the service throws ApiExceptions with status 404 Not Found.
     * @param email the unique email of the Kindergarten (must not be blank and must be valid format)
     * @return ResponseEntity containing the Kindergarten and HTTP status 200 OK
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<Kindergarten> findByEmail(
            @PathVariable
            @NotBlank(message = "email can not be empty or null")
            @Email
            String email){
        return ResponseEntity.status(HttpStatus.OK).body(kindergartenService.findByEmail(email));
    }

    /**
     * Updates an existing Kindergarten by its unique ID.
     * Supports partial updates: only non-null fields from the request body are applied.
     * If the Kindergarten does not exist, the service throws ApiExceptions with status 404 Not Found.
     * If name or email conflict with existing records, throws ApiExceptions with status 409 Conflict.
     * @param model Kindergarten object containing updated data
     * @param id the unique identifier of the Kindergarten to update
     * @return ResponseEntity containing the updated Kindergarten and HTTP status 200 OK
     */
    @PutMapping("/{id}")
    public ResponseEntity<Kindergarten> update(
            @RequestBody
            Kindergarten model,
            @PathVariable
            @NotNull(message = "id can not be null")
            @Min(value = 1, message = "id must be greater then zero")
            Integer id
    ){
        return ResponseEntity.status(HttpStatus.OK).body(kindergartenService.update(model, id));
    }

    /**
     * Deletes a Kindergarten by its unique ID.
     * If the Kindergarten does not exist, the service throws ApiExceptions with status 404 Not Found.
     * On successful deletion, returns HTTP 204 No Content.
     * @param id the unique identifier of the Kindergarten to delete
     * @return ResponseEntity with HTTP status 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable
            @NotNull(message = "id can not be null")
            @Min(value = 1, message = "id must be greater then zero")
            Integer id
    ){
        kindergartenService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Adds one or more groups to a kindergarten without removing existing ones.
     * This endpoint retrieves the kindergarten by its ID, then looks up all groups
     * based on the provided IDs and adds them to the kindergarten's group set.
     * Each group ID is validated individually through GroupsIdsDto.
     * If the kindergarten or any of the groups cannot be found, the service throws
     * ApiExceptions with the appropriate HTTP status code.
     *
     * @param kindergartenId the unique identifier of the kindergarten (must be >= 1)
     * @param groupsIds      a set of group IDs to be added to the kindergarten
     * @return ResponseEntity containing the updated Kindergarten entity and HTTP status 200 OK
     */
    @PostMapping("{kindergartenId}/groups")
    public ResponseEntity<Kindergarten> addGroupsToKindergarten(
            @PathVariable
            @NotNull(message = "id can not be null")
            @Min(value = 1, message = "id must be greater then zero")
            Integer kindergartenId,
            @RequestBody
            Set<@Valid GroupsIdsDto> groupsIds
    ){
        Set<Integer> ids = groupsIds.stream()
                .map(GroupsIdsDto::getGroupId)
                .collect(Collectors.toSet());
        return ResponseEntity.status(HttpStatus.OK).body(kindergartenService.addGroupsToKindergarten(kindergartenId, ids));
    }

    /**
     * Removes one or more groups from a kindergarten.
     * This endpoint retrieves the kindergarten by its ID, then looks up all groups
     * based on the provided IDs and removes them from the kindergarten's group set.
     * Each group ID is validated individually through GroupsIdsDto.
     * If the kindergarten or any of the groups cannot be found, the service throws
     * ApiExceptions with the appropriate HTTP status code.
     *
     * @param kindergartenId the unique identifier of the kindergarten (must be >= 1)
     * @param groupsIds      a set of group IDs to be removed from the kindergarten
     * @return ResponseEntity containing the updated Kindergarten entity without the removed groups and HTTP status 200 OK
     */
    @DeleteMapping("{kindergartenId}/groups")
    public ResponseEntity<Kindergarten> removeGroupsFromKindergarten(
            @PathVariable
            @NotNull(message = "id can not be null")
            @Min(value = 1, message = "id must be greater then zero")
            Integer kindergartenId,
            @RequestBody
            Set<@Valid GroupsIdsDto> groupsIds
    ){

        Set<Integer> ids = groupsIds.stream()
                .map(GroupsIdsDto::getGroupId)
                .collect(Collectors.toSet());
        return ResponseEntity.status(HttpStatus.OK).body(kindergartenService.removeGroupsFromKindergarten(kindergartenId, ids));
    }

    /**
     * Clears all groups associated with a kindergarten.
     * This endpoint retrieves the kindergarten by its ID and removes all groups
     * from its group set. If the kindergarten cannot be found, the service throws
     * ApiExceptions with status 404 Not Found.
     *
     * @param kindergartenId the unique identifier of the kindergarten (must be >= 1)
     * @return ResponseEntity containing the updated Kindergarten entity without any groups and HTTP status 200 OK
     */
    @DeleteMapping("{kindergartenId}/groups/clear")
    public ResponseEntity<Kindergarten> clearGroupsFromKindergarten(
            @PathVariable
            @NotNull(message = "id can not be null")
            @Min(value = 1, message = "id must be greater then zero")
            Integer kindergartenId){

        return ResponseEntity.status(HttpStatus.OK).body(kindergartenService.clearGroupsFromKindergarten(kindergartenId));
    }

    /**
     * Adds one or more activities to a kindergarten without removing existing ones.
     * This endpoint retrieves the kindergarten by its ID, then looks up all activities
     * based on the provided IDs and adds them to the kindergarten's activity set.
     * Each activity ID is validated individually through ActivitiesIdsDto.
     * If the kindergarten or any of the activities cannot be found, the service throws
     * ApiExceptions with the appropriate HTTP status code.
     * If the kindergarten already contains one of the specified activities,
     * an ApiExceptions with status 409 Conflict is thrown.
     *
     * @param kindergartenId the unique identifier of the kindergarten (must be >= 1)
     * @param groupsIds      a set of activity IDs to be added to the kindergarten
     * @return ResponseEntity containing the updated Kindergarten entity and HTTP status 200 OK
     */
    @PostMapping("{kindergartenId}/activities")
    public ResponseEntity<Kindergarten> addActivitiesToKindergarten(
            @PathVariable
            @NotNull(message = "id can not be null")
            @Min(value = 1, message = "id must be greater then zero")
            Integer kindergartenId,
            @RequestBody
            Set<@Valid ActivitiesIdsDto> groupsIds
    ){
        Set<Integer> ids = groupsIds.stream()
                .map(ActivitiesIdsDto::getActivitiesId)
                .collect(Collectors.toSet());
        return ResponseEntity.status(HttpStatus.OK).body(kindergartenService.addActivityToKindergarten(kindergartenId, ids));
    }

    /**
     * Removes one or more activities from a kindergarten.
     * This endpoint retrieves the kindergarten by its ID, then looks up all activities
     * based on the provided IDs and removes them from the kindergarten's activity set.
     * Each activity ID is validated individually through ActivitiesIdsDto.
     * If the kindergarten or any of the activities cannot be found, the service throws
     * ApiExceptions with the appropriate HTTP status code.
     *
     * @param kindergartenId the unique identifier of the kindergarten (must be >= 1)
     * @param groupsIds      a set of activity IDs to be removed from the kindergarten
     * @return ResponseEntity containing the updated Kindergarten entity without the removed activities and HTTP status 200 OK
     */
    @DeleteMapping("{kindergartenId}/activities")
    public ResponseEntity<Kindergarten> removeActivitiesFromKindergarten(
            @PathVariable
            @NotNull(message = "id can not be null")
            @Min(value = 1, message = "id must be greater then zero")
            Integer kindergartenId,
            @RequestBody
            Set<@Valid ActivitiesIdsDto> groupsIds
    ){

        Set<Integer> ids = groupsIds.stream()
                .map(ActivitiesIdsDto::getActivitiesId)
                .collect(Collectors.toSet());
        return ResponseEntity.status(HttpStatus.OK).body(kindergartenService.removeActivityFromKindergarten(kindergartenId, ids));
    }

    /**
     * Clears all activities associated with a kindergarten.
     * This endpoint retrieves the kindergarten by its ID and removes all activities
     * from its activity set. If the kindergarten cannot be found, the service throws
     * ApiExceptions with status 404 Not Found.
     *
     * @param kindergartenId the unique identifier of the kindergarten (must be >= 1)
     * @return ResponseEntity containing the updated Kindergarten entity without any activities and HTTP status 200 OK
     */
    @DeleteMapping("{kindergartenId}/activities/clear")
    public ResponseEntity<Kindergarten> clearActivitiesFromKindergarten(
            @PathVariable
            @NotNull(message = "id can not be null")
            @Min(value = 1, message = "id must be greater then zero")
            Integer kindergartenId){

        return ResponseEntity.status(HttpStatus.OK).body(kindergartenService.clearActivitiesFromKindergarten(kindergartenId));
    }



}
