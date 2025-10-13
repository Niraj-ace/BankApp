package com.bankapp.strategy;

import com.bankapp.entity.Account;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;

@Component
public class CsvReportStrategy implements ReportStrategy {

    @Override
    public void generateReport(Account account) {
        String fileName = "report_" + account.getAccountNumber() + ".csv";
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.append("Account Number,Balance\n");
            writer.append(account.getAccountNumber()).append(",")
                  .append(String.valueOf(account.getBalance())).append("\n");
            System.out.println("CSV report generated: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
