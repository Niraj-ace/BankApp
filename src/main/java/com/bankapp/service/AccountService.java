package com.bankapp.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bankapp.dto.AccountRequest;
import com.bankapp.dto.AccountResponse;
import com.bankapp.entity.Account;
import com.bankapp.entity.User;
import com.bankapp.mapper.AccountMapper;
import com.bankapp.repository.AccountRepository;
import com.bankapp.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AccountMapper accountMapper;

    // Create a new account from DTO
    public AccountResponse createAccount(AccountRequest request) {
        Account account = new Account();

        // Set account holder name and balance
        account.setAccountHolderName(request.getAccountHolderName());
        account.setBalance(request.getBalance());

        // Generate account number if missing
        account.setAccountNumber("ACCT-" + UUID.randomUUID().toString().substring(0, 8));

        // Find user by email
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            User user = userRepository.findByEmail(request.getEmail())
                          .orElseThrow(() -> {
                        	  log.error("User not found with email : {}", request.getEmail());
                              throw new RuntimeException("User not found with email: " + request.getEmail());
                          });
                          
            account.setUser(user);
        } else {
        	log.error("Account must have a valid email : {}", request.getEmail());
            throw new RuntimeException("Account must have a valid email");
        }
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> {
      	  log.error("User not found with email : {}", request.getEmail());
          throw new RuntimeException("User not found with email: " + request.getEmail());
      });
        accountRepository.save(account);
        AccountResponse accountResponse = accountMapper.mapAccountAndUserToAccountResponse(account,user);
        log.info("Email {}",account.getUser().getEmail());
        return accountResponse;
    }

    // Get account by ID
    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> {
                	log.error("Account not found with id : {}", id);
                	throw new RuntimeException("Account not found with id: " + id);
                });
    }

    // Get all accounts
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    // Update an existing account
    public Account updateAccount(Long id, AccountRequest request) {
        Account account = getAccountById(id);

        account.setAccountHolderName(request.getAccountHolderName());
        account.setBalance(request.getBalance());

        // Update user by email
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            User user = userRepository.findByEmail(request.getEmail())
                          .orElseThrow(() -> {
                        	  log.error("User not found with email : {}", request.getEmail());
                        	  throw new RuntimeException("User not found with email: " + request.getEmail());
                          });
            account.setUser(user);
        }
        
        return accountRepository.save(account);
        
    }

    // Delete an account
    public void deleteAccount(Long id) {
        Account account = getAccountById(id);
        accountRepository.delete(account);
        log.info("AccountID - {} deleted!", id);
    }
}
