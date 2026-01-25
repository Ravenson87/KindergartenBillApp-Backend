package com.example.KindergartenBillApp.administration.repository;

import com.example.KindergartenBillApp.administration.model.Kindergarten;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KindergartenRepository extends JpaRepository<Kindergarten,Integer> {
    boolean existsByName(String name);
    boolean existsByEmail(String email);
    Optional<Kindergarten> findByName(String name);
    Optional<Kindergarten> findByEmail(String email);
}
