package br.com.vitor.antifraud.service.api.analysis;

import br.com.vitor.antifraud.service.api.IntegrationTest;
import br.com.vitor.antifraud.service.model.bankaccount.BankAccount;
import br.com.vitor.antifraud.service.model.banktransaction.BankTransaction;
import br.com.vitor.antifraud.service.model.banktransaction.TransactionType;
import br.com.vitor.antifraud.service.model.suspicioustransaction.AnalysisStatus;
import br.com.vitor.antifraud.service.model.suspicioustransaction.SuspiciousBankTransaction;
import br.com.vitor.antifraud.service.core.bankaccount.repository.BankAccountRepository;
import br.com.vitor.antifraud.service.core.suspiciousbanktransaction.repository.SuspiciousTransactionRepository;
import br.com.vitor.antifraud.service.core.transaction.repository.BankTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class AnalysisControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private BankTransactionRepository bankTransactionRepository;

    @Autowired
    private SuspiciousTransactionRepository suspiciousTransactionRepository;

    private BankAccount testBankAccount;
    private BankTransaction testTransaction;
    private SuspiciousBankTransaction testSuspiciousTransaction;

    @BeforeEach
    void setUp() {
        suspiciousTransactionRepository.deleteAll();
        bankTransactionRepository.deleteAll();
        bankAccountRepository.deleteAll();

        testBankAccount = new BankAccount();
        testBankAccount.setId(UUID.randomUUID());
        testBankAccount.setCreditLimit(BigDecimal.valueOf(1000));
        testBankAccount.setDebitLimit(BigDecimal.valueOf(1000));
        testBankAccount.setIsActive(true);
        testBankAccount = bankAccountRepository.save(testBankAccount);

        testTransaction = new BankTransaction();
        testTransaction.setId(UUID.randomUUID());
        testTransaction.setBankAccountId(testBankAccount.getId());
        testTransaction.setValue(BigDecimal.valueOf(1000));
        testTransaction.setType(TransactionType.CREDIT);
        testTransaction.setDateTime(LocalDateTime.now());
        testTransaction = bankTransactionRepository.save(testTransaction);

        testSuspiciousTransaction = new SuspiciousBankTransaction();
        testSuspiciousTransaction.setId(UUID.randomUUID());
        testSuspiciousTransaction.setBankTransactionId(testTransaction.getId());
        testSuspiciousTransaction.setStatus(AnalysisStatus.UNDER_ANALYSIS);
        testSuspiciousTransaction.setCreatedAt(LocalDateTime.now());
        testSuspiciousTransaction.setReason("Test reason");
        testSuspiciousTransaction = suspiciousTransactionRepository.save(testSuspiciousTransaction);
    }

    @Test
    void shouldGetAnalysisStatusById() {
        ResponseEntity<AnalysisStatusDTO> response = restTemplate.getForEntity(
            "/analises/{id}",
            AnalysisStatusDTO.class,
            testTransaction.getId()
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getBankTransactionId()).isEqualTo(testTransaction.getId());
        assertThat(response.getBody().getStatus()).isEqualTo(AnalysisStatus.UNDER_ANALYSIS);
    }

    @Test
    void shouldReturnNotFoundWhenAnalysisDoesNotExist() {
        ResponseEntity<AnalysisStatusDTO> response = restTemplate.getForEntity(
            "/analises/{id}",
            AnalysisStatusDTO.class,
            UUID.randomUUID()
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldFindSuspiciousTransactionsWithPagination() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(1);
        LocalDateTime endDate = LocalDateTime.now().plusDays(1);

        String url = UriComponentsBuilder.fromPath("/analises/suspeitas")
            .queryParam("startDate", startDate)
            .queryParam("endDate", endDate)
            .toUriString();

        ResponseEntity<RestPage<AnalysisStatusDTO>> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<RestPage<AnalysisStatusDTO>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getContent()).hasSize(1);
        assertThat(response.getBody().getContent().get(0).getBankTransactionId()).isEqualTo(testTransaction.getId());
    }

    @Test
    void shouldUpdateAnalysisStatus() {
        UpdateAnalysisStatusRequest request = new UpdateAnalysisStatusRequest();
        request.setStatus(AnalysisStatus.APPROVED);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<UpdateAnalysisStatusRequest> requestEntity = new HttpEntity<>(request, headers);

        ResponseEntity<AnalysisStatusDTO> response = restTemplate.exchange(
            "/analises/{transactionId}",
            HttpMethod.PATCH,
            requestEntity,
            AnalysisStatusDTO.class,
            testTransaction.getId()
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(AnalysisStatus.APPROVED);

        SuspiciousBankTransaction updated = suspiciousTransactionRepository.findByBankTransactionId(testTransaction.getId()).orElseThrow();
        assertThat(updated.getStatus()).isEqualTo(AnalysisStatus.APPROVED);
    }
}
