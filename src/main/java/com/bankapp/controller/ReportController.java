package com.bankapp.controller;

import com.bankapp.entity.Account;
import com.bankapp.repository.AccountRepository;
import com.bankapp.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;
    private final AccountRepository accountRepo;

    public ReportController(ReportService reportService, AccountRepository accountRepo) {
        this.reportService = reportService;
        this.accountRepo = accountRepo;
    }

    @GetMapping("/export/{format}/{accountNumber}")
    public ResponseEntity<?> exportReport(@PathVariable String format,
                                          @PathVariable String accountNumber) {
        Account account = accountRepo.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        reportService.generateReport(account, format.toUpperCase());
        return ResponseEntity.ok("Report generated in " + format + " format");
    }
}
