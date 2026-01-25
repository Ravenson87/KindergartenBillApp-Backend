package com.example.KindergartenBillApp.administration.services;

import com.example.KindergartenBillApp.administration.model.Activity;
import com.example.KindergartenBillApp.administration.repository.ActivityRepository;
import com.example.KindergartenBillApp.sharedTools.exceptions.ApiExceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;

    /**
     * Creates a new Activities entity.
     * Validates that the activity name is unique before saving to prevent duplicates.
     * If an activity with the same name already exists, throws ApiExceptions with status 409 Conflict.
     * @param model Activities object to be created
     * @return the created Activity entity
     * @throws ApiExceptions if an activity with the same name already exists
     */

    public Activity create(Activity model) {

        if(activityRepository.existsByName(model.getName())){
            throw new ApiExceptions("Activities " + model.getName() + " already exists", HttpStatus.CONFLICT);
        }

        return activityRepository.save(model);
    }

    /**
     * Retrieves a paginated list of Activities entities from the database.
     * @param page the page number (0-based index)
     * @param size the number of records per page
     * @return a Page object containing Activity entities and pagination metadata
     */
    public Page<Activity> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return activityRepository.findAll(pageable);
    }

    /**
     * Retrieves an Activities entity by its unique ID.
     * If the entity does not exist, throws ApiExceptions with status 404 Not Found.
     * @param id the unique identifier of the Activities entity
     * @return the Activity entity with the given ID
     * @throws ApiExceptions if the Activities entity does not exist
     */
    public Activity findById(Integer id) {
        return activityRepository.findById(id)
                .orElseThrow(()-> new ApiExceptions("Activities with id = " + id + " not found", HttpStatus.NOT_FOUND));
    }

    /**
     * Retrieves an Activities entity by its unique name.
     * If the entity does not exist, throws ApiExceptions with status 404 Not Found.
     * @param name the unique name of the Activities entity
     * @return the Activities entity with the given name
     * @throws ApiExceptions if the Activities entity does not exist
     */
    public Activity findByName(String name) {
        return activityRepository.findByName(name)
                .orElseThrow(()-> new ApiExceptions("Activities with name = " + name + " not found", HttpStatus.NOT_FOUND));
    }

    /**
     * Partially updates an existing Activities entity by its ID.
     * Only non-null fields from the provided model are applied to the existing entity.
     * If the Activities with the given ID does not exist, throws ApiExceptions with status 404 Not Found.
     * If the name is changed, validates uniqueness to prevent duplicate activity names.
     * @param model Activities object containing updated values (only non-null fields are applied)
     * @param id the unique identifier of the Activities to update
     * @return the updated Activities entity
     * @throws ApiExceptions if the Activities does not exist or if the new name conflicts with existing records
     */
    public Activity update(Activity model, Integer id) {

        Activity activity = activityRepository.findById(id)
                .orElseThrow(() -> new ApiExceptions("Activities with id = " + id + " does not exist", HttpStatus.NOT_FOUND));

        if(model.getName() != null
                && !model.getName().equals(activity.getName())
                && activityRepository.existsByName(model.getName())
        ){
            throw new ApiExceptions("Activities " + model.getName() + " already exists", HttpStatus.CONFLICT);
        }

        if(model.getName() !=null) activity.setName(model.getName());
        if(model.getPrice() != null) activity.setPrice(model.getPrice());
        if(model.getStatus()!= null) activity.setStatus(model.getStatus());

        return activityRepository.save(activity);
    }

    /**
     * Deletes an Activities entity by its unique ID.
     * Retrieves the entity first to allow audit logging before deletion.
     * If the entity does not exist, throws ApiExceptions with status 404 Not Found.
     * @param id the unique identifier of the Activity entity to delete
     * @throws ApiExceptions if the Activities entity does not exist
     */
    public void deleteById(Integer id) {
        Activity model = activityRepository.findById(id)
                .orElseThrow(() -> new ApiExceptions("Activities with id = " + id + " not found", HttpStatus.NOT_FOUND));
        activityRepository.delete(model);
    }

}
