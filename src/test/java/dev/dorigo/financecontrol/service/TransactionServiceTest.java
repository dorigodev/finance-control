package dev.dorigo.financecontrol.service;

import dev.dorigo.financecontrol.controller.request.TransactionRequest;
import dev.dorigo.financecontrol.controller.request.TransactionUpdateRequest;
import dev.dorigo.financecontrol.domain.transaction.Transaction;
import dev.dorigo.financecontrol.domain.transaction.Type;
import dev.dorigo.financecontrol.domain.user.User;
import dev.dorigo.financecontrol.mappers.TransactionMapper;
import dev.dorigo.financecontrol.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

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

    @Test
    void shouldDeleteTransaction() {
        User mockUser = new User();
        mockUser.setId(1L);
        Transaction mockTransaction = new Transaction(1L, "desc1", BigDecimal.TEN, Type.EXPENSE, LocalDate.now(), mockUser);

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(mockTransaction));
        when(authService.getAuhenticatedUser()).thenReturn(mockUser);

        transactionService.deleteById(1L);

        verify(transactionRepository).deleteById(1L);
    }

    @Test
    void shouldThrowWhenUserIsNotOwnerOnDelete() {
        User mockUser = new User();
        mockUser.setId(1L);

        User otherUser = new User();
        otherUser.setId(2L);

        Transaction mockTransaction = new Transaction(1L, "desc1", BigDecimal.TEN, Type.EXPENSE, LocalDate.now(), mockUser);

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(mockTransaction));
        when(authService.getAuhenticatedUser()).thenReturn(otherUser);

        assertThrows(ResponseStatusException.class, () -> transactionService.deleteById(1L));
        verify(transactionRepository, never()).deleteById(anyLong());

    }

    @Test
    void shouldThrowWhenTransactionNotFoundOnDelete() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> transactionService.deleteById(1L));

        verify(transactionRepository, never()).deleteById(anyLong());
    }

    @Test
    void shouldUpdateTransaction() {
        User mockUser = new User();
        mockUser.setId(1L);
        Transaction mockTransaction = new Transaction(1L, "desc1", BigDecimal.ONE, Type.EXPENSE, LocalDate.now(), mockUser);

        TransactionUpdateRequest updateRequest = new TransactionUpdateRequest("new", BigDecimal.TEN, Type.REVENUE, LocalDate.now().plusDays(1));

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(mockTransaction));
        when(authService.getAuhenticatedUser()).thenReturn(mockUser);
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Transaction updated = transactionService.update(1L, updateRequest);

        assertEquals("new", updated.getDescription());
        assertEquals(Type.REVENUE, updated.getType());
        assertEquals(BigDecimal.TEN, updated.getAmount());
        assertEquals(LocalDate.now().plusDays(1), updated.getDate());
    }

    @Test
    void shouldUpdateDescriptionAndAmountTransaction() {
        User mockUser = new User();
        mockUser.setId(1L);
        Transaction mockTransaction = new Transaction(1L, "desc1", BigDecimal.ONE, Type.EXPENSE, LocalDate.now(), mockUser);

        TransactionUpdateRequest updateRequest = new TransactionUpdateRequest("new", BigDecimal.TEN, null, null);

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(mockTransaction));
        when(authService.getAuhenticatedUser()).thenReturn(mockUser);
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Transaction updated = transactionService.partialUpdate(1L, updateRequest);
        assertEquals("new", updated.getDescription());
        assertEquals(BigDecimal.TEN, updated.getAmount());
        assertEquals(Type.EXPENSE, updated.getType());
        assertEquals(LocalDate.now(), updated.getDate());

    }

    @Test
    void shouldBuscarTransacoesComFiltroCorretamente() {
        // Arrange
        User mockUser = new User();
        mockUser.setId(1L);

        LocalDate dataInicio = LocalDate.of(2025, 1, 1);
        LocalDate dataFim = LocalDate.of(2025, 12, 31);
        String tipoStr = "revenue";
        Type tipo = Type.REVENUE;
        BigDecimal valorMin = new BigDecimal("100.00");
        BigDecimal valorMax = new BigDecimal("500.00");

        List<Transaction> mockTransactions = List.of(
                new Transaction(1L, "Transação A", new BigDecimal("200.00"), tipo, LocalDate.of(2025, 6, 15), mockUser)
        );

        when(authService.getAuhenticatedUser()).thenReturn(mockUser);
        when(transactionRepository.buscarFiltradas(dataInicio, dataFim, tipo, valorMin, valorMax, 1L))
                .thenReturn(mockTransactions);

        // Act
        List<Transaction> result = transactionService.buscarTransacoesComFiltro(dataInicio, dataFim, tipoStr, valorMin, valorMax);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Transação A", result.get(0).getDescription());
        verify(authService).getAuhenticatedUser();
        verify(transactionRepository).buscarFiltradas(dataInicio, dataFim, tipo, valorMin, valorMax, 1L);

        }

    @Test
    void shoulGetSaldo() {
        User mockUser = new User();
        mockUser.setId(1L);

        when(authService.getAuhenticatedUser()).thenReturn(mockUser);
        when(transactionRepository.somarPorTipo(Type.EXPENSE, 1L)).thenReturn(new BigDecimal("300.00"));
        when(transactionRepository.somarPorTipo(Type.REVENUE, 1L)).thenReturn(new BigDecimal("1000.00"));

        BigDecimal saldo = transactionService.calcularSaldo();

        assertEquals(new BigDecimal("700.00"), saldo);


    }
}
