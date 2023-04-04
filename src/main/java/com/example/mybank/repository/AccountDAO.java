package com.example.mybank.repository;

import com.example.mybank.dto.AccountDTO;

import java.util.List;
import java.util.Optional;

public interface AccountDAO {

    Optional<AccountDTO> findById(String accountId);

    List<AccountDTO> findAll();

    AccountDTO save(AccountDTO accountDTO);


    AccountDTO saveAndFlush(AccountDTO accountDTO);
}
