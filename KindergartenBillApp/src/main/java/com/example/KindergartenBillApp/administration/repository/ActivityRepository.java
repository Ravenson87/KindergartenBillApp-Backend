package com.example.KindergartenBillApp.administration.repository;



import com.example.KindergartenBillApp.administration.model.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActivityRepository extends JpaRepository<Activity, Integer> {
    boolean existsByName(String name);
    Optional<Activity> findByName(String name);
}
