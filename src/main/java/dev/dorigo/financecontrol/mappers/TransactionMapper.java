package dev.dorigo.financecontrol.mappers;

import dev.dorigo.financecontrol.controller.request.TransactionRequest;
import dev.dorigo.financecontrol.controller.response.TransactionResponse;
import dev.dorigo.financecontrol.domain.transaction.Transaction;
import lombok.experimental.UtilityClass;


@UtilityClass
public class TransactionMapper {
    public static Transaction toTransaction(TransactionRequest request) {
        return Transaction.builder()
                .description(request.description())
                .amount(request.amount())
                .type(request.type())
                .date(request.date())
                .build();
    }

    public static TransactionResponse toResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .description(transaction.getDescription())
                .amount(transaction.getAmount())
                .type(transaction.getType())
                .date(transaction.getDate())
                .build();
    }
}
