package com.example.groupexercise1.service;

import com.example.groupexercise1.exeption.AccountNotFoundException;
import com.example.groupexercise1.exeption.TransactionNotFoundException;
import com.example.groupexercise1.model.Account;
import com.example.groupexercise1.model.Transaction;
import com.example.groupexercise1.model.dto.AccountDto;
import com.example.groupexercise1.model.dto.TransactionDto;
import com.example.groupexercise1.repository.TransactionRepository;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService{

  @Autowired
  TransactionRepository transactionRepository;

  @Override
  public TransactionDto getTransactionById(long transactionId) {

    Optional<Transaction> result = transactionRepository.findById(transactionId);

    if (result.isPresent()) {

      return new TransactionDto(result.get());
    } else {
      throw new TransactionNotFoundException("Transaction not found");
    }
  }
}
