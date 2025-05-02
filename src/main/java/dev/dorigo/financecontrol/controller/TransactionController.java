package dev.dorigo.financecontrol.controller;

import dev.dorigo.financecontrol.controller.request.TransactionRequest;
import dev.dorigo.financecontrol.controller.response.TransactionResponse;
import dev.dorigo.financecontrol.domain.transaction.Transaction;
import dev.dorigo.financecontrol.mappers.TransactionMapper;
import dev.dorigo.financecontrol.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponse> save(@RequestBody TransactionRequest transactionRequest) {
        Transaction saved = transactionService.save(TransactionMapper.toTransaction(transactionRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(TransactionMapper.toResponse(saved));
    }

    @GetMapping()
    public ResponseEntity<List<TransactionResponse>> getTransactions() {
        return ResponseEntity.ok(transactionService.getAll().stream().map(TransactionMapper::toResponse).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(TransactionMapper.toResponse(transactionService.getById(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransactionById(@PathVariable Long id) {
        Optional<Transaction> optTransaction = Optional.ofNullable(transactionService.getById(id));
        if (optTransaction.isPresent()) {
            transactionService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}


