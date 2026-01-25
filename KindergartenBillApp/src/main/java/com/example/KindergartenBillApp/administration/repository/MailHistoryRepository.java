package com.example.KindergartenBillApp.administration.repository;

import com.example.KindergartenBillApp.administration.model.MailHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MailHistoryRepository extends JpaRepository<MailHistory,Integer> {
    List<MailHistory> findByAddresses(String addresses);
    List<MailHistory> findByMessage(String message);
}
