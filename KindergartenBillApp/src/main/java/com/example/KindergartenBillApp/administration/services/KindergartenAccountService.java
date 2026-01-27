package com.example.KindergartenBillApp.administration.services;

import com.example.KindergartenBillApp.administration.model.Kindergarten;
import com.example.KindergartenBillApp.administration.model.KindergartenAccount;
import com.example.KindergartenBillApp.administration.repository.KindergartenAccountRepository;
import com.example.KindergartenBillApp.administration.repository.KindergartenRepository;
import com.example.KindergartenBillApp.sharedTools.exceptions.ApiExceptions;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class KindergartenAccountService {

    private final KindergartenAccountRepository kindergartenAccountRepository;
    private final KindergartenRepository kindergartenRepository;

    /**
     * Creates a new KindergartenAccount in the database.
     * Before saving, checks if an account with the same accountNumber or identificationNumber already exists.
     * If it does, throws an ApiExceptions with status 409 Conflict.
     * Otherwise, saves the account and returns the persisted entity.
     * @param model the KindergartenAccount object to be created
     * @return the saved KindergartenAccount with a generated ID
     */
    public KindergartenAccount create(KindergartenAccount model){
        if(model.getKindergarten() == null || model.getKindergarten().getId() == null){
            throw new ApiExceptions("Kindegarten id must be provided", HttpStatus.BAD_REQUEST);
        }

        Integer kindergartenId = model.getKindergarten().getId();
        Kindergarten kindergarten = kindergartenRepository.findById(kindergartenId).orElseThrow(() -> new ApiExceptions("Kindergarten with id " + kindergartenId + " not found", HttpStatus.NOT_FOUND));

        if(kindergartenAccountRepository.existsByAccountNumber(model.getAccountNumber()) ){
            throw new ApiExceptions("Account number already exists", HttpStatus.CONFLICT);
        }
        if(kindergartenAccountRepository.existsByIdentificationNumber(model.getIdentificationNumber())){
            throw new ApiExceptions("Identification number already exists", HttpStatus.CONFLICT);
        }
            model.setKindergarten(kindergarten);
            return kindergartenAccountRepository.save(model);

    }

    /**
     * Retrieves a paginated list of KindergartenAccount entities from the database.
     * @param page the page number (0-based index)
     * @param size the number of records per page
     * @return a Page object containing KindergartenAccount entities and pagination metadata
     */
    public Page<KindergartenAccount> findAll(int page, int size){
        Pageable  pageable = PageRequest.of(page, size);
        return kindergartenAccountRepository.findAll(pageable);
    }

    /**
     * Finds a KindergartenAccount by its ID.
     * If the account does not exist,
     * throws an ApiExceptions with status 404 Not Found.
     * @param id the unique identifier of the KindergartenAccount
     * @return the KindergartenAccount entity if found
     */
    public KindergartenAccount findById(Integer id){
            return kindergartenAccountRepository.findById(id)
                    .orElseThrow(() -> new ApiExceptions("Kindergarten account does not exist", HttpStatus.NOT_FOUND));

    }

    /**
     * Finds a KindergartenAccount by its account number.
     * If no account exists with the given account number,
     * throws an ApiExceptions with status 404 Not Found.
     * @param accountNumber the unique account number of the KindergartenAccount
     * @return the KindergartenAccount entity if found
     */
    public KindergartenAccount findByAccountNumber(String accountNumber){
    return kindergartenAccountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new ApiExceptions("Account number does not exist", HttpStatus.NOT_FOUND));
    }

    /**
     * Finds a KindergartenAccount by its identification number.
     * If no account exists with the given identification number,
     * throws an ApiExceptions with status 404 Not Found.
     * @param identificationNumber the unique identification number of the KindergartenAccount
     * @return the KindergartenAccount entity if found
     */
    public KindergartenAccount findByIdentificationNumber(String identificationNumber){
        return kindergartenAccountRepository.findByIdentificationNumber(identificationNumber)
                .orElseThrow(() -> new ApiExceptions("Identification number does not exist", HttpStatus.NOT_FOUND));

    }

    /**
     * Partially updates an existing KindergartenAccount by its ID.
     * Only non-null fields from the provided model are copied into the existing entity.
     * Unique fields (accountNumber, identificationNumber) are validated to prevent conflicts.
     * Audit fields (lastModifiedBy, lastModifiedDate) should be automatically updated in the future via Auditable.
     * If the account does not exist, throws an ApiExceptions with status 404 Not Found.
     * @param model the KindergartenAccount object containing updated values (only non-null fields are applied)
     * @param id the unique identifier of the KindergartenAccount to update
     * @return the updated KindergartenAccount entity
     */
    public KindergartenAccount update(KindergartenAccount model, Integer id){
      KindergartenAccount existing =kindergartenAccountRepository.findById(id)
              .orElseThrow(() -> new ApiExceptions("Kindergarten account with id = " + id + " not found", HttpStatus.NOT_FOUND));

      if(model.getKindergarten() != null && model.getKindergarten().getId() != null){
          Kindergarten kindergarten = kindergartenRepository.findById(model.getKindergarten().getId())
                  .orElseThrow(() ->new ApiExceptions("Kindergarten with id " + model.getKindergarten().getId() + " not found", HttpStatus.NOT_FOUND));
          existing.setKindergarten(kindergarten);
      }

      if(model.getAccountNumber() != null
              && !model.getAccountNumber().equals(existing.getAccountNumber())
              && kindergartenAccountRepository.existsByAccountNumber(model.getAccountNumber())){
          throw new ApiExceptions("Account number already exists", HttpStatus.CONFLICT);
      }

      if(model.getIdentificationNumber() != null
              && !model.getIdentificationNumber().equals(existing.getIdentificationNumber())
              && kindergartenAccountRepository.existsByIdentificationNumber(model.getIdentificationNumber())){
          throw new ApiExceptions("Identification number already exists", HttpStatus.CONFLICT);
      }

      //Validation for Pib
        if (model.getPib() != null) {
            if (!model.getPib().matches("\\d{9}")) {
                throw new ApiExceptions("PIB must have exactly 9 digits", HttpStatus.BAD_REQUEST);
            }
            existing.setPib(model.getPib());
        }

      if(model.getBankName() != null) existing.setBankName(model.getBankName());
      if(model.getAccountNumber() != null) existing.setAccountNumber(model.getAccountNumber());
      if(model.getIdentificationNumber() != null) existing.setIdentificationNumber(model.getIdentificationNumber());
      if(model.getActivityCode() != null) existing.setActivityCode(model.getActivityCode());

      return kindergartenAccountRepository.save(existing);
    }

    /**
     * Deletes a KindergartenAccount entity by its unique ID.
     * Retrieves the entity first to allow audit logging before deletion.
     * If the entity does not exist, throws ApiExceptions with status 404 Not Found.
     * @param id the unique identifier of the KindergartenAccount entity to delete
     * @throws ApiExceptions if the KindergartenAccount entity does not exist
     */
    public void deleteById(Integer id){
        KindergartenAccount model = kindergartenAccountRepository.findById(id)
                .orElseThrow(() -> new ApiExceptions("Kindergarten account with id " + id + " does not exist", HttpStatus.NOT_FOUND));
        kindergartenAccountRepository.delete(model);
    }

}
