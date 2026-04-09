package com.bankapp.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.bankapp.dto.DashboardSummaryDTO;
import com.bankapp.entity.Category;
import com.bankapp.entity.Transaction;
import com.bankapp.repository.AccountRepository;
import com.bankapp.repository.TransactionRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DashboardService {

    private final TransactionRepository transactionRepo;
    private final AccountRepository accountRepository;

    public DashboardService(TransactionRepository transactionRepo, AccountRepository accountRepository) {
        this.transactionRepo = transactionRepo;
        this.accountRepository = accountRepository;
    }

    public DashboardSummaryDTO getDashboardSummary(Long accountId) {
        List<Transaction> transactions = transactionRepo.findByAccountId(accountId);

        double totalDeposits = transactions.stream()
                .filter(t -> t.getType() == Transaction.Type.DEPOSIT)
                .mapToDouble(Transaction::getAmount)
                .sum();
        
        double totalWithdrawals = transactions.stream()
                .filter(t -> t.getType() == Transaction.Type.WITHDRAW)
                .mapToDouble(Transaction::getAmount)
                .sum();

        double currentBalance = accountRepository.findById(accountId).orElse(null).getBalance();
        log.info(String.valueOf(totalWithdrawals));
        
        Category mostFrequentCategory = transactions.stream()
        	    .collect(Collectors.groupingBy(Transaction::getCategory, Collectors.counting())) // Map<Category, Long>
        	    .entrySet().stream() // Stream of Map.Entry<Category, Long>
        	    .max(Map.Entry.comparingByValue()) // Find the one with the largest count
        	    .map(Map.Entry::getKey) // Get the Category itself
        	    .orElse(null); // Handle empty case
        

//        Map<String, Double> categoryTotals = transactions.stream()
//                .collect(Collectors.groupingBy(
//                        t -> t.getCategory().name(),
//                        Collectors.summingDouble(Transaction::getAmount)
//                ));
//        log.info(categoryTotals.toString());
//
//        String topCategory = categoryTotals.entrySet().stream()
//                .max(Map.Entry.comparingByValue())
//                .map(Map.Entry::getKey)
//                .orElse("N/A");

        return DashboardSummaryDTO.builder()
                .totalDeposits(totalDeposits)
                .totalWithdrawals(totalWithdrawals)
                .currentBalance(currentBalance)
                //.topCategory(category)
                .topCategory(mostFrequentCategory)
                .build();
    }
}
