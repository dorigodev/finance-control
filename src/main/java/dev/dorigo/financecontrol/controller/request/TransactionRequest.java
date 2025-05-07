package dev.dorigo.financecontrol.controller.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.dorigo.financecontrol.domain.transaction.Type;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequest(String description,
                                 BigDecimal amount,
                                 Type type,
                                 @JsonFormat(pattern = "dd/MM/yyyy")
                                 LocalDate date) {
}
