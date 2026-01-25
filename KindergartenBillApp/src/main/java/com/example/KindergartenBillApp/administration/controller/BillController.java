package com.example.KindergartenBillApp.administration.controller;

import com.example.KindergartenBillApp.administration.model.Bill;
import com.example.KindergartenBillApp.administration.services.BillService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing Bill entities.
 * Provides CRUD operations and pagination support.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/bill")
public class BillController {

    private final BillService billService;

    /**
     * Creates a new Bill entity.
     * Validates the request body using Bean Validation annotations.
     * If referenced Kindergarten or Child IDs are missing or invalid, BillService throws ApiExceptions.
     * Returns HTTP 201 Created with the created Bill entity in the response body.
     * @param model Bill object to be created, validated with @Valid
     * @return ResponseEntity containing the created Bill entity and HTTP status 201 Created
     */
    @PostMapping()
    public ResponseEntity<Bill> create(
            @RequestBody
            @Valid
            Bill model
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(billService.create(model));
    }

    /**
     * Retrieves a paginated list of Bill entities.
     * Accepts page and size parameters with default values (page=0, size=10).
     * Validates that page is non-negative and size is at least 1.
     * Returns HTTP 200 OK with a Page object containing Bill entities and pagination metadata.
     * @param page the page number (0-based index, must be >= 0)
     * @param size the number of records per page (must be >= 1)
     * @return ResponseEntity containing a Page of Bill entities and HTTP status 200 OK
     */
    @GetMapping()
    public ResponseEntity<Page<Bill>> findAll(
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page index must be zero or positive")
            int page,
            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Page size must be at least 1")
            int size
    ){
        return ResponseEntity.status(HttpStatus.OK).body(billService.findAll(page, size));
    }

    /**
     * Retrieves a Bill entity by its unique ID.
     * Validates that the ID is not null and greater than zero.
     * If the entity does not exist, BillService throws ApiExceptions with status 404 Not Found.
     * Returns HTTP 200 OK with the Bill entity in the response body if found.
     * @param id unique identifier of the Bill entity (must be >= 1)
     * @return ResponseEntity containing the Bill entity and HTTP status 200 OK
     */
    @GetMapping("/{id}")
    public ResponseEntity<Bill> findById(
            @PathVariable
            @NotNull(message = "id can not be null")
            @Min(value = 1, message = "id must be greater than zero")
            Integer id
    ){
        return ResponseEntity.status(HttpStatus.OK).body(billService.findById(id));
    }

    /**
     * Updates an existing Bill entity by its ID.
     * Allows partial updates: only non-null fields from the input model are applied.
     * Validates that the ID is not null and greater than zero.
     * Delegates update logic to BillService, which performs validation of referenced entities.
     * Returns HTTP 200 OK with the updated Bill entity in the response body.
     * @param model Bill object containing updated fields
     * @param id unique identifier of the Bill entity to update (must be >= 1)
     * @return ResponseEntity containing the updated Bill entity and HTTP status 200 OK
     */
    @PutMapping("/{id}")
    public ResponseEntity<Bill> update(
            @RequestBody
            Bill model,
            @PathVariable
            @NotNull(message = "id can not be null")
            @Min(value = 1, message = "id must be greater than zero")
            Integer id
    ){
        return ResponseEntity.status(HttpStatus.OK).body(billService.update(model, id));
    }

    /**
     * Deletes a Bill entity by its unique ID.
     * Validates that the ID is not null and greater than zero.
     * Delegates deletion to BillService, which ensures the entity exists before deleting.
     * Returns HTTP 204 No Content if deletion is successful.
     * @param id unique identifier of the Bill entity to delete (must be >= 1)
     * @return ResponseEntity with HTTP status 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(
            @PathVariable
            @NotNull(message = "id can not be null")
            @Min(value = 1, message = "id must be greater than zero")
            Integer id
    ){
        billService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
