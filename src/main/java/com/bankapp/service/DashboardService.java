package com.bankapp.service;

import com.bankapp.dto.DashboardSummaryDTO;
import com.bankapp.entity.Transaction;
import com.bankapp.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final TransactionRepository transactionRepo;

    public DashboardService(TransactionRepository transactionRepo) {
        this.transactionRepo = transactionRepo;
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

        double currentBalance = totalDeposits - totalWithdrawals;

        Map<String, Double> categoryTotals = transactions.stream()
                .filter(t -> t.getType() == Transaction.Type.WITHDRAW)
                .collect(Collectors.groupingBy(
                        t -> t.getCategory().name(),
                        Collectors.summingDouble(Transaction::getAmount)
                ));

        String topCategory = categoryTotals.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");

        return DashboardSummaryDTO.builder()
                .totalDeposits(totalDeposits)
                .totalWithdrawals(totalWithdrawals)
                .currentBalance(currentBalance)
                .topCategory(topCategory)
                .build();
    }
}
