package br.com.vitor.antifraud.service.core.suspiciousbanktransaction.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class SuspiciousTransactionFilterDTO {
    @NotNull(message = "Start date is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDate;

    @NotNull(message = "End date is required")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endDate;

    private UUID bankAccountId;
}
