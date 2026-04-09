package com.bankapp.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bankapp.dto.AccountRequest;
import com.bankapp.dto.AccountResponse;
import com.bankapp.entity.Account;
import com.bankapp.entity.User;
import com.bankapp.exception.AccountNotFoundException;
import com.bankapp.exception.UserNotFoundException;
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
    @Transactional
    public AccountResponse createAccount(AccountRequest request) {
        Account account = new Account();

        // Set account holder name and balance
        account.setAccountHolderName(request.getAccountHolderName());
        account.setBalance(request.getBalance());

        // Generate account number if missing
        account.setAccountNumber("ACCT-" + UUID.randomUUID().toString().substring(0, 8));
        
        User user = new User();
        // Find user by email
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            user = userRepository.findByEmail(request.getEmail())
                          .orElseThrow(() -> {
                        	  //log.error("User not found with email : {}", request.getEmail());
                              return new UserNotFoundException("User not found with email: " + request.getEmail());
                          });
                          
            // 2️⃣ Check if this user already has an account
//            boolean accountExists = userRepository.findByEmail(request.getEmail());
//            if (accountExists) {
//                log.error("Account already exists for user email: {}", request.getEmail());
//                throw new RuntimeException("Account already exists for user email: " + request.getEmail());
//            }
            account.setUser(user);
        } else {
        	//log.error("Account must have a valid email : {}", request.getEmail());
            throw new RuntimeException("Account must have a valid email");
        }
//        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> {
//      	  log.error("User not found with email : {}", request.getEmail());
//          throw new RuntimeException("User not found with email: " + request.getEmail());
//      });
        accountRepository.save(account);
        AccountResponse accountResponse = accountMapper.mapAccountAndUserToAccountResponse(account,user);
        log.info("Email {}",account.getUser().getEmail());
        return accountResponse;
    }

    // Get account by ID
    public AccountResponse getAccountById(Long id) {
    	Account account = accountRepository.findById(id)
                			.orElseThrow(() -> {
                				//log.error("Account not found with id : {}", id);
                				return new AccountNotFoundException("Account not found with id: " + id);
                			});
    	User user = new User();
    	user = account.getUser();
    	AccountResponse accountResponse = accountMapper.mapAccountAndUserToAccountResponse(account,user);
        return accountResponse;
    }

    // Get all accounts
    public List<AccountResponse> getAllAccounts() {
        List<Account> accounts =  accountRepository.findAll();
        List<AccountResponse> accountResponse = accounts.stream()
        		.map(account->accountMapper.mapAccountAndUserToAccountResponse(account,account.getUser()))
        		.collect(Collectors.toList());
        return accountResponse;
    }

    // Update an existing account
    @Transactional
    public AccountResponse updateAccount(Long id, AccountRequest request) {
    	Account account = accountRepository.findById(id)
    			.orElseThrow(() -> {
    				//log.error("Account not found with id : {}", id);
    				return new AccountNotFoundException("Account not found with id: " + id);
    			});

        account.setAccountHolderName(request.getAccountHolderName());
        account.setBalance(request.getBalance());

        User user = new User();
        // Update user by email
        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            user = userRepository.findByEmail(request.getEmail())
                          .orElseThrow(() -> {
                        	  //log.error("User not found with email : {}", request.getEmail());
                        	  return new UserNotFoundException("User not found with email: " + request.getEmail());
                          });
            account.setUser(user);
        }
        accountRepository.save(account);
        AccountResponse accountResponse = accountMapper.mapAccountAndUserToAccountResponse(account,user);
        
        return accountResponse;
        
    }

    // Delete an account
    public void deleteAccount(Long id) {
    	Account account = accountRepository.findById(id)
    			.orElseThrow(() -> {
    				//log.error("Account not found with id : {}", id);
    				return new AccountNotFoundException("Account not found with id: " + id);
    			});
        accountRepository.delete(account);
        log.info("AccountID - {} deleted!", id);
    }
}
