package com.example.KindergartenBillApp.administration.services;

import com.example.KindergartenBillApp.administration.model.*;
import com.example.KindergartenBillApp.administration.repository.*;
import com.example.KindergartenBillApp.sharedTools.exceptions.ApiExceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ChildService {

    private final ChildRepository childRepository;
    private final GroupRepository groupRepository;
    private final KindergartenRepository kindergartenRepository;
    private final ActivityRepository activityRepository;
    private final ParentRepository parentRepository;

    /**
     * Creates a new Child entity.
     * Validates that Group, Kindergarten, and Parent IDs are provided and exist in the database.
     * Checks for uniqueness based on name, surname, and parent reference.
     * If IDs are missing, throws ApiExceptions with status 400 Bad Request.
     * If referenced entities do not exist, throws ApiExceptions with status 404 Not Found.
     * If a duplicate child exists with the same name, surname, and parent, throws ApiExceptions with status 409 Conflict.
     *
     * @param model Child object to be created
     * @return the created Child entity
     * @throws ApiExceptions if validation fails, referenced entities do not exist, or uniqueness constraint is violated
     */
    public Child create(Child model) {

        if (model.getGroup() == null || model.getGroup().getId() == null) {
            throw new ApiExceptions("Group id must be provided", HttpStatus.BAD_REQUEST);
        }

        if (model.getKindergarten() == null || model.getKindergarten().getId() == null) {
            throw new ApiExceptions("Kindergarten id must be provided", HttpStatus.BAD_REQUEST);
        }

        childRepository.findByNameIgnoreCaseAndSurnameIgnoreCaseAndParent_Id(
                model.getName(),
                model.getSurname(),
                model.getParent().getId()).ifPresent(existingChild -> {
                    throw new ApiExceptions("Child already exists with same name, surname and parent",
                            HttpStatus.CONFLICT);
        });



        Integer groupId = model.getGroup().getId();
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ApiExceptions("Group id " + groupId + " not found", HttpStatus.NOT_FOUND));

        Integer kindergartenId = model.getKindergarten().getId();
        Kindergarten kindergarten = kindergartenRepository.findById(kindergartenId)
                .orElseThrow(() -> new ApiExceptions("Kindergarten id " + kindergartenId + " not found", HttpStatus.NOT_FOUND));
        Integer parentId = model.getParent().getId();
        Parent parent = parentRepository.findById(model.getParent().getId()).orElseThrow(() -> new ApiExceptions("Parent id " + parentId + " not found", HttpStatus.NOT_FOUND));

        model.setGroup(group);
        model.setKindergarten(kindergarten);
        model.setParent(parent);

        return childRepository.save(model);
    }

    /**
     * Retrieves a paginated list of Child entities from the database.
     *
     * @param page the page number (0-based index)
     * @param size the number of records per page
     * @return a Page object containing Child entities and pagination metadata
     */
    public Page<Child> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return childRepository.findAll(pageable);
    }

    /**
     * Retrieves a Child entity by its unique ID.
     * If the entity does not exist, throws ApiExceptions with status 404 Not Found.
     *
     * @param id unique identifier of the Child entity
     * @return the Child entity with the given ID
     * @throws ApiExceptions if the entity does not exist
     */
    public Child findById(Integer id) {
        return childRepository.findById(id)
                .orElseThrow(() -> new ApiExceptions("Child with id " + id + " not found", HttpStatus.NOT_FOUND));
    }

    /**
     * Updates an existing Child entity by its ID.
     * Allows partial updates: only non-null fields from the input model are applied.
     * Validates that referenced Group and Kindergarten IDs exist before updating.
     * Checks uniqueness constraint (name, surname, parent) only if all three fields are provided.
     * If entity does not exist, throws ApiExceptions with status 404 Not Found.
     * If uniqueness constraint is violated, throws ApiExceptions with status 409 Conflict.
     *
     * @param model Child object containing updated fields
     * @param id unique identifier of the Child entity to update
     * @return the updated Child entity
     * @throws ApiExceptions if entity does not exist, referenced entities do not exist,
     *                       or uniqueness constraint is violated
     */
    public Child update(Child model, Integer id) {
        Child existing = childRepository.findById(id)
                .orElseThrow(() -> new ApiExceptions("Child with id " + id + " not found", HttpStatus.NOT_FOUND));

        if (model.getGroup() != null && model.getGroup().getId() != null) {
            Group group = groupRepository.findById(model.getGroup().getId())
                    .orElseThrow(() -> new ApiExceptions("Group id " + model.getGroup().getId() + " not found", HttpStatus.NOT_FOUND));
            existing.setGroup(group);
        }

        if (model.getKindergarten() != null && model.getKindergarten().getId() != null) {
            Kindergarten kindergarten = kindergartenRepository.findById(model.getKindergarten().getId())
                    .orElseThrow(() -> new ApiExceptions("Kindergarten id " + model.getKindergarten().getId() + " not found", HttpStatus.NOT_FOUND));
            existing.setKindergarten(kindergarten);

        }

        if (model.getParent() != null && model.getParent().getId() != null) {
            Parent parent = parentRepository.findById(model.getParent().getId())
                    .orElseThrow(() -> new ApiExceptions("Parent id " + model.getParent().getId() + " not found", HttpStatus.NOT_FOUND));
            existing.setParent(parent);
        }


        // Provera jedinstvenosti samo ako su sva polja prosleđena
        if (model.getName() != null &&
                model.getSurname() != null &&
                model.getParent() != null &&
                model.getParent().getId() != null
                ) {

            Child uniqueCheck = childRepository
                    .findByNameIgnoreCaseAndSurnameIgnoreCaseAndParent_Id(
                            model.getName(),
                            model.getSurname(),
                            model.getParent().getId()
                    ).orElse(null);

            if (uniqueCheck != null && !uniqueCheck.getId().equals(id)) {
                throw new ApiExceptions(
                        "Child with the same name, surname and parent already exists",
                        HttpStatus.CONFLICT
                );
            }
        }

        if (model.getName() != null) existing.setName(model.getName());
        if (model.getSurname() != null) existing.setSurname(model.getSurname());
        if (model.getSiblingOrder() != null) existing.setSiblingOrder(model.getSiblingOrder());
        if (model.getBirthday() != null) existing.setBirthday(model.getBirthday());
        if (model.getStatus() != null) existing.setStatus(model.getStatus());

        return childRepository.save(existing);
    }

    /**
     * Deletes a Child entity by its unique ID.
     * If the entity does not exist, throws ApiExceptions with status 404 Not Found.
     * On successful deletion, no content is returned.
     *
     * @param id unique identifier of the Child entity to delete
     * @throws ApiExceptions if the entity does not exist
     */
    public void delete(Integer id) {
        Child child = childRepository.findById(id)
                .orElseThrow(() -> new ApiExceptions("Child with id " + id + " not found", HttpStatus.NOT_FOUND));
        childRepository.delete(child);
    }

    /**
     * Adds one or more activities to a child.
     *
     * <p>This method retrieves the child by its ID, then looks up all activities
     * based on the provided IDs and adds them to the child's activity set.
     * If the child or any of the activities cannot be found, an {@link ApiExceptions}
     * is thrown.</p>
     *
     * @param childId      the ID of the child to which activities should be added
     * @param activitiesId a set of activity IDs to be added
     * @return the updated {@link Child} entity with the newly added activities
     * @throws ApiExceptions if the child or any activity cannot be found
     */
    @Transactional
    public Child addActivitiesToChild(Integer childId, Set<Integer> activitiesId) {
        Child child = childRepository.findById(childId)
                .orElseThrow(() -> new ApiExceptions("Child with id " + childId + " not found", HttpStatus.NOT_FOUND));
        Set<Activity> activities = new HashSet<>();
        activitiesId.forEach(id -> activities.add(activityRepository.findById(id)
                .orElseThrow(() -> new ApiExceptions("Activity with id " + id + " not found", HttpStatus.NOT_FOUND))));
        child.getActivities().addAll(activities);
        return childRepository.save(child);
    }

    /**
     * Removes one or more activities from a child.
     *
     * <p>This method retrieves the child by its ID, then looks up all activities
     * based on the provided IDs and removes them from the child's activity set.
     * If the child or any of the activities cannot be found, an {@link ApiExceptions}
     * is thrown.</p>
     *
     * @param childId      the ID of the child from which activities should be removed
     * @param activitiesId a set of activity IDs to be removed
     * @return the updated {@link Child} entity without the removed activities
     * @throws ApiExceptions if the child or any activity cannot be found
     */
    @Transactional
    public Child removeActivitiesFromChild(Integer childId, Set<Integer> activitiesId) {
        Child child = childRepository.findById(childId)
                .orElseThrow(() -> new ApiExceptions("Child with id " + childId + " not found", HttpStatus.NOT_FOUND));

        activitiesId.forEach(id -> child.getActivities().remove(activityRepository
                .findById(id).orElseThrow(() -> new ApiExceptions("Activity with id " + id + " not found", HttpStatus.NOT_FOUND))));
        return childRepository.save(child);
    }

    /**
     * Clears all activities associated with a child.
     *
     * <p>This method retrieves the child by its ID and removes all activities
     * from its activity set. If the child cannot be found, an {@link ApiExceptions}
     * is thrown.</p>
     *
     * @param childId the ID of the child whose activities should be cleared
     * @return the updated {@link Child} entity without any activities
     * @throws ApiExceptions if the child cannot be found
     */
    @Transactional
    public Child clearActivitiesFromChild(Integer childId) {
        Child child = childRepository.findById(childId)
                .orElseThrow(() -> new ApiExceptions("Child with id " + childId + " not found", HttpStatus.NOT_FOUND));

        child.getActivities().clear();
        return childRepository.save(child);
    }

}
