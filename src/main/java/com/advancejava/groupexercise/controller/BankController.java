package com.advancejava.groupexercise.controller;

import com.advancejava.groupexercise.entity.Account;
import com.advancejava.groupexercise.entity.BankTransaction;
import com.advancejava.groupexercise.model.dto.AccountRequest;
import com.advancejava.groupexercise.model.dto.DTOResponse;
import com.advancejava.groupexercise.service.BankService;
import com.advancejava.groupexercise.model.dto.DTORequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1")
public class BankController {

    final static Logger logger = Logger.getLogger("BankController");

    @Autowired
    private BankService bankService;

    @PutMapping("/accounts/{id}/transactions")
    public Account txnAccount(@RequestBody DTORequest request, @PathVariable Integer id){
        logger.info(request.getType()+" transaction");

        bankService.updateTransaction(request,id);
        return bankService.getAccount(id);
    }

    @GetMapping("/transactions/{field}/{order}")
    private DTOResponse<Page<BankTransaction>> getBankTxnsWithPaginationAndSort(@RequestParam int page, @RequestParam int limit,
                                                                                @PathVariable String field, @PathVariable String order) {
        logger.info("pagination/sorting transaction");
        Page<BankTransaction> txn = bankService.getBankTxnsWithPaginationAndSort(page, limit, field,order);
        return new DTOResponse<>(txn.getSize(), txn);
    }
}
