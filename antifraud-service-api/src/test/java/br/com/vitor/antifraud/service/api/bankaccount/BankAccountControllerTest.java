package br.com.vitor.antifraud.service.api.bankaccount;

import br.com.vitor.antifraud.service.api.IntegrationTest;
import br.com.vitor.antifraud.service.model.bankaccount.BankAccount;
import br.com.vitor.antifraud.service.core.bankaccount.repository.BankAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class BankAccountControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    private BankAccount testBankAccount;

    @BeforeEach
    void setUp() {
        bankAccountRepository.deleteAll();

        testBankAccount = new BankAccount();
        testBankAccount.setId(UUID.randomUUID());
        testBankAccount.setCreditLimit(BigDecimal.valueOf(1000));
        testBankAccount.setDebitLimit(BigDecimal.valueOf(1000));
        testBankAccount.setIsActive(true);
        testBankAccount = bankAccountRepository.save(testBankAccount);
    }

    @Test
    void shouldReturnBankAccountWhenExists() {
        ResponseEntity<BankAccountDTO> response = restTemplate.getForEntity(
            "/contas/{id}",
            BankAccountDTO.class,
            testBankAccount.getId()
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(testBankAccount.getId());
        assertThat(response.getBody().getCreditLimit()).isEqualTo(testBankAccount.getCreditLimit());
        assertThat(response.getBody().getDebitLimit()).isEqualTo(testBankAccount.getDebitLimit());
        assertThat(response.getBody().getIsActive()).isEqualTo(testBankAccount.getIsActive());
    }

    @Test
    void shouldReturnNotFoundWhenBankAccountDoesNotExist() {
        UUID nonExistentId = UUID.randomUUID();

        ResponseEntity<BankAccountDTO> response = restTemplate.getForEntity(
            "/contas/{id}",
            BankAccountDTO.class,
            nonExistentId
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
