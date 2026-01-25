package com.example.KindergartenBillApp.administration.controller;

import com.example.KindergartenBillApp.administration.model.MailHistory;
import com.example.KindergartenBillApp.administration.services.MailHistoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/mail-history")
public class MailHistoryController {

    private final MailHistoryService mailHistoryService;

    /**
     * Creates a new MailHistory entry.
     * Used to persist information about sent emails for auditing or tracking purposes.
     *
     * @param model MailHistory object to be created
     * @return ResponseEntity containing the created MailHistory and HTTP status 201 Created
     */
    @PostMapping()
    public ResponseEntity<MailHistory> create(
            @RequestBody
            @Valid
            MailHistory model
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mailHistoryService.create(model));
    }

    /**
     * Retrieves a paginated list of MailHistory entries.
     *
     * @param page the page number (0-based index, must be >= 0)
     * @param size the number of records per page (must be >= 1)
     * @return ResponseEntity containing a Page of MailHistory and HTTP status 200 OK
     */
    @GetMapping()
    public ResponseEntity<Page<MailHistory>> getAll(
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page index must be zero or positive")
            int page,
            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Page size must be at least 1")
            int size
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(mailHistoryService.findAll(page, size));
    }

    /**
     * Retrieves a MailHistory entry by its unique ID.
     * If the entry does not exist, the service throws ApiExceptions with status 404 Not Found.
     *
     * @param id the unique identifier of the MailHistory (must be greater than zero)
     * @return ResponseEntity containing the MailHistory and HTTP status 200 OK
     */
    @GetMapping("/{id}")
    public ResponseEntity<MailHistory> findById(
            @PathVariable
            @NotNull(message = "id can not be null")
            @Min(value = 1, message = "id must be greater than zero")
            Integer id
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(mailHistoryService.findById(id));
    }

    /**
     * Retrieves MailHistory entries by recipient addresses.
     * Accepts the addresses as a query parameter for flexibility (supports spaces and special characters).
     * Returns a list of MailHistory entities, which may be empty if no records match.
     *
     * @param addresses email addresses to search for (must not be blank)
     * @return ResponseEntity containing a list of MailHistory and HTTP status 200 OK
     */
    @GetMapping("/addresses")
    public ResponseEntity<List<MailHistory>> findByAddresses(
            @RequestParam
            @NotBlank(message = "addresses can not be empty or null")
            String addresses
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(mailHistoryService.findByAddresses(addresses));
    }

    /**
     * Retrieves MailHistory entries by message content.
     * Accepts the message as a query parameter for flexibility (supports spaces and special characters).
     * Returns a list of MailHistory entities, which may be empty if no records match.
     *
     * @param message text to search for in the message field (must not be blank)
     * @return ResponseEntity containing a list of MailHistory and HTTP status 200 OK
     */
    @GetMapping("/message")
    public ResponseEntity<List<MailHistory>> findByMessage(
            @RequestParam
            @NotBlank(message = "message can not be empty or null")
            String message
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(mailHistoryService.findByMessage(message));
    }
}


