package dev.dorigo.financecontrol.controller;

import dev.dorigo.financecontrol.controller.request.TransactionRequest;
import dev.dorigo.financecontrol.controller.response.TransactionResponse;
import dev.dorigo.financecontrol.controller.response.UserResponse;
import dev.dorigo.financecontrol.domain.transaction.Transaction;
import dev.dorigo.financecontrol.domain.transaction.Type;
import dev.dorigo.financecontrol.mappers.TransactionMapper;
import dev.dorigo.financecontrol.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name="Transactions Controller", description = "Recurso responsavel pelo gerenciamento de todas as transações.")
public class TransactionController {

    private final TransactionService transactionService;

    /*
    * @PostMapping
    public ResponseEntity<TransactionResponse> save(@RequestBody TransactionRequest transactionRequest) {
        Transaction saved = transactionService.save(TransactionMapper.toTransaction(transactionRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(TransactionMapper.toResponse(saved));
    }
     */

    @Operation(summary = "Salvar um novo gasto",
            description = "Método responsável por realizar o salvamento de um novo gasto",
            security = @SecurityRequirement(name="bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Novo gasto salvo  com sucesso",
            content = @Content(schema = @Schema(implementation = TransactionResponse.class)))
    @PostMapping("/expense")
    public ResponseEntity<TransactionResponse> saveExpenseTransaction(@RequestBody TransactionRequest transactionRequest) {
        Transaction saved = transactionService.save(transactionRequest,Type.EXPENSE);
        return ResponseEntity.status(HttpStatus.CREATED).body(TransactionMapper.toResponse(saved));
    }

    @Operation(summary = "Salvar um novo ganho",
            description = "Método responsável por realizar o salvamento de um novo ganho",
            security = @SecurityRequirement(name="bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Novo gasto salvo  com sucesso",
            content = @Content(schema = @Schema(implementation = TransactionResponse.class)))
    @PostMapping("/revenue")
    public ResponseEntity<TransactionResponse> saveRevenueTransaction(@RequestBody TransactionRequest transactionRequest) {
        Transaction saved = transactionService.save(transactionRequest,Type.REVENUE);
        return ResponseEntity.status(HttpStatus.CREATED).body(TransactionMapper.toResponse(saved));
    }

    @Operation(summary = "Buscar todas as transações",
            description = "Método responsável por retornar todos as transações",
            security = @SecurityRequirement(name="bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Transações encontradas com sucesso ",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TransactionResponse.class))))
    @GetMapping()
    public ResponseEntity<List<TransactionResponse>> getTransactions() {
        return ResponseEntity.ok(transactionService.getAll().stream().map(TransactionMapper::toResponse).toList());
    }

    @Operation(summary = "Buscar um transação por id",
            description = "Método responsável por buscar uma transação pelo seu id",
            security = @SecurityRequirement(name="bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Transação encontrada com sucesso",
            content = @Content(schema = @Schema(implementation = TransactionResponse.class)))
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable Long id) {
        return ResponseEntity.ok(TransactionMapper.toResponse(transactionService.getById(id)));
    }

    @Operation(summary = "Deletar uma transação por id",
            description = "Método responsável por deletar uma transação pelo seu id",
            security = @SecurityRequirement(name="bearerAuth"))
    @ApiResponse(responseCode = "204", description = "Transação deletada com sucesso",
    content=@Content())
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransactionById(@PathVariable Long id) {
        Optional<Transaction> optTransaction = Optional.ofNullable(transactionService.getById(id));
        if (optTransaction.isPresent()) {
            transactionService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Atualizar uma transação completa por ID",
            description = "Método responsável por atualizar todos os campos de uma transação pelo seu id",
            security = @SecurityRequirement(name="bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Transação atualizada com sucesso",
            content=@Content(schema = @Schema(implementation = TransactionResponse.class)))
    @PutMapping("/{id}")
    public ResponseEntity<TransactionResponse> updateTransactionById(@PathVariable Long id, @RequestBody TransactionRequest transactionRequest) {
        Transaction transaction = transactionService.update(id,transactionRequest);
       return ResponseEntity.ok(TransactionMapper.toResponse(transaction));
    }

    @Operation(summary = "Atualizar campos de transação por ID",
            description = "Método responsável por atualizar alguns campos de uma transação pelo seu id",
            security = @SecurityRequirement(name="bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Transação atualizada com sucesso",
            content=@Content(schema = @Schema(implementation = TransactionResponse.class)))
    @PatchMapping("/{id}")
    public ResponseEntity<TransactionResponse> updateTransaction(@PathVariable Long id, @RequestBody TransactionRequest transactionRequest) {
        Transaction transaction = transactionService.partialUpdate(id, transactionRequest);
        return ResponseEntity.ok(TransactionMapper.toResponse(transaction));
    }

    @Operation(summary = "Buscar transações com filtro",
            description = "Método responsável por retornar todos as transações de acordo com um certo filtro",
            security = @SecurityRequirement(name="bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Transações com os filtros encontradas com sucesso ",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = TransactionResponse.class))))
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

    @Operation(summary = "Verificar saldo total",
            description = "Método responsável por verificar o saldo de um usuário",
            security = @SecurityRequirement(name="bearerAuth"))
    @ApiResponse(responseCode = "200", description = "Saldo retornado com sucesso.")
    @GetMapping("/saldo")
    public ResponseEntity<BigDecimal> getSaldo() {
        BigDecimal saldo = transactionService.calcularSaldo();
        return ResponseEntity.ok(saldo);
    }
}





