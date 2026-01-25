package com.example.KindergartenBillApp.administration.services;

import com.example.KindergartenBillApp.administration.model.User;
import com.example.KindergartenBillApp.administration.repository.UserRepository;
import com.example.KindergartenBillApp.sharedTools.exceptions.ApiExceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

        /**
         * Creates a new User.
         * Validates uniqueness of username and email before saving.
         * @param model User object to be created
         * @return the created User entity
         */
        public User create(User model) {
            if (userRepository.existsByUsername(model.getUsername())) {
                throw new ApiExceptions("Username " + model.getUsername() + " already exists", HttpStatus.CONFLICT);
            }
            if (userRepository.existsByEmail(model.getEmail())) {
                throw new ApiExceptions("Email " + model.getEmail() + " already exists", HttpStatus.CONFLICT);
            }
            return userRepository.save(model);
        }

    /**
     * Retrieves a paginated list of User entities from the database.
     * @param page the page number (0-based index)
     * @param size the number of records per page
     * @return a Page object containing User entities and pagination metadata
     */
        public Page<User> findAll(int page, int size) {
            Pageable pageable = PageRequest.of(page, size);
            return userRepository.findAll(pageable);
        }

        /**
         * Retrieves a User by ID.
         * @param id unique identifier of the User
         * @return User entity
         */
        public User findById(Integer id) {
            return userRepository.findById(id)
                    .orElseThrow(() -> new ApiExceptions("User with id = " + id + " not found", HttpStatus.NOT_FOUND));
        }

        /**
         * Retrieves a User by username.
         * @param username unique username
         * @return User entity
         */
        public User findByUsername(String username) {
            return userRepository.findByUsername(username)
                    .orElseThrow(() -> new ApiExceptions("User with username = " + username + " not found", HttpStatus.NOT_FOUND));
        }

        /**
         * Retrieves a User by email.
         * @param email unique email
         * @return User entity
         */
        public User findByEmail(String email) {
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new ApiExceptions("User with email = " + email + " not found", HttpStatus.NOT_FOUND));
        }

        /**
         * Partially updates an existing User by ID.
         * Only non-null fields are applied.
         * Validates uniqueness of username and email if changed.
         * @param model User object containing updated values
         * @param id unique identifier of the User
         * @return updated User entity
         */
        public User update(User model, Integer id) {
            User existing = userRepository.findById(id)
                    .orElseThrow(() -> new ApiExceptions("User with id = " + id + " not found", HttpStatus.NOT_FOUND));

            if (model.getUsername() != null
                    && !model.getUsername().equals(existing.getUsername())
                    && userRepository.existsByUsername(model.getUsername())) {
                throw new ApiExceptions("Username " + model.getUsername() +  " already exists", HttpStatus.CONFLICT);
            }

            if (model.getEmail() != null
                    && !model.getEmail().equals(existing.getEmail())
                    && userRepository.existsByEmail(model.getEmail())) {
                throw new ApiExceptions("Email " + model.getEmail() + " already exists", HttpStatus.CONFLICT);
            }

            // Validation for email format
            if (model.getEmail() != null) {
                String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
                if (!model.getEmail().matches(emailRegex)) {
                    throw new ApiExceptions("Invalid email format", HttpStatus.BAD_REQUEST);
                }
                existing.setEmail(model.getEmail());
            }

            if (model.getUsername() != null) existing.setUsername(model.getUsername());
            if (model.getPassword() != null) existing.setPassword(model.getPassword());
            if (model.getRoleId() != null) existing.setRoleId(model.getRoleId());
            if (model.getStatus() != null) existing.setStatus(model.getStatus());

            return userRepository.save(existing);
        }


    /**
     * Deletes a User entity by its unique ID.
     * Retrieves the entity first to allow audit logging before deletion.
     * If the entity does not exist, throws ApiExceptions with status 404 Not Found.
     * @param id the unique identifier of the User entity to delete
     * @throws ApiExceptions if the User entity does not exist
     */
    public void delete(Integer id) {
            User existing = userRepository.findById(id)
                    .orElseThrow(() -> new ApiExceptions("User with id = " + id + " not found", HttpStatus.NOT_FOUND));
            userRepository.delete(existing);
        }

    }



