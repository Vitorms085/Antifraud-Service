package br.com.vitor.antifraud.service.core.bankaccount.service;

import br.com.vitor.antifraud.service.core.bankaccount.exceptions.BankAccountNotFoundException;
import br.com.vitor.antifraud.service.core.bankaccount.repository.BankAccountRepository;
import br.com.vitor.antifraud.service.model.bankaccount.BankAccount;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BankAccountServiceTest {

    @Mock
    private BankAccountRepository repository;

    @InjectMocks
    private BankAccountService service;

    @Test
    void shouldFindBankAccountById() {
        var id = UUID.randomUUID();
        var bankAccount = new BankAccount();
        bankAccount.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(bankAccount));

        var result = service.findById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void shouldThrowWhenBankAccountNotFound() {
        var id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(BankAccountNotFoundException.class, () -> service.findById(id));
    }
}
