package com.example.KindergartenBillApp.administration.controller;

import com.example.KindergartenBillApp.administration.model.User;
import com.example.KindergartenBillApp.administration.services.UserService;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing User entities.
 * Provides CRUD operations and pagination support.
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;


        /**
         * Creates a new User.
         * If username or email already exist, throws ApiExceptions with status 409 Conflict.
         * @param model User object to be created
         * @return ResponseEntity containing the created User and HTTP status 201 Created
         */
        @PostMapping
        public ResponseEntity<User> create(@RequestBody User model) {
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(model));
        }

    /**
     * Retrieves a paginated list of User entities.
     * @param page the page number (0-based index, must be >= 0)
     * @param size the number of records per page (must be >= 1)
     * @return ResponseEntity containing a Page of User and HTTP status 200 OK
     */
        @GetMapping
        public ResponseEntity<Page<User>> findAll(
                @RequestParam(defaultValue = "0")
                @Min(value = 0, message = "Page index must be zero or positive")
                int page,
                @Min(value = 1, message = "Page size must be at least 1")
                @RequestParam(defaultValue = "10")
                int size
        ) {
            return ResponseEntity.status(HttpStatus.OK).body(userService.findAll(page, size));
        }

        /**
         * Retrieves a User by ID.
         * @param id unique identifier of the User (must be > 0)
         * @return ResponseEntity containing the User and HTTP status 200 OK
         */
        @GetMapping("/{id}")
        public ResponseEntity<User> findById(
                @PathVariable
                @NotNull(message = "id can not be null")
                @Min(value = 1, message = "id must be greater than zero")
                Integer id) {
            return ResponseEntity.status(HttpStatus.OK).body(userService.findById(id));
        }

        /**
         * Retrieves a User by username.
         * @param username unique username (must not be blank)
         * @return ResponseEntity containing the User and HTTP status 200 OK
         */
        @GetMapping("/username/{username}")
        public ResponseEntity<User> findByUsername(
                @PathVariable
                @NotBlank(message = "username can not be empty or null")
                String username) {
            return ResponseEntity.status(HttpStatus.OK).body(userService.findByUsername(username));
        }

        /**
         * Retrieves a User by email.
         * @param email unique email (must not be blank and must be valid format)
         * @return ResponseEntity containing the User and HTTP status 200 OK
         */
        @GetMapping("/email/{email}")
        public ResponseEntity<User> findByEmail(
                @PathVariable
                @NotBlank(message = "email can not be empty or null")
                @Email(message = "email format is not valid")
                String email) {
            return ResponseEntity.status(HttpStatus.OK).body(userService.findByEmail(email));
        }

        /**
         * Updates an existing User by ID.
         * Supports partial updates: only non-null fields are applied.
         * @param model User object containing updated values
         * @param id unique identifier of the User (must be > 0)
         * @return ResponseEntity containing the updated User and HTTP status 200 OK
         */
        @PutMapping("/{id}")
        public ResponseEntity<User> update(
                @RequestBody
                User model,
                @PathVariable
                @NotNull(message = "id can not be null")
                @Min(value = 1, message = "id must be greater than zero")
                Integer id) {
            return ResponseEntity.status(HttpStatus.OK).body(userService.update(model, id));
        }

        /**
         * Deletes a User by ID.
         * @param id unique identifier of the User (must be > 0)
         * @return ResponseEntity with HTTP status 204 No Content
         */
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> delete(
                @PathVariable
                @NotNull(message = "id can not be null")
                @Min(value = 1, message = "id must be greater than zero")
                Integer id) {
            userService.delete(id);
            return ResponseEntity.noContent().build();
        }
    }



