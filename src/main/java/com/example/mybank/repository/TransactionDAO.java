package com.example.mybank.repository;

import com.example.mybank.dto.TransactionDTO;
import com.example.mybank.entity.Transaction;

import java.util.List;

public interface TransactionDAO {

    List<TransactionDTO> findAll();
    TransactionDTO save(TransactionDTO transactionDTO);
    List<TransactionDTO> findTransactionsBySourceAccountIdAndTargetAccountId(String sourceAccountId, String targetAccountId);
    List<TransactionDTO> findTransactionsBySourceAccountId(String sourceAccountId);
    List<TransactionDTO> findTransactionsByTargetAccountId(String targetAccountId);
}
