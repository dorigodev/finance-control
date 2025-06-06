package dev.dorigo.financecontrol.controller.request;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionRequest(String description,
                                 BigDecimal amount,
                                 LocalDate date) {
}
