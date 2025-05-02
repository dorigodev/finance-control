package dev.dorigo.financecontrol.service;

import dev.dorigo.financecontrol.domain.transaction.Transaction;
import dev.dorigo.financecontrol.repository.TransactionRepository;
import dev.dorigo.financecontrol.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository repository;
    private final AuthService authService;
    private final UserRepository userRepository;


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
        var transaction = repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN));
        if(!transaction.getUser().equals(authService.getAuhenticatedUser())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        transaction.setDescription(UpdateTransaction.getDescription());
        transaction.setAmount(UpdateTransaction.getAmount());
        transaction.setDate(UpdateTransaction.getDate());
        transaction.setType(UpdateTransaction.getType());
        return repository.save(transaction);
    }
}
