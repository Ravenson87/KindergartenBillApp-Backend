package com.example.KindergartenBillApp.administration.controller;

import com.example.KindergartenBillApp.administration.model.KindergartenAccount;
import com.example.KindergartenBillApp.administration.services.KindergartenAccountService;
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
 * REST controller for managing KindergartenAccount entities.
 * Provides CRUD operations and pagination support.
 */

@RestController()
@RequiredArgsConstructor
@RequestMapping("/api/v1/kindergarten-account")
public class KindergartenAccountController {

    private final KindergartenAccountService kindergartenAccountService;

    /**
     * Creates a new KindergartenAccount.
     * If accountNumber or identificationNumber already exist, throws ApiExceptions with status 409 Conflict.
     * @param model KindergartenAccount object to be created
     * @return ResponseEntity containing the created KindergartenAccount and HTTP status 201 Created
     */
    @PostMapping()
    public ResponseEntity<KindergartenAccount> create(
            @Valid
            @RequestBody
            KindergartenAccount model

    ){
        KindergartenAccount kindergartenAccount = kindergartenAccountService.create(model);
        return ResponseEntity.status(HttpStatus.CREATED).body(kindergartenAccount);
    }

    /**
     * Retrieves a paginated list of KindergartenAccount entities.
     * @param page the page number (0-based index, must be >= 0)
     * @param size the number of records per page (must be >= 1)
     * @return ResponseEntity containing a Page of KindergartenAccount and HTTP status 200 OK
     */
    @GetMapping()
    public ResponseEntity<Page<KindergartenAccount>> findAll(
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page index must be zero or positive")
            int page,
            @Min(value = 1, message = "Page size must be at least 1")
            @RequestParam(defaultValue = "10")
            int size
    ){
        return ResponseEntity.status(HttpStatus.OK).body(kindergartenAccountService.findAll(page, size));
    }

    /**
     * Retrieves a KindergartenAccount by its unique ID.
     * Validates that the ID is not null and greater than zero.
     * If the account does not exist, the service throws an ApiExceptions with status 404 Not Found.
     * @param id the unique identifier of the KindergartenAccount to retrieve
     * @return ResponseEntity containing the KindergartenAccount object and HTTP status 200 OK
     */
    @GetMapping("/{id}")
    public ResponseEntity<KindergartenAccount> findById(
            @PathVariable
            @NotNull(message = "id can not be null")
            @Min(value = 1, message = "id must be greater then zero")
            Integer id
    ){
        return ResponseEntity.status(HttpStatus.OK).body(kindergartenAccountService.findById(id));
    }

    /**
     * Retrieves a KindergartenAccount by its unique account number.
     * Validates that the account number is not null or empty.
     * If the account does not exist, the service throws an ApiExceptions with status 404 Not Found.
     * @param accountNumber the unique account number of the KindergartenAccount to retrieve (from path variable)
     * @return ResponseEntity containing the KindergartenAccount object and HTTP status 200 OK
     */
    @GetMapping("account/{accountNumber}")
    public ResponseEntity<KindergartenAccount> findByAccountNumber(
            @PathVariable
            @NotBlank(message = "account number can not be empty or null")
            String accountNumber
    ){
        return ResponseEntity.status(HttpStatus.OK).body(kindergartenAccountService.findByAccountNumber(accountNumber));
    }

    /**
     * Retrieves a KindergartenAccount by its unique identification number.
     * Validates that the identification number is not null or empty.
     * If the account does not exist, the service throws an ApiExceptions with status 404 Not Found.
     * @param identificationNumber the unique identification number of the KindergartenAccount to retrieve (from path variable)
     * @return ResponseEntity containing the KindergartenAccount object and HTTP status 200 OK
     */
    @GetMapping("identification/{identificationNumber}")
    public ResponseEntity<KindergartenAccount> findByIdentificationNumber(
            @PathVariable
            @NotBlank(message = "identification number can not be empty or null")
            String identificationNumber
    ){
        return ResponseEntity.status(HttpStatus.OK).body(kindergartenAccountService.findByIdentificationNumber(identificationNumber));
    }

    /**
     * Updates an existing KindergartenAccount by its unique ID.
     * Validates that the request body is not null and fields meet defined constraints.
     * Ensures that the ID is not null and greater than zero.
     * If the account does not exist, the service throws an ApiExceptions with status 404 Not Found.
     * @param model the KindergartenAccount object containing updated data (from request body)
     * @param id the unique identifier of the KindergartenAccount to update (from path variable)
     * @return ResponseEntity containing the updated KindergartenAccount object and HTTP status 200 OK
     */
    @PutMapping("/{id}")
    public ResponseEntity<KindergartenAccount> update(
            @RequestBody
            KindergartenAccount model,
            @PathVariable
            @NotNull(message = "id can not be null")
            @Min(value = 1, message = "id must be greater then zero")
            Integer id){
        return ResponseEntity.status(HttpStatus.OK).body(kindergartenAccountService.update(model, id));
    }

    /**
     * Deletes a KindergartenAccount by its unique ID.
     * Ensures that the ID is not null and greater than zero.
     * If the account does not exist, the service throws an ApiExceptions with status 404 Not Found.
     * On successful deletion, returns HTTP 204 No Content without a response body.
     * @param id the unique identifier of the KindergartenAccount to delete (from path variable)
     * @return ResponseEntity with HTTP status 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable
            @NotNull(message = "id can not be null")
            @Min(value = 1, message = "id must be greater then zero")
            Integer id
    ){
        kindergartenAccountService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
