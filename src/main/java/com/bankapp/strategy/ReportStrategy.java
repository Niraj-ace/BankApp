package com.bankapp.strategy;

import com.bankapp.entity.Account;

public interface ReportStrategy {

    void generateReport(Account account);
}
