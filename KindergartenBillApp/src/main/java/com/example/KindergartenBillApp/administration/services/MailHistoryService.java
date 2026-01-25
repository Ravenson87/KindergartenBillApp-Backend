package com.example.KindergartenBillApp.administration.services;

import com.example.KindergartenBillApp.administration.model.MailHistory;
import com.example.KindergartenBillApp.administration.repository.MailHistoryRepository;
import com.example.KindergartenBillApp.sharedTools.exceptions.ApiExceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MailHistoryService {
    private final MailHistoryRepository mailHistoryRepository;

    /**
     * Creates a new MailHistory entity.
     * Used to persist information about sent emails for auditing or tracking purposes.
     * Simply saves the MailHistory object without additional validation.
     *
     * @param model MailHistory object to be created
     * @return the created MailHistory entity
     */
    public MailHistory create(MailHistory model) {
        return mailHistoryRepository.save(model);
    }

    /**
     * Retrieves a paginated list of MailHistory entities.
     * Accepts page and size parameters with default values defined in the controller.
     * Returns a Page object containing MailHistory entities and pagination metadata,
     * including total pages, total elements, and current page information.
     *
     * @param page the page number (0-based index, must be >= 0)
     * @param size the number of records per page (must be >= 1)
     * @return a Page of MailHistory entities
     */

    public Page<MailHistory> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return mailHistoryRepository.findAll(pageable);
    }

    /**
     * Retrieves a MailHistory entity by its unique ID.
     * If the entity does not exist, throws ApiExceptions with status 404 Not Found.
     *
     * @param id unique identifier of the MailHistory entity
     * @return the MailHistory entity with the given ID
     * @throws ApiExceptions if the entity does not exist
     */
    public MailHistory findById(Integer id) {
        return mailHistoryRepository.findById(id)
                .orElseThrow(() -> new ApiExceptions("MailHistory with id " + id + " not found", HttpStatus.NOT_FOUND));
    }

    /**
     * Retrieves MailHistory entities by recipient addresses.
     * Searches the database for entries where the addresses field matches the provided value.
     * Returns a list of MailHistory entities, which may be empty if no records match.
     *
     * @param addresses email addresses to search for
     * @return list of MailHistory entities matching the given addresses
     */
    public List<MailHistory> findByAddresses(String addresses) {
        return mailHistoryRepository.findByAddresses(addresses);
    }

    /**
     * Retrieves MailHistory entities by message content.
     * Searches the database for entries where the message field matches the provided value.
     * Returns a list of MailHistory entities, which may be empty if no records match.
     *
     * @param message text to search for in the message field
     * @return list of MailHistory entities matching the given message content
     */
    public List<MailHistory> findByMessage(String message) {
        return mailHistoryRepository.findByMessage(message);
    }
}
