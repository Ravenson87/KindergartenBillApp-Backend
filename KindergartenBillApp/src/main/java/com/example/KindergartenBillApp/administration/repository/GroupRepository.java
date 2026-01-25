package com.example.KindergartenBillApp.administration.repository;

import com.example.KindergartenBillApp.administration.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Integer> {
    boolean existsByName(String name);
    Optional<Group>findByName(String name);
}
