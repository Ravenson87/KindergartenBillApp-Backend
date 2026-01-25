package com.example.KindergartenBillApp.administration.controller;

import com.example.KindergartenBillApp.administration.model.Parent;
import com.example.KindergartenBillApp.administration.services.ParentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/parent")
public class ParentController {

    private final ParentService parentService;

    /**
     * Creates a new Parent entity.
     * Validates the input model using Bean Validation annotations.
     * Returns HTTP 201 Created status with the saved Parent entity in the response body.
     *
     * @param model Parent object to be created, validated with @Valid
     * @return ResponseEntity containing the created Parent entity
     */
    @PostMapping()
    public ResponseEntity<Parent> create(
            @RequestBody
            @Valid
            Parent model
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(parentService.create(model));
    }

    /**
     * Retrieves a paginated list of Parent entities.
     * Accepts page and size parameters with default values if not provided.
     * Returns HTTP 200 OK status with a Page object containing Parent entities.
     *
     * @param page the page index (0-based, must be >= 0)
     * @param size the number of records per page (must be >= 1)
     * @return ResponseEntity containing a Page of Parent entities
     */
    @GetMapping()
    public ResponseEntity<Page<Parent>> findAll(
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page index must be zero or positive")
            int page,
            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Page size must be at least 1")
            int size
    ){
        return ResponseEntity.status(HttpStatus.OK).body(parentService.findAll(page, size));
    }

    /**
     * Retrieves a Parent entity by its unique ID.
     * Validates that the ID is not null and greater than zero.
     * Returns HTTP 200 OK status with the Parent entity if found.
     *
     * @param id unique identifier of the Parent entity (must be >= 1)
     * @return ResponseEntity containing the Parent entity with the given ID
     */
    @GetMapping("{id}")
    public ResponseEntity<Parent> findById(
            @PathVariable
            @NotNull(message = "id can not be null")
            @Min(value = 1, message = "id must be greater than zero")
            Integer id
    ){
        return ResponseEntity.status(HttpStatus.OK).body(parentService.findById(id));
    }

    /**
     * Retrieves a Parent entity by its email address.
     * Validates that the email parameter is not null.
     * Returns HTTP 200 OK status with the Parent entity if found.
     *
     * @param email unique email address of the Parent entity
     * @return ResponseEntity containing the Parent entity with the given email
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<Parent> findByEmail(
            @PathVariable
            @NotNull(message = "email can not be null")
            String email
    ){
        return ResponseEntity.status(HttpStatus.OK).body(parentService.findByEmail(email));
    }

    /**
     * Updates an existing Parent entity by its ID.
     * Allows partial updates: only non-null fields from the input model are applied.
     * Validates that the ID is not null and greater than zero.
     * Returns HTTP 200 OK status with the updated Parent entity.
     *
     * @param model Parent object containing updated fields
     * @param id unique identifier of the Parent entity to update (must be >= 1)
     * @return ResponseEntity containing the updated Parent entity
     */
    @PutMapping("{id}")
    public ResponseEntity<Parent> update(
            @RequestBody
            Parent model,
            @PathVariable
            @NotNull(message = "id can not be null")
            @Min(value = 1, message = "id must be greater than zero")
            Integer id){
        return ResponseEntity.status(HttpStatus.OK).body(parentService.update(model, id));
    }

    /**
     * Deletes a Parent entity by its unique ID.
     * Validates that the ID is not null and greater than zero.
     * Returns HTTP 204 No Content status if deletion is successful.
     *
     * @param id unique identifier of the Parent entity to delete (must be >= 1)
     * @return ResponseEntity with no content
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(
            @PathVariable
            @NotNull(message = "id can not be null")
            @Min(value = 1, message = "id must be greater than zero")
            Integer id
    ){
        parentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
