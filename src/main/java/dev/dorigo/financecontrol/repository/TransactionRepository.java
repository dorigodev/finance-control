package dev.dorigo.financecontrol.repository;

import dev.dorigo.financecontrol.domain.transaction.Transaction;
import dev.dorigo.financecontrol.domain.transaction.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findTransactionByUser_Id(Long id);

    @Query("""
    SELECT t FROM Transaction t
    WHERE (:dataInicio IS NULL OR t.date >= :dataInicio)
      AND (:dataFim IS NULL OR t.date <= :dataFim)
      AND (:tipo IS NULL OR t.type = :tipo)
      AND (:valorMin IS NULL OR t.amount >= :valorMin)
      AND (:valorMax IS NULL OR t.amount <= :valorMax)
      AND t.user.id = :userId
""")
    List<Transaction> buscarFiltradas(
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            @Param("tipo") Type tipo,
            @Param("valorMin") BigDecimal valorMin,
            @Param("valorMax") BigDecimal valorMax,
            @Param("userId") Long userId
    );
}
