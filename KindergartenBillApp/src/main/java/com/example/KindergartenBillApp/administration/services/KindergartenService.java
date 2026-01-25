package com.example.KindergartenBillApp.administration.services;

import com.example.KindergartenBillApp.administration.model.Group;
import com.example.KindergartenBillApp.administration.model.Kindergarten;
import com.example.KindergartenBillApp.administration.model.KindergartenAccount;
import com.example.KindergartenBillApp.administration.repository.GroupRepository;
import com.example.KindergartenBillApp.administration.repository.KindergartenAccountRepository;
import com.example.KindergartenBillApp.administration.repository.KindergartenRepository;
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

@RequiredArgsConstructor
@Service
public class KindergartenService {

    private final KindergartenRepository kindergartenRepository;
    private final KindergartenAccountRepository kindergartenAccountRepository;
    private final GroupRepository groupRepository;

    /**
     * Creates a new Kindergarten entity and associates it with an existing KindergartenAccount.
     * Validates that the account exists before saving.
     * If the account does not exist, throws ApiExceptions with status 404 Not Found.
     * If kindergarten name or email already exist, throws ApiExceptions with status 409 Conflict.
     * @param model Kindergarten object containing data from frontend
     * @return the saved Kindergarten entity
     */
    public Kindergarten create(Kindergarten model){
        if(model.getAccount()==null || model.getAccount().getId()==null){
            throw new ApiExceptions("Account id must be provided", HttpStatus.BAD_REQUEST);
        }

        Integer accountId = model.getAccount().getId();
        KindergartenAccount account = kindergartenAccountRepository.findById(accountId)
                .orElseThrow(()-> new ApiExceptions("Kindergarten account with id = " + accountId + " not found", HttpStatus.NOT_FOUND));

        if(kindergartenRepository.existsByName(model.getName())){
            throw new ApiExceptions("Kindergarten with name " + model.getName() + " already exists", HttpStatus.CONFLICT);
        }

        if(kindergartenRepository.existsByEmail(model.getEmail())){
            throw new ApiExceptions("Kindergarten with email " + model.getEmail() + " already exists", HttpStatus.CONFLICT);
        }

        model.setAccount(account);
        return kindergartenRepository.save(model);
    }

