package com.bankapp.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * Service to send emails asynchronously.
 * Returns CompletableFuture to allow chaining with other async tasks.
 */
@Service
public class EmailService {

    @Async("taskExecutor") // Use the same executor as reports
    public CompletableFuture<Void> sendReportEmail(String to, String reportFileName) {
        try {
            System.out.println("Sending email to " + to + " with report " + reportFileName);
            Thread.sleep(3000); // Simulate email sending delay
            System.out.println("Email sent successfully to " + to);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Email sending interrupted for " + to);
        }
        return CompletableFuture.completedFuture(null);
    }
}
