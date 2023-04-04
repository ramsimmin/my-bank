package com.example.mybank.repository;

import com.example.mybank.dto.AccountDTO;
import com.example.mybank.entity.Account;
import com.example.mybank.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountDAOImpl implements AccountDAO {
    private final AccountRepository accountRepository;

    private final AccountMapper accountMapper;

    @Override
    public Optional<AccountDTO> findById(String accountId) {
        Optional<Account> account = accountRepository.findById(accountId);
        if (account.isPresent()) {
            return Optional.of(accountMapper.entityToDto(account.get()));
        }
        return Optional.empty();
    }

    @Override
    public List<AccountDTO> findAll() {
        List<Account> accounts = accountRepository.findAll();
        return accountMapper.entitiesToDtos(accounts);
    }

    @Override
    public AccountDTO save(AccountDTO accountDTO) {
        Account account = accountRepository.save(accountMapper.dtoToEntity(accountDTO));
        return accountMapper.entityToDto(account);
    }

    @Override
    public AccountDTO saveAndFlush(AccountDTO accountDTO) {
        Account account = accountRepository.save(accountMapper.dtoToEntity(accountDTO));
        return accountMapper.entityToDto(account);
    }


}
