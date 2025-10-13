package com.bankapp.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bankapp.entity.Account;
import com.bankapp.entity.Transaction;
import com.bankapp.repository.AccountRepository;
import com.bankapp.repository.TransactionRepository;

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
    public void transfer(Long fromAccountId, Long toAccountId, double amount) {
    	
            Account from = accountRepo.findById(fromAccountId)
                    .orElseThrow(() -> new RuntimeException("Source account not found"));
            Account to = accountRepo.findById(toAccountId)
                    .orElseThrow(() -> new RuntimeException("Destination account not found"));

            if (from.getBalance() < amount) {
                throw new RuntimeException("Insufficient balance");
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
                    .category(Transaction.Category.OTHERS)
                    .description("Transfer to account " + to.getAccountNumber())
                    .build();
            Transaction t2 = Transaction.builder()
                    .account(to)
                    .amount(amount)
                    .type(Transaction.Type.DEPOSIT)
                    .category(Transaction.Category.OTHERS)
                    .description("Transfer from account " + from.getAccountNumber())
                    .build();

            transactionRepo.save(t1);
            transactionRepo.save(t2);
    }
}
