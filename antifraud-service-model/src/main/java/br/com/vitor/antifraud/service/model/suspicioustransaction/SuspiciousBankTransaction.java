package br.com.vitor.antifraud.service.model.suspicioustransaction;

import br.com.vitor.antifraud.service.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "suspicious_bank_transaction")
@Data
public class SuspiciousBankTransaction extends BaseEntity {

    private UUID bankTransactionId;

    private String reason;

    @Enumerated(EnumType.STRING)
    private AnalysisStatus status;
}
