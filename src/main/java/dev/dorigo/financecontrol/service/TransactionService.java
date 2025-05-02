package dev.dorigo.financecontrol.service;

import dev.dorigo.financecontrol.controller.request.TransactionRequest;
import dev.dorigo.financecontrol.domain.transaction.Transaction;
import dev.dorigo.financecontrol.domain.transaction.Type;
import dev.dorigo.financecontrol.repository.TransactionRepository;
import dev.dorigo.financecontrol.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository repository;
    private final AuthService authService;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;


    public Transaction save(Transaction transaction) {
        transaction.setUser(authService.getAuhenticatedUser());
        return repository.save(transaction);
    }

    public List<Transaction> getAll() {
        return repository.findTransactionByUser_Id(authService.getAuhenticatedUser().getId());
    }

    public Transaction getById(Long id) {
        var transaction = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if(!transaction.getUser().equals(authService.getAuhenticatedUser())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return transaction;
    }

    public void deleteById(Long id) {
        var transaction = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));
        if(!transaction.getUser().equals(authService.getAuhenticatedUser())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        repository.deleteById(id);
    }

    public Transaction update(Long id, Transaction UpdateTransaction) {
        var transaction = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if(!transaction.getUser().equals(authService.getAuhenticatedUser())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        transaction.setDescription(UpdateTransaction.getDescription());
        transaction.setAmount(UpdateTransaction.getAmount());
        transaction.setDate(UpdateTransaction.getDate());
        transaction.setType(UpdateTransaction.getType());
        return repository.save(transaction);
    }

    public Transaction partialUpdate(Long id, TransactionRequest UpdateTransaction) {
        var transaction = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if(!transaction.getUser().equals(authService.getAuhenticatedUser())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        if (UpdateTransaction.description() != null) {
            transaction.setDescription(UpdateTransaction.description());
        }
        if (UpdateTransaction.amount() != null) {
            transaction.setAmount(UpdateTransaction.amount());
        }
        if (UpdateTransaction.date() != null) {
            transaction.setDate(UpdateTransaction.date());
        }
        if (UpdateTransaction.type() != null) {
            transaction.setType(UpdateTransaction.type());
        }
        return repository.save(transaction);
    }

    public List<Transaction> buscarTransacoesComFiltro(LocalDate dataInicio, LocalDate dataFim, String tipoStr, BigDecimal valorMin, BigDecimal valorMax) {
        Long userId = authService.getAuhenticatedUser().getId();
        Type tipo = tipoStr != null ? Type.valueOf(tipoStr.toUpperCase()) : null;
        return repository.buscarFiltradas(dataInicio, dataFim, tipo, valorMin, valorMax, userId
        );
    }

    public BigDecimal calcularSaldo(){
        Long userId = authService.getAuhenticatedUser().getId();
        BigDecimal expense = transactionRepository.somarPorTipo(Type.EXPENSE, userId);
        BigDecimal revenue = transactionRepository.somarPorTipo(Type.REVENUE, userId);
        return revenue.subtract(expense);
    }

}