    /**
     * Retrieves a paginated list of Kindergarten entities from the database.
     * @param page the page number (0-based index)
     * @param size the number of records per page
     * @return a Page object containing Kindergarten entities and pagination metadata
     */
    public Page<Kindergarten> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return kindergartenRepository.findAll(pageable);
    }

    /**
     * Retrieves a Kindergarten entity by its unique ID.
     * If the kindergarten does not exist, throws ApiExceptions with status 404 Not Found.
     * @param id the unique identifier of the Kindergarten to retrieve
     * @return Kindergarten object if found
     * @throws ApiExceptions if no Kindergarten with the given ID exists
     */
    public Kindergarten findById(Integer id){
        return kindergartenRepository.findById(id)
                .orElseThrow(()-> new ApiExceptions("Kindergarten with id = " + id + " not found", HttpStatus.NOT_FOUND));
    }

    /**
     * Retrieves a Kindergarten entity by its unique name.
     * If the kindergarten does not exist, throws ApiExceptions with status 404 Not Found.
     * @param name the unique name of the Kindergarten to retrieve
     * @return Kindergarten object if found
     * @throws ApiExceptions if no Kindergarten with the given name exists
     */
    public Kindergarten findByName(String name){
        return kindergartenRepository.findByName(name)
                .orElseThrow(()-> new ApiExceptions("Kindergarten with name " + name + " not found", HttpStatus.NOT_FOUND));
    }

    /**
     * Retrieves a Kindergarten entity by its unique email address.
     * If the kindergarten does not exist, throws ApiExceptions with status 404 Not Found.
     * @param email the unique email address of the Kindergarten to retrieve
     * @return Kindergarten object if found
     * @throws ApiExceptions if no Kindergarten with the given email exists
     */
    public Kindergarten findByEmail(String email){
        return kindergartenRepository.findByEmail(email)
                .orElseThrow(()-> new ApiExceptions("Kindergarten with email " + email + " not found", HttpStatus.NOT_FOUND));
    }

    /**
     * Updates an existing Kindergarten entity by its unique ID.
     * Supports partial updates: only non-null fields from the request are applied.
     * If the kindergarten does not exist, throws ApiExceptions with status 404 Not Found.
     * If account is provided, validates that the account exists before updating.
     * If name or email are changed, validates uniqueness to prevent duplicates.
     * @param model Kindergarten object containing updated data (fields may be partial)
     * @param id the unique identifier of the Kindergarten to update
     * @return the updated Kindergarten entity
     * @throws ApiExceptions if the kindergarten does not exist, if account does not exist,
     *                       or if name/email conflict with existing records
     */
    public Kindergarten update(Kindergarten model, Integer id){

        Kindergarten existing = kindergartenRepository.findById(id)
                .orElseThrow(()-> new ApiExceptions("Kindergarten with id = " + id + " not found", HttpStatus.NOT_FOUND));

        if (model.getAccount() != null && model.getAccount().getId() != null){
            KindergartenAccount account = kindergartenAccountRepository.findById(model.getAccount().getId())
                    .orElseThrow(()-> new ApiExceptions("Kindergarten account with id = " + model.getAccount().getId() + " not found", HttpStatus.NOT_FOUND));
            existing.setAccount(account);
        }

        if(model.getName()!=null
                && !model.getName().equals(existing.getName())
                && kindergartenRepository.existsByName(model.getName())){
            throw new ApiExceptions("Kindergarten name already exists", HttpStatus.CONFLICT);
        }

        if(model.getEmail()!=null
                && !model.getEmail().equals(existing.getEmail())
                && kindergartenRepository.existsByEmail(model.getEmail())){
            throw new ApiExceptions("Kindergarten email already exists", HttpStatus.CONFLICT);

        }

        //validation for email
        if (model.getEmail() != null) {
            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
            if (!model.getEmail().matches(emailRegex)) {
                throw new ApiExceptions("Invalid email format", HttpStatus.BAD_REQUEST);}
            existing.setEmail(model.getEmail());
        }

        if(model.getName()!=null) existing.setName(model.getName());
        if(model.getAddress()!=null) existing.setAddress(model.getAddress());
        if(model.getPhoneNumber()!=null) existing.setPhoneNumber(model.getPhoneNumber());
        if(model.getLogo()!=null) existing.setLogo(model.getLogo());

        return kindergartenRepository.save(existing);
    }

    /**
     * Deletes a Kindergarten entity by its unique ID.
     * Retrieves the entity first to allow audit logging before deletion.
     * If the entity does not exist, throws ApiExceptions with status 404 Not Found.
     * @param id the unique identifier of the Kindergarten entity to delete
     * @throws ApiExceptions if the Kindergarten entity does not exist
     */
    public void delete(Integer id){
        Kindergarten model = kindergartenRepository.findById(id)
                .orElseThrow(()-> new ApiExceptions("Kindergarten with id " + id + " not found", HttpStatus.NOT_FOUND));
        kindergartenRepository.delete(model);
    }

    /**
     * Adds one or more groups to a kindergarten without removing existing ones.
     *
     * <p>This method retrieves the kindergarten by its ID, then looks up all groups
     * based on the provided IDs and adds them to the kindergarten's group set.
     * Existing groups remain associated with the kindergarten. If the kindergarten
     * or any of the groups cannot be found, an {@link ApiExceptions} is thrown.</p>
     *
     * @param kindergartenId the ID of the kindergarten to which groups should be added
     * @param groupIds       a set of group IDs to be associated with the kindergarten
     * @return the updated {@link Kindergarten} entity with the newly added groups
     * @throws ApiExceptions if the kindergarten or any group cannot be found
     */
    @Transactional
    public Kindergarten addGroupsToKindergarten(Integer kindergartenId, Set<Integer> groupIds){
        Kindergarten kindergarten = kindergartenRepository.findById(kindergartenId)
                .orElseThrow(()-> new ApiExceptions("Kindergarten with id " + kindergartenId + " not found", HttpStatus.NOT_FOUND));
        Set<Group> groups = new HashSet<>();

        groupIds.forEach(id -> groups.add(groupRepository.findById(id).orElseThrow(()-> new ApiExceptions("Group with id " + id + " not found", HttpStatus.NOT_FOUND))));
        kindergarten.getGroup().addAll(groups);
        return kindergartenRepository.save(kindergarten);
    }

    /**
     * Removes one or more groups from a kindergarten.
     *
     * <p>This method retrieves the kindergarten by its ID, then looks up all groups
     * based on the provided IDs and removes them from the kindergarten's group set.
     * If the kindergarten or any of the groups cannot be found, an {@link ApiExceptions}
     * is thrown.</p>
     *
     * @param kindergartenId the ID of the kindergarten from which groups should be removed
     * @param groupIds       a set of group IDs to be removed from the kindergarten
     * @return the updated {@link Kindergarten} entity without the removed groups
     * @throws ApiExceptions if the kindergarten or any group cannot be found
     */
    @Transactional
    public Kindergarten removeGroupsFromKindergarten(Integer kindergartenId, Set<Integer> groupIds){
        Kindergarten kindergarten = kindergartenRepository.findById(kindergartenId)
                .orElseThrow(()-> new ApiExceptions("Kindergarten with id " + kindergartenId + " not found", HttpStatus.NOT_FOUND));

        groupIds.forEach(id -> kindergarten.getGroup().remove(groupRepository.findById(id)
                .orElseThrow(()-> new ApiExceptions("Group with id " + id + " not found", HttpStatus.NOT_FOUND))));
        return kindergartenRepository.save(kindergarten);
    }

    /**
     * Clears all groups associated with a kindergarten.
     *
     * <p>This method retrieves the kindergarten by its ID and removes all groups
     * from its group set. If the kindergarten cannot be found, an {@link ApiExceptions}
     * is thrown.</p>
     *
     * @param kindergartenId the ID of the kindergarten whose groups should be cleared
     * @return the updated {@link Kindergarten} entity without any groups
     * @throws ApiExceptions if the kindergarten cannot be found
     */
    @Transactional
    public Kindergarten clearGroupsFromKindergarten(Integer kindergartenId){
        Kindergarten kindergarten = kindergartenRepository.findById(kindergartenId)
                .orElseThrow(()-> new ApiExceptions("Kindergarten with id " + kindergartenId + " not found", HttpStatus.NOT_FOUND));

        kindergarten.getGroup().clear();
        return kindergartenRepository.save(kindergarten);
    }
}