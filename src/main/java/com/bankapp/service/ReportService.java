package com.bankapp.service;

import com.bankapp.entity.Account;
import com.bankapp.strategy.ReportStrategy;
import com.bankapp.strategy.ReportStrategyFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * Service for generating reports using the Strategy pattern.
 * Runs asynchronously using @Async and returns CompletableFuture.
 */
@Service
public class ReportService {

    private final ReportStrategyFactory strategyFactory;

    public ReportService(ReportStrategyFactory strategyFactory) {
        this.strategyFactory = strategyFactory;
    }

    @Async("taskExecutor") // Runs in a separate thread from the custom executor
    public CompletableFuture<Void> generateReport(Account account, String format) {
        ReportStrategy strategy = strategyFactory.getStrategy(format);
        strategy.generateReport(account); // Existing strategy logic
        return CompletableFuture.completedFuture(null); // Signal completion
    }
}
