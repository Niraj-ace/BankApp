package com.bankapp;

import com.bankapp.entity.Account;
import com.bankapp.entity.Transaction;
import com.bankapp.repository.AccountRepository;
import com.bankapp.repository.TransactionRepository;
import com.bankapp.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private AccountRepository accountRepo;

    @Mock
    private TransactionRepository transactionRepo;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testTransfer_Success() {
        Account from = Account.builder().id(1L).balance(1000).build();
        Account to = Account.builder().id(2L).balance(500).build();

        when(accountRepo.findById(1L)).thenReturn(Optional.of(from));
        when(accountRepo.findById(2L)).thenReturn(Optional.of(to));
        when(accountRepo.save(any(Account.class))).thenAnswer(i -> i.getArguments()[0]);
        when(transactionRepo.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);

        transactionService.transfer(1L, 2L, 200);

        assertEquals(800, from.getBalance());
        assertEquals(700, to.getBalance());
    }

    @Test
    public void testTransfer_InsufficientBalance() {
        Account from = Account.builder().id(1L).balance(100).build();
        Account to = Account.builder().id(2L).balance(500).build();

        when(accountRepo.findById(1L)).thenReturn(Optional.of(from));
        when(accountRepo.findById(2L)).thenReturn(Optional.of(to));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                transactionService.transfer(1L, 2L, 200)
        );

        assertEquals("Insufficient balance", exception.getMessage());
    }
}
