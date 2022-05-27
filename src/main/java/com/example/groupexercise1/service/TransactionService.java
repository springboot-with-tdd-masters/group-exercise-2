package com.example.groupexercise1.service;

import com.example.groupexercise1.model.dto.AccountDto;
import com.example.groupexercise1.model.dto.TransactionDto;
import com.example.groupexercise1.model.dto.TransactionRequestDto;

public interface TransactionService {

  TransactionDto getTransactionById(long id);
  
  AccountDto createTransaction(long accountId, TransactionRequestDto request);
  
  void delete(long accountId, long transactionId);
}
