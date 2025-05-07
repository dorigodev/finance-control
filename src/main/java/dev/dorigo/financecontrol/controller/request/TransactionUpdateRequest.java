package dev.dorigo.financecontrol.controller.request;

import dev.dorigo.financecontrol.domain.transaction.Type;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionUpdateRequest(String description,
                                 BigDecimal amount,
                                 Type type,
                                 LocalDate date) {
}
