package br.com.vitor.antifraud.service.api.transaction;

import br.com.vitor.antifraud.service.api.IntegrationTest;
import br.com.vitor.antifraud.service.model.bankaccount.BankAccount;
import br.com.vitor.antifraud.service.model.banktransaction.BankTransaction;
import br.com.vitor.antifraud.service.model.banktransaction.TransactionType;
import br.com.vitor.antifraud.service.core.bankaccount.repository.BankAccountRepository;
import br.com.vitor.antifraud.service.core.transaction.repository.BankTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class BankTransactionControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private BankTransactionRepository bankTransactionRepository;

    private BankAccount testBankAccount;
    private BankTransaction testTransaction;

    @BeforeEach
    void setUp() {
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
    }

    @Test
    void shouldCreateTransaction() {
        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setBankAccountId(testBankAccount.getId());
        request.setValue(BigDecimal.valueOf(500));
        request.setType(TransactionType.CREDIT);
        request.setDateTime(LocalDateTime.now());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateTransactionRequest> requestEntity = new HttpEntity<>(request, headers);

        ResponseEntity<TransactionDTO> response = restTemplate.exchange(
            "/transacoes",
            HttpMethod.POST,
            requestEntity,
            TransactionDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getBankAccountId()).isEqualTo(testBankAccount.getId());
        assertThat(response.getBody().getValue()).isEqualTo(request.getValue());
        assertThat(response.getBody().getType()).isEqualTo(request.getType());
    }

    @Test
    void shouldReturnBadRequestWhenCreatingTransactionWithInvalidBankAccount() {
        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setBankAccountId(UUID.randomUUID());
        request.setValue(BigDecimal.valueOf(500));
        request.setType(TransactionType.CREDIT);
        request.setDateTime(LocalDateTime.now());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CreateTransactionRequest> requestEntity = new HttpEntity<>(request, headers);

        ResponseEntity<TransactionDTO> response = restTemplate.exchange(
            "/transacoes",
            HttpMethod.POST,
            requestEntity,
            TransactionDTO.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}
