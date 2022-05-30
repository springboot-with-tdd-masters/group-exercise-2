package com.example.groupexercise2.service;

import com.example.groupexercise2.model.dto.AccountDto;
import com.example.groupexercise2.model.dto.TransactionDto;
import com.example.groupexercise2.model.dto.TransactionRequestDto;

public interface TransactionService {

  TransactionDto getTransactionById(long id);
  
  AccountDto createTransaction(long accountId, TransactionRequestDto request);
  
  void delete(long accountId, long transactionId);
}
