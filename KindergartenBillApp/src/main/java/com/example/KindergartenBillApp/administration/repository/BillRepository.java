package com.example.KindergartenBillApp.administration.repository;

import com.example.KindergartenBillApp.administration.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill,Integer> {
}
