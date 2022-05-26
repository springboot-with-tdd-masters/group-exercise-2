package com.advancejava.groupexercise1.service;

import com.advancejava.groupexercise1.entity.Account;
import com.advancejava.groupexercise1.model.AccountRequest;
import com.advancejava.groupexercise1.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class BankServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private BankService bankService = new BankServiceImpl();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSave() {


        Account account = mock(Account.class);

        when(accountRepository.save(account))
                .thenReturn(account);

        //execute
        bankService.createAccount(account);

        //test
        verify(accountRepository)
                .save(account);
    }
}
