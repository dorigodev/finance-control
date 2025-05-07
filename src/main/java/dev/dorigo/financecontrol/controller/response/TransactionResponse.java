package dev.dorigo.financecontrol.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import dev.dorigo.financecontrol.domain.transaction.Type;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record TransactionResponse(Long id,
                                  String description,
                                  BigDecimal amount,
                                  Type type,
                                  @JsonFormat(pattern = "dd/MM/yyyy")
                                  LocalDate date) {
}
