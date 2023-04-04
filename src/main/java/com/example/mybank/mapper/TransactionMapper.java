package com.example.mybank.mapper;

import com.example.mybank.dto.TransactionDTO;
import com.example.mybank.entity.Transaction;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TransactionMapper {
    Transaction dtoToEntity(TransactionDTO transactionDTO);

    TransactionDTO entityToDto(Transaction account);

    List<TransactionDTO> entitiesToDtos(List<Transaction> transactionEntities);

}
