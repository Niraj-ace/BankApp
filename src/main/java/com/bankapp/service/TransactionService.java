package com.bankapp.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bankapp.entity.Account;
import com.bankapp.entity.Category;
import com.bankapp.entity.Transaction;
import com.bankapp.exception.InsufficientBalanceException;
import com.bankapp.repository.AccountRepository;
import com.bankapp.repository.TransactionRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TransactionService {

    private final AccountRepository accountRepo;
    private final TransactionRepository transactionRepo;

    public TransactionService(AccountRepository accountRepo,
                              TransactionRepository transactionRepo) {
        this.accountRepo = accountRepo;
        this.transactionRepo = transactionRepo;
    }

    @Transactional
    public String transfer(Long fromAccountId, Long toAccountId, double amount, Category category) {
    	
            Account from = accountRepo.findById(fromAccountId)
                    .orElseThrow(() -> new RuntimeException("Source account not found"));
            Account to = accountRepo.findById(toAccountId)
                    .orElseThrow(() -> new RuntimeException("Destination account not found"));

            if (from.getBalance() < amount) {
                throw new InsufficientBalanceException("Insufficient balance");
            }

            from.setBalance(from.getBalance() - amount);
            to.setBalance(to.getBalance() + amount);

            accountRepo.save(from);
            accountRepo.save(to);

            // Log transactions
            Transaction t1 = Transaction.builder()
                    .account(from)
                    .amount(amount)
                    .type(Transaction.Type.WITHDRAW)
                    .category(category)
                    .description("Withdrawn from account " + to.getAccountNumber())
                    .build();
            log.info(t1.getCategory().toString());
            Transaction t2 = Transaction.builder()
                    .account(to)
                    .amount(amount)
                    .type(Transaction.Type.DEPOSIT)
                    .category(category)
                    .description("Deposited to account " + from.getAccountNumber())
                    .build();

            transactionRepo.save(t1);
            transactionRepo.save(t2);
            return "Amount : "+amount + t1.getDescription()+" and "+t2.getDescription();
    }
}
