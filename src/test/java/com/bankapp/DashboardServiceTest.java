//package com.bankapp;
//
//import com.bankapp.dto.DashboardSummaryDTO;
//import com.bankapp.entity.Transaction;
//import com.bankapp.repository.TransactionRepository;
//import com.bankapp.service.DashboardService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//public class DashboardServiceTest {
//
//    @InjectMocks
//    private DashboardService dashboardService;
//
//    @Mock
//    private TransactionRepository transactionRepo;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    public void testDashboardSummary() {
//        Transaction t1 = Transaction.builder().amount(100).type(Transaction.Type.DEPOSIT)
//                .category(Transaction.Category.FOOD).build();
//        Transaction t2 = Transaction.builder().amount(50).type(Transaction.Type.WITHDRAW)
//                .category(Transaction.Category.FOOD).build();
//
//        List<Transaction> transactions = Arrays.asList(t1, t2);
//        when(transactionRepo.findByAccountId(1L)).thenReturn(transactions);
//
//        DashboardSummaryDTO summary = dashboardService.getDashboardSummary(1L);
//
//        assertEquals(100, summary.getTotalDeposits());
//        assertEquals(50, summary.getTotalWithdrawals());
//        assertEquals(50, summary.getCurrentBalance());
//        assertEquals("FOOD", summary.getTopCategory());
//    }
//}
