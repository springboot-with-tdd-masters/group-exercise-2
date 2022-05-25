package com.example.groupexercise1.service;

import com.example.groupexercise1.model.dto.TransactionDto;

public interface TransactionService {

  TransactionDto getTransactionById(long id);
}
