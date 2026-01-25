package com.example.KindergartenBillApp.administration.repository;

import com.example.KindergartenBillApp.administration.model.Child;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChildRepository extends JpaRepository<Child,Integer> {
    Optional<Child> findByNameIgnoreCaseAndSurnameIgnoreCaseAndParent_Id(
            String name,
            String surname,
            Integer parentId);
}
