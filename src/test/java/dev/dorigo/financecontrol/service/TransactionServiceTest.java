package dev.dorigo.financecontrol.service;

import dev.dorigo.financecontrol.controller.request.TransactionRequest;
import dev.dorigo.financecontrol.domain.transaction.Transaction;
import dev.dorigo.financecontrol.domain.transaction.Type;
import dev.dorigo.financecontrol.domain.user.User;
import dev.dorigo.financecontrol.mappers.TransactionMapper;
import dev.dorigo.financecontrol.repository.TransactionRepository;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.BDDAssertions.then;
import static org.awaitility.Awaitility.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private Transaction transaction;

    @Mock
    private AuthService authService;

    @InjectMocks
    private TransactionService transactionService;



    @Test
    void shouldSaveTransaction() {

        TransactionRequest request = new TransactionRequest(
                "Teste description",
                new BigDecimal(100),
                LocalDate.now());

        User mockUser = new User();
        Transaction saveTransaction = TransactionMapper.toTransaction(request);
        saveTransaction.setUser(mockUser);
        saveTransaction.setType(Type.EXPENSE);

        when(authService.getAuhenticatedUser()).thenReturn(mockUser);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(saveTransaction);

        var result = transactionService.save(request, Type.EXPENSE);

        assertEquals("Teste description", result.getDescription());
        assertEquals( new BigDecimal(100), result.getAmount());
        assertEquals(Type.EXPENSE, result.getType());
        assertEquals(mockUser, result.getUser());

        verify(authService).getAuhenticatedUser();
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void shouldGetAllTransactions() {
        User mockUser = new User();
        mockUser.setId(1L);
        List<Transaction> mockTransactions = List.of(
                new Transaction(1L, "desc1", BigDecimal.TEN, Type.EXPENSE, LocalDate.now(), mockUser),
                new Transaction(2L, "desc2", BigDecimal.ONE, Type.REVENUE, LocalDate.now(), mockUser)
        );

        when(authService.getAuhenticatedUser()).thenReturn(mockUser);
        when(transactionRepository.findTransactionByUser_Id(1L)).thenReturn(mockTransactions);

        List<Transaction> result = transactionService.getAll();

        assertEquals(2, result.size());
        assertEquals("desc1", result.get(0).getDescription());
        assertEquals(BigDecimal.TEN, result.get(0).getAmount());
        assertEquals(Type.REVENUE, result.get(1).getType());

        verify(authService).getAuhenticatedUser();
        verify(transactionRepository).findTransactionByUser_Id(1L);
}

    @Test
    void shouldGetTransactionById() {
        User mockUser = new User();
        mockUser.setId(1L);
        Transaction mockTransaction = new Transaction(1L, "desc1", BigDecimal.TEN, Type.EXPENSE, LocalDate.now(), mockUser);

        when(authService.getAuhenticatedUser()).thenReturn(mockUser);
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(mockTransaction));

        var result = transactionService.getById(1L);

        assertEquals("desc1", result.getDescription());
        assertEquals(BigDecimal.TEN, result.getAmount());
        assertEquals(Type.EXPENSE, result.getType());
        assertEquals(mockUser, result.getUser());

        verify(authService).getAuhenticatedUser();
        verify(transactionRepository).findById(1L);
    }


}
