package com.example.mybank.repository;

import com.example.mybank.dto.TransactionDTO;
import com.example.mybank.entity.Transaction;
import com.example.mybank.mapper.TransactionMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionDAOImpl implements TransactionDAO {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;


    @Override
    public List<TransactionDTO> findAll() {
        List<Transaction> transactionList = transactionRepository.findAll();
        return transactionMapper.entitiesToDtos(transactionList);
    }

    @Override
    public TransactionDTO save(TransactionDTO transactionDTO) {
        Transaction transaction =  transactionRepository.save(transactionMapper.dtoToEntity(transactionDTO));
        return transactionMapper.entityToDto(transaction);
    }

    @Override
    public List<TransactionDTO> findTransactionsBySourceAccountIdAndTargetAccountId(String sourceAccountId, String targetAccountId) {
        List<Transaction> transactionList = transactionRepository.findTransactionsBySourceAccountIdAndTargetAccountId(sourceAccountId, targetAccountId);
        return transactionMapper.entitiesToDtos(transactionList);
    }

    @Override
    public List<TransactionDTO> findTransactionsBySourceAccountId(String sourceAccountId) {
        List<Transaction> transactionList = transactionRepository.findTransactionsBySourceAccountId(sourceAccountId);
        return transactionMapper.entitiesToDtos(transactionList);
    }

    @Override
    public List<TransactionDTO> findTransactionsByTargetAccountId(String targetAccountId) {
        List<Transaction> transactionList = transactionRepository.findTransactionsByTargetAccountId(targetAccountId);
        return transactionMapper.entitiesToDtos(transactionList);
    }
}
