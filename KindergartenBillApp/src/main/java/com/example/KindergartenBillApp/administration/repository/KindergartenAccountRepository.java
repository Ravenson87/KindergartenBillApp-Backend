package com.example.KindergartenBillApp.administration.repository;

import com.example.KindergartenBillApp.administration.model.KindergartenAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KindergartenAccountRepository extends JpaRepository<KindergartenAccount,Integer> {
    boolean existsByAccountNumber(String accountNumber);
    boolean existsByIdentificationNumber(String identificationNumber);
    Optional<KindergartenAccount> findByIdentificationNumber(String identificationNumber);
    Optional<KindergartenAccount> findByAccountNumber(String accountNumber);
}
