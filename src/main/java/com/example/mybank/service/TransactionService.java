package com.example.mybank.service;

import com.example.mybank.dto.TransactionDTO;
import com.example.mybank.dto.TransactionRegisterDTO;
import com.example.mybank.entity.Account;
import com.example.mybank.enums.CurrencyCode;
import com.example.mybank.repository.AccountRepository;
import com.example.mybank.repository.TransactionDAO;
import com.example.mybank.validation.TransactionValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {
    private final TransactionDAO transactionDAO;

    private final AccountRepository accountRepository;
    private final TransactionValidationService transactionValidationService;

    public ResponseEntity<List<TransactionDTO>> getTransactions() {
        List<TransactionDTO> transactionDTOList = transactionDAO.findAll();
        return ResponseEntity.ok(transactionDTOList);
    }

    public ResponseEntity<List<TransactionDTO>> getTransactions(String sourceAccountId,
                                                                String targetAccountId) {
        List<TransactionDTO> transactionDTOList;
        if (StringUtils.isNotBlank(sourceAccountId) && StringUtils.isNotBlank(targetAccountId)) {
            transactionDTOList = transactionDAO.findTransactionsBySourceAccountIdAndTargetAccountId(sourceAccountId, targetAccountId);
            return ResponseEntity.ok(transactionDTOList);
        }

        if (StringUtils.isNotBlank(sourceAccountId)) {
            transactionDTOList = transactionDAO.findTransactionsBySourceAccountId(sourceAccountId);
            return ResponseEntity.ok(transactionDTOList);
        }

        if (StringUtils.isNotBlank(targetAccountId)) {
            transactionDTOList = transactionDAO.findTransactionsByTargetAccountId(targetAccountId);
            return ResponseEntity.ok(transactionDTOList);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    public ResponseEntity<?> createTransaction(TransactionRegisterDTO transactionRegisterDTO) {
        Set<String> errorMessages = transactionValidationService.validateTransactionRegistration(transactionRegisterDTO);
        if (!errorMessages.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
        } else {
            // Perform transaction
            TransactionDTO responseDto = performTransaction(transactionRegisterDTO);
            return ResponseEntity.ok(responseDto);
        }
    }


    /**
     * TODO: Alternatively @Version for optimistic locking can be used
     * TODO: A currency-exchange service should be used to convert amounts in different currencies between accounts
     *
     * @param transactionRegisterDTO the transaction to be created
     * @return the result transaction dto
     */
    @Transactional
    public synchronized TransactionDTO performTransaction(TransactionRegisterDTO transactionRegisterDTO) {
        log.info("Performing transaction for amount: {}", transactionRegisterDTO.getAmount());

        Account senderAccount = accountRepository.findById(transactionRegisterDTO.getSourceAccountId()).get();
        Account receiverAccount = accountRepository.findById(transactionRegisterDTO.getTargetAccountId()).get();
        log.info("Current source account balance: {}  -  Current target account balance: {}", senderAccount.getBalance(), receiverAccount.getBalance());

        TransactionDTO transactionDTO = transactionDAO.save(buildTransactionDTO(transactionRegisterDTO));
        updateAccountBalances(senderAccount, receiverAccount, transactionDTO.getAmount());

        return transactionDTO;

    }

    @Transactional
    synchronized void updateAccountBalances(Account senderAccount, Account receiverAccount, Double amount) {
        // Subtract the transaction amount from the source account id
        senderAccount.setBalance(senderAccount.getBalance() - amount);
        // Add the transaction amount to the target account id
        receiverAccount.setBalance(receiverAccount.getBalance() + amount);

        // Update account balances
        senderAccount = accountRepository.saveAndFlush(senderAccount);
        receiverAccount = accountRepository.saveAndFlush(receiverAccount);
        log.info("New source account balance: {}  -  New target account balance: {}", senderAccount.getBalance(), receiverAccount.getBalance());
    }

    private TransactionDTO buildTransactionDTO(TransactionRegisterDTO transactionRegisterDTO) {
        return TransactionDTO.builder()
                .id(UUID.randomUUID().toString())
                .sourceAccountId(transactionRegisterDTO.getSourceAccountId())
                .targetAccountId(transactionRegisterDTO.getTargetAccountId())
                .amount(Double.parseDouble(transactionRegisterDTO.getAmount()))
                .currency(CurrencyCode.valueOf(transactionRegisterDTO.getCurrency()))
                .build();
    }

}
