package com.bankapp.strategy;

import com.bankapp.entity.Account;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

@Component
public class JsonReportStrategy implements ReportStrategy {

    @Override
    public void generateReport(Account account) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            File file = new File("report_" + account.getAccountNumber() + ".json");
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, account);
            System.out.println("JSON report generated: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
