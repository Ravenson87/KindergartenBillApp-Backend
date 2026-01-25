package com.example.KindergartenBillApp.administration.services;

import com.example.KindergartenBillApp.administration.model.Parent;
import com.example.KindergartenBillApp.administration.repository.ParentRepository;
import com.example.KindergartenBillApp.sharedTools.exceptions.ApiExceptions;
import com.example.KindergartenBillApp.sharedTools.helpers.Helper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParentService {

    private final ParentRepository parentRepository;

    /**
     * Creates a new Parent entity.
     * Validates that the email is unique before saving.
     * If the email already exists in the database, throws ApiExceptions with status 409 Conflict.
     *
     * @param model Parent object to be created
     * @return the created Parent entity
     * @throws ApiExceptions if the email already exists
     */
    public Parent create(Parent model){
        if(parentRepository.existsByEmail(model.getEmail())){
            throw new ApiExceptions("Email " + model.getEmail() + " already exists", HttpStatus.CONFLICT);
        }
        return parentRepository.save(model);
    }

    /**
     * Retrieves a paginated list of Parent entities.
     * Accepts page and size parameters with default values defined in the controller.
     * Returns a Page object containing Parent entities and pagination metadata.
     *
     * @param page the page number (0-based index, must be >= 0)
     * @param size the number of records per page (must be >= 1)
     * @return a Page of Parent entities
     */
    public Page<Parent> findAll(int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return parentRepository.findAll(pageable);
    }

    /**
     * Retrieves a Parent entity by its unique ID.
     * If the entity does not exist, throws ApiExceptions with status 404 Not Found.
     *
     * @param id unique identifier of the Parent entity
     * @return the Parent entity with the given ID
     * @throws ApiExceptions if the entity does not exist
     */
    public Parent findById(Integer id){
        return parentRepository.findById(id)
                .orElseThrow(() -> new ApiExceptions("Parent with id " + id + " not found", HttpStatus.NOT_FOUND));
    }

    /**
     * Retrieves a Parent entity by its email address.
     * If the entity does not exist, throws ApiExceptions with status 404 Not Found.
     *
     * @param email unique email address of the Parent entity
     * @return the Parent entity with the given email
     * @throws ApiExceptions if the entity does not exist
     */
    public Parent findByEmail(String email){
        return parentRepository.findByEmail(email)
                .orElseThrow(()-> new ApiExceptions("Parent with email " + email + " not found", HttpStatus.NOT_FOUND));
    }

    /**
     * Updates an existing Parent entity by its ID.
     * Allows partial updates: only non-null fields from the input model are applied.
     * Validates that the new email is unique and has a valid format before updating.
     * If entity does not exist, throws ApiExceptions with status 404 Not Found.
     * If email already exists, throws ApiExceptions with status 409 Conflict.
     * If email format is invalid, throws ApiExceptions with status 400 Bad Request.
     *
     * @param model Parent object containing updated fields
     * @param id unique identifier of the Parent entity to update
     * @return the updated Parent entity
     * @throws ApiExceptions if entity does not exist, email already exists, or email format is invalid
     */
    public Parent update(Parent model, Integer id){

        Parent existing = parentRepository.findById(id)
                .orElseThrow(() -> new ApiExceptions("Parent with id " + id + " ", HttpStatus.NOT_FOUND));

        if(model.getEmail() != null
        && !model.getEmail().equals(existing.getEmail())
        && parentRepository.existsByEmail(model.getEmail())){
            throw new ApiExceptions("Email " + model.getEmail() + " already exists", HttpStatus.CONFLICT);
        }

        //Izdvojio sam poseban metod, cisto vezbe radi, iako mislim da nije neophodno, nego da mi je to na pameti za ubuduce,
        // kada na vise mesta koristim istu funkciju da je izdvojim u helper
        if(model.getEmail() != null){
            if(Helper.emailCheck(model.getEmail())){
                existing.setEmail(model.getEmail());
            }else {
                throw new ApiExceptions("Invalid email format", HttpStatus.BAD_REQUEST);
            }
        }

        if(model.getName() != null) existing.setName(model.getName());
        if(model.getSurname() != null) existing.setSurname(model.getSurname());
        if(model.getAddress() != null) existing.setAddress(model.getAddress());
        return parentRepository.save(existing);

    }

    /**
     * Deletes a Parent entity by its unique ID.
     * First retrieves the entity to ensure it exists.
     * If the entity does not exist, throws ApiExceptions with status 404 Not Found.
     *
     * @param id unique identifier of the Parent entity to delete
     * @throws ApiExceptions if the entity does not exist
     */
    public void delete(Integer id){
        Parent parent = parentRepository.findById(id)
                .orElseThrow(() -> new ApiExceptions("Parent with id " + id + " not found", HttpStatus.NOT_FOUND));
        parentRepository.delete(parent);
    }
}
