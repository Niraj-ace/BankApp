package com.bankapp.controller;

import com.bankapp.dto.TransferRequest;
import com.bankapp.service.TransactionService;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@Valid @RequestBody TransferRequest request) {
        transactionService.transfer(request.getFromAccountId(),
                                    request.getToAccountId(),
                                    request.getAmount());
        return ResponseEntity.ok("Transfer successful");
    }
}
