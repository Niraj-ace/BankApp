package com.bankapp.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bankapp.dto.AccountRequest;
import com.bankapp.dto.AccountResponse;
import com.bankapp.entity.Account;
import com.bankapp.entity.User;
import com.bankapp.mapper.AccountMapper;
import com.bankapp.repository.AccountRepository;
import com.bankapp.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {
	
	@InjectMocks
	private AccountService accountService;
	
	 @Mock
	 private AccountRepository accountRepository;
	 
	    @Mock
	    private UserRepository userRepository;
	    
	    @Mock
	    private AccountMapper accountMapper;
	    
	    private AccountRequest request;
	    private AccountResponse response;
	    private User user;
	    private Account account;
	    
	    @BeforeEach
		void setup() {
			request = new AccountRequest();
			request.setAccountHolderName("John Doe");
			request.setBalance(1000.0);
			request.setEmail("abc@gmail.com");
			
			user = new User();
			user.setEmail("abc@gmail.com");
			
			account = new Account();
			account.setId(1L);
			
			response = new AccountResponse();
			response.setAccountNumber("dummy");
			response.setId(1L);
		}
	    
	   @Test
	   public void testCreateAccount_Success() {
		 
		  when(userRepository.findByEmail("abc@gmail.com")).thenReturn(Optional.of(user));
		  when(accountRepository.save(any(Account.class))).thenReturn(account);
		  when(accountMapper.mapAccountAndUserToAccountResponse(any(Account.class),any(User.class))).thenReturn(response);
		  
		  AccountResponse result = accountService.createAccount(request);
		  
		  assertNotNull(result);
		  assertEquals("dummy",result.getAccountNumber());
	   }
	   
	   @Test
	   public void testGetAccountById_Success() {
		   try {
			   when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
			   when(accountMapper.mapAccountAndUserToAccountResponse(any(Account.class),any(User.class))).thenReturn(response);
			   AccountResponse result = accountService.getAccountById(1L);
			   assertNotNull(result);
			   assertEquals(1L,result.getId());
		   }catch(Exception e) {
			   e.printStackTrace();
		   }
		   
	   }
}

	
