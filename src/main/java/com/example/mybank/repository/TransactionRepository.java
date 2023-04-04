package com.example.mybank.repository;

import com.example.mybank.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
    List<Transaction> findTransactionsBySourceAccountIdAndTargetAccountId(String sourceAccountId, String targetAccountId);
    List<Transaction> findTransactionsBySourceAccountId(String sourceAccountId);
    List<Transaction> findTransactionsByTargetAccountId(String targetAccountId);

}
