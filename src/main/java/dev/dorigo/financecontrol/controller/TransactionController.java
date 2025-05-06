package dev.dorigo.financecontrol.controller;

import dev.dorigo.financecontrol.controller.request.TransactionRequest;
import dev.dorigo.financecontrol.controller.response.TransactionResponse;
import dev.dorigo.financecontrol.domain.transaction.Transaction;
import dev.dorigo.financecontrol.domain.transaction.Type;
import dev.dorigo.financecontrol.mappers.TransactionMapper;
import dev.dorigo.financecontrol.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    /*
    * @PostMapping
    public ResponseEntity<TransactionResponse> save(@RequestBody TransactionRequest transactionRequest) {
        Transaction saved = transactionService.save(TransactionMapper.toTransaction(transactionRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(TransactionMapper.toResponse(saved));
    }
     */

    @PostMapping("/expense")
    public ResponseEntity<TransactionResponse> saveExpenseTransaction(@RequestBody TransactionRequest transactionRequest) {
        Transaction saved = transactionService.save(transactionRequest,Type.EXPENSE);
        return ResponseEntity.status(HttpStatus.CREATED).body(TransactionMapper.toResponse(saved));
    }

    @PostMapping("/revenue")
    public ResponseEntity<TransactionResponse> saveRevenueTransaction(@RequestBody TransactionRequest transactionRequest) {
        Transaction saved = transactionService.save(transactionRequest,Type.REVENUE);
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

    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> updateTransactionById(@PathVariable Long id, @RequestBody TransactionRequest transactionRequest) {
        Transaction transaction = transactionService.update(id,transactionRequest);
       return ResponseEntity.ok(TransactionMapper.toResponse(transaction));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TransactionResponse> updateTransaction(@PathVariable Long id, @RequestBody TransactionRequest transactionRequest) {
        Transaction transaction = transactionService.partialUpdate(id, transactionRequest);
        return ResponseEntity.ok(TransactionMapper.toResponse(transaction));
    }

    @GetMapping("/filter")
    public List<TransactionResponse> buscarFiltradas(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) BigDecimal valorMin,
            @RequestParam(required = false) BigDecimal valorMax
    ) {
        var resp = transactionService.buscarTransacoesComFiltro(dataInicio, dataFim, tipo, valorMin, valorMax);
        return resp.stream().map(TransactionMapper::toResponse).collect(Collectors.toList());
    }

    @GetMapping("/saldo")
    public ResponseEntity<BigDecimal> getSaldo() {
        BigDecimal saldo = transactionService.calcularSaldo();
        return ResponseEntity.ok(saldo);
    }
}





