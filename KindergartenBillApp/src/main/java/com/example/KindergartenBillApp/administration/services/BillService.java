package com.example.KindergartenBillApp.administration.services;

import com.example.KindergartenBillApp.administration.model.Bill;
import com.example.KindergartenBillApp.administration.model.Child;
import com.example.KindergartenBillApp.administration.model.Kindergarten;
import com.example.KindergartenBillApp.administration.repository.BillRepository;
import com.example.KindergartenBillApp.administration.repository.ChildRepository;
import com.example.KindergartenBillApp.administration.repository.KindergartenRepository;
import com.example.KindergartenBillApp.sharedTools.exceptions.ApiExceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BillService {

    private final BillRepository billRepository;
    private final KindergartenRepository kindergartenRepository;
    private final ChildRepository childRepository;

    /**
     * Creates a new Bill entity.
     * Validates that Kindergarten and Child IDs are provided and exist in the database.
     * If IDs are missing, throws ApiExceptions with status 400 Bad Request.
     * If referenced entities do not exist, throws ApiExceptions with status 404 Not Found.
     * Sets the Kindergarten and Child references before saving.
     * @param model Bill object to be created
     * @return the created Bill entity
     * @throws ApiExceptions if validation fails or referenced entities do not exist
     */
    public Bill create(Bill model){

        if(model.getKindergarten()==null || model.getKindergarten().getId()==null){
            throw new ApiExceptions("Kindergarten id must be provided", HttpStatus.BAD_REQUEST);
        }

        Integer kindergartenId = model.getKindergarten().getId();
        Kindergarten kindergarten = kindergartenRepository.findById(kindergartenId)
                .orElseThrow(()-> new ApiExceptions("Kindergarten with id " + kindergartenId + " not found", HttpStatus.NOT_FOUND));

        Integer childId = model.getChild().getId();
        Child child = childRepository.findById(childId)
                .orElseThrow(() -> new ApiExceptions("Child with id " + childId + " not found", HttpStatus.NOT_FOUND));

        model.setKindergarten(kindergarten);
        model.setChild(child);

        return billRepository.save(model);

    }

    /**
     * Retrieves a paginated list of Bill entities.
     * Accepts page and size parameters with default values defined in the controller.
     * Returns a Page object containing Bill entities and pagination metadata.
     * @param page the page number (0-based index, must be >= 0)
     * @param size the number of records per page (must be >= 1)
     * @return a Page of Bill entities
     */
    public Page<Bill> findAll(int page, int size ){
        PageRequest pageRequest = PageRequest.of(page, size);
        return billRepository.findAll(pageRequest);
    }

    /**
     * Retrieves a Bill entity by its unique ID.
     * If the entity does not exist, throws ApiExceptions with status 404 Not Found.
     * @param id unique identifier of the Bill entity
     * @return the Bill entity with the given ID
     * @throws ApiExceptions if the entity does not exist
     */
    public Bill findById(Integer id){
        return billRepository.findById(id)
                .orElseThrow(() -> new ApiExceptions("Bill with id " + id + " not found", HttpStatus.NOT_FOUND));
    }

    /**
     * Updates an existing Bill entity by its ID.
     * Allows partial updates: only non-null fields from the input model are applied.
     * Validates that referenced Kindergarten and Child IDs exist before updating.
     * If entity does not exist, throws ApiExceptions with status 404 Not Found.
     * @param model Bill object containing updated fields
     * @param id unique identifier of the Bill entity to update
     * @return the updated Bill entity
     * @throws ApiExceptions if entity does not exist or referenced entities do not exist
     */
    public Bill update(Bill model, Integer id) {

        Bill exists =billRepository.findById(id)
                .orElseThrow(() -> new ApiExceptions("Bill with id " + id + " not found", HttpStatus.NOT_FOUND));

        if(model.getKindergarten()!=null && model.getKindergarten().getId()!=null) {
            Integer kindergartenId = model.getKindergarten().getId();
            Kindergarten kindergarten = kindergartenRepository.findById(kindergartenId)
                    .orElseThrow(()-> new ApiExceptions("Kindergarten with id " + kindergartenId + " not found", HttpStatus.NOT_FOUND));
            exists.setKindergarten(kindergarten);
        }
        if(model.getChild()!=null && model.getChild().getId()!=null) {
            Integer childId = model.getChild().getId();
            Child child = childRepository.findById(childId)
                    .orElseThrow(() -> new ApiExceptions("Child with id " + childId + " not found", HttpStatus.NOT_FOUND));
            exists.setChild(child);
        }
        if(model.getYear()!=null) exists.setYear(model.getYear());
        if(model.getMonth()!=null) exists.setMonth(model.getMonth());
        if(model.getDeadline()!=null) exists.setDeadline(model.getDeadline());
        if(model.getBillCode()!=null) exists.setBillCode(model.getBillCode());
        if(model.getPaymentSum()!=null) exists.setPaymentSum(model.getPaymentSum());

        return billRepository.save(exists);

    }

    /**
     * Deletes a Bill entity by its unique ID.
     * First retrieves the entity to ensure it exists.
     * If the entity does not exist, throws ApiExceptions with status 404 Not Found.
     * @param id unique identifier of the Bill entity to delete
     * @throws ApiExceptions if the entity does not exist
     */
    public void delete(Integer id){
        Bill bill = billRepository.findById(id)
                .orElseThrow(() -> new ApiExceptions("Bill with id " + id + " not found", HttpStatus.NOT_FOUND));
        billRepository.delete(bill);
    }
}
