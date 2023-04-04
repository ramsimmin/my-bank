package com.example.mybank.mapper;

import com.example.mybank.dto.AccountDTO;
import com.example.mybank.entity.Account;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AccountMapper {
    Account dtoToEntity(AccountDTO accountDTO);
    AccountDTO entityToDto(Account account);
    List<AccountDTO> entitiesToDtos(List<Account> accountEntities);

}
