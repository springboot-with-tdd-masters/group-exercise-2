package com.group3.exercise.bankapp.controllers;

import com.group3.exercise.bankapp.request.TransactionRequest;
import com.group3.exercise.bankapp.response.TransactionLogResponse;
import com.group3.exercise.bankapp.services.transaction.TransactionLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts/{accountId:\\d+}/transactions")
public class TransactionLogController {

    private TransactionLogService transactionLogService;

    public TransactionLogController(TransactionLogService transactionLogService) {
        this.transactionLogService = transactionLogService;
    }

    @GetMapping
    public Page<TransactionLogResponse> getAll(@PathVariable Long accountId, Pageable pageable) {
        return transactionLogService.findAllByAccountId(accountId, pageable);
    }

    @PostMapping
    public TransactionLogResponse create(@PathVariable Long accountId, @RequestBody TransactionRequest transactionRequest) {
        return transactionLogService.createLogFor(accountId, transactionRequest);
    }

    @DeleteMapping("{id:\\d+}")
    public ResponseEntity<String> delete(@PathVariable Long id) {

        transactionLogService.delete(id);

        return ResponseEntity
                .ok("Delete Successful");
    }

}
