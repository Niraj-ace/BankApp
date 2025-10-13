package com.bankapp.dto;

public class AccountRequest {

    private String accountHolderName;
    private Double balance;
    private String email;

    // Getters and Setters
    public String getAccountHolderName() { return accountHolderName; }
    public void setAccountHolderName(String accountHolderName) { this.accountHolderName = accountHolderName; }

    public Double getBalance() { return balance; }
    public void setBalance(Double balance) { this.balance = balance; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
