package com.example.KindergartenBillApp.administration.repository;

import com.example.KindergartenBillApp.administration.model.Parent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParentRepository extends JpaRepository<Parent,Integer> {
    boolean existsByEmail(String email);
    Optional<Parent> findByEmail(String email);
}
