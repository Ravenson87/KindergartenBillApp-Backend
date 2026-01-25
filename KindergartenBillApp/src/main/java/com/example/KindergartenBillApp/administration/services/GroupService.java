package com.example.KindergartenBillApp.administration.services;

import com.example.KindergartenBillApp.administration.model.Group;
import com.example.KindergartenBillApp.administration.repository.GroupRepository;
import com.example.KindergartenBillApp.sharedTools.exceptions.ApiExceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GroupService {

    private final GroupRepository groupRepository;

    /**
     * Creates a new Groups entity.
     * Validates that the group name is unique before saving to prevent duplicates.
     * If a group with the same name already exists, throws ApiExceptions with status 409 Conflict.
     * @param group Groups object to be created
     * @return the created Groups entity
     * @throws ApiExceptions if a group with the same name already exists
     */
    public Group create(Group group){
        if(groupRepository.existsByName(group.getName())){
            throw new ApiExceptions("group with name " + group.getName() + " already exists", HttpStatus.CONFLICT);
        }
        return groupRepository.save(group);
    }

    /**
     * Retrieves a paginated list of Groups entities from the database.
     * @param page the page number (0-based index)
     * @param size the number of records per page
     * @return a Page object containing Groups entities and pagination metadata
     */
    public Page<Group> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return groupRepository.findAll(pageable);
    }

    /**
     * Retrieves a Groups entity by its unique ID.
     * If the entity does not exist, throws ApiExceptions with status 404 Not Found.
     * @param id the unique identifier of the Groups entity
     * @return the Group entity with the given ID
     * @throws ApiExceptions if the Groups entity does not exist
     */
    public Group findById(Integer id){
        return groupRepository.findById(id)
                .orElseThrow(() -> new ApiExceptions("group with id " + id + " not found", HttpStatus.NOT_FOUND));
    }

    /**
     * Retrieves a Groups entity by its unique name.
     * If the entity does not exist, throws ApiExceptions with status 404 Not Found.
     * @param name the unique name of the Groups entity
     * @return the Groups entity with the given name
     * @throws ApiExceptions if the Groups entity does not exist
     */
    public Group findByName(String name){
        return groupRepository.findByName(name)
                .orElseThrow(() -> new ApiExceptions("group with name " + name + " not found", HttpStatus.NOT_FOUND));
    }

    /**
     * Partially updates an existing Groups entity by its ID.
     * Only non-null fields from the provided model are applied to the existing entity.
     * If the Groups with the given ID does not exist, throws ApiExceptions with status 404 Not Found.
     * If the name is changed, validates uniqueness to prevent duplicate group names.
     * @param model Groups object containing updated values (only non-null fields are applied)
     * @param id the unique identifier of the Groups entity to update
     * @return the updated Groups entity
     * @throws ApiExceptions if the Groups entity does not exist or if the new name conflicts with existing records
     */
    public Group update(Group model, Integer id){

        Group existing = groupRepository.findById(id)
                .orElseThrow(() -> new ApiExceptions("group with id " + id + " not found", HttpStatus.NOT_FOUND));

        if(model.getName() != null
                && !existing.getName().equals(model.getName())
                && groupRepository.existsByName(model.getName())){
            throw new ApiExceptions("group with name " + model.getName() + " already exists", HttpStatus.CONFLICT);
        }

        if(model.getName() != null) existing.setName(model.getName());
        if(model.getPrice()!= null) existing.setPrice(model.getPrice());
        if(model.getDiscount()!= null) existing.setDiscount(model.getDiscount());
        if(model.getActive()!= null) existing.setActive(model.getActive());

        return groupRepository.save(existing);
    }

    /**
     * Deletes a Groups entity by its unique ID.
     * Retrieves the entity first to allow audit logging before deletion.
     * If the entity does not exist, throws ApiExceptions with status 404 Not Found.
     * @param id the unique identifier of the Groups entity to delete
     * @throws ApiExceptions if the Groups entity does not exist
     */
    public void delete(Integer id){
        //Ovako sam uradio zbob audit logova
        Group model = groupRepository.findById(id)
                .orElseThrow(() -> new ApiExceptions("group with id " + id + " not found", HttpStatus.NOT_FOUND));
        groupRepository.delete(model);
    }
}
