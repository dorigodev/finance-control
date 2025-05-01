package dev.dorigo.financecontrol.service;

import dev.dorigo.financecontrol.domain.transaction.Transaction;
import dev.dorigo.financecontrol.domain.user.User;
import dev.dorigo.financecontrol.repository.TransactionRepository;
import dev.dorigo.financecontrol.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository repository;
    private final AuthService authService;


    public Transaction save(Transaction transaction) {
        transaction.setUser(authService.getAuhenticatedUser());
        return repository.save(transaction);

    }
}
