package com.example.mybank.mapper;

import com.example.mybank.dto.TransactionDTO;
import com.example.mybank.entity.Transaction;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
//@Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface TransactionMapper {
    Transaction dtoToEntity(TransactionDTO transactionDTO);
    TransactionDTO entityToDto(Transaction account);
    List<TransactionDTO> entitiesToDtos(List<Transaction> transactionEntities);

}
