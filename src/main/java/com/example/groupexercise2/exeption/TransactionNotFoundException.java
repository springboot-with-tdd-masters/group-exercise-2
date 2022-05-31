package com.example.groupexercise2.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class TransactionNotFoundException extends
    RuntimeException {

  private static final long serialVersionUID = -2433483504677527981L;

  public TransactionNotFoundException() {
    super("Transaction Not Found");
  }

  public TransactionNotFoundException(String message) {
    super(message);
  }
}
