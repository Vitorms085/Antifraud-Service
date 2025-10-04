package br.com.vitor.antifraud.service.core.suspiciousbanktransaction.repository;

import br.com.vitor.antifraud.service.model.banktransaction.TransactionType;
import br.com.vitor.antifraud.service.model.suspicioustransaction.AnalysisStatus;
import br.com.vitor.antifraud.service.model.suspicioustransaction.SuspiciousBankTransaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SuspiciousTransactionRepository extends PagingAndSortingRepository<SuspiciousBankTransaction, UUID>,
        CrudRepository<SuspiciousBankTransaction, UUID> {

    @Query("SELECT COUNT(1) > 0 " +
           "FROM SuspiciousBankTransaction sbt " +
           "JOIN BankTransaction bt ON bt.id = sbt.bankTransactionId " +
           "JOIN BankAccount ba ON ba.id = bt.bankAccountId " +
           "WHERE ba.id = :bankAccountId " +
           "AND sbt.createdAt >= :thresholdDate " +
           "AND sbt.status = :status")
    boolean existsPendingAnalysisInTheLast7Days(
            @Param("bankAccountId") UUID bankAccountId,
            @Param("thresholdDate") LocalDateTime thresholdDate,
            @Param("status") AnalysisStatus status);

    @Query("SELECT SUM(bt.value)" +
            " FROM BankTransaction bt " +
            " JOIN BankAccount ba ON bt.bankAccountId = ba.id " +
            " WHERE bt.dateTime >= :dateTime AND bt.type = :TYPE" +
            " GROUP BY bt.bankAccountId" +
            " HAVING count(bt.id) > 1")
    Optional<BigDecimal> sumTransactionsValueInTheLast2Minutes(@Param("dateTime") LocalDateTime dateTime,
                                                               @Param("TYPE") TransactionType type);

    Optional<SuspiciousBankTransaction> findByBankTransactionId(UUID bankTransactionId);
}
