package com.bankapp.scheduler;

import com.bankapp.entity.Account;
import com.bankapp.repository.AccountRepository;
import com.bankapp.service.EmailService;
import com.bankapp.service.ReportService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Scheduler to generate monthly reports and send emails after completion.
 */
@Component
public class MonthlyReportScheduler {

    private final AccountRepository accountRepo;
    private final ReportService reportService;
    private final EmailService emailService;

    public MonthlyReportScheduler(AccountRepository accountRepo,
                                  ReportService reportService,
                                  EmailService emailService) {
        this.accountRepo = accountRepo;
        this.reportService = reportService;
        this.emailService = emailService;
    }

    /**
     * Scheduled to run monthly (first day of every month at midnight)
     */
    @Scheduled(cron = "0 0 0 1 * ?") // ✅ remove 'scheduler' attribute (not supported in Spring 5)
    public void generateMonthlyReports() {

        List<Account> accounts = accountRepo.findAll(); // Fetch all accounts once
        List<CompletableFuture<Void>> reportFutures = new ArrayList<>();

        // Trigger async report generation for all accounts
        for (Account account : accounts) {
            reportFutures.add(reportService.generateReport(account, "PDF"));
        }

        // Wait for all reports to finish
        CompletableFuture<Void> allReports = CompletableFuture.allOf(reportFutures.toArray(new CompletableFuture[0]));

        allReports.thenRun(() -> {
            System.out.println("All reports generated. Sending emails...");

            List<CompletableFuture<Void>> emailFutures = new ArrayList<>();

            // Trigger async email sending for all accounts
            for (Account account : accounts) {
                emailFutures.add(emailService.sendReportEmail(
                        account.getUser().getEmail(), "MonthlyReport.pdf"));
            }

            // Wait for all emails to finish
            CompletableFuture.allOf(emailFutures.toArray(new CompletableFuture[0])).join();
            System.out.println("All emails sent successfully!");
        });
    }
}
