package com.masters.masters.exercise.service;

import com.masters.masters.exercise.exception.AccountExistException;
import com.masters.masters.exercise.exception.InvalidTypeException;
import com.masters.masters.exercise.exception.RecordNotFoundException;
import com.masters.masters.exercise.model.Account;
import com.masters.masters.exercise.model.CheckingAccount;
import com.masters.masters.exercise.model.InterestAccount;
import com.masters.masters.exercise.model.dto.AccountDto;
import com.masters.masters.exercise.repository.AccountRepository;
import com.masters.masters.exercise.services.AccountServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AccountServiceImplTest {

    @Mock
    AccountRepository repo;

    @InjectMocks
    AccountServiceImpl service;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }
    
    @Test
    public void createInterestAccountHappyPath() throws RecordNotFoundException, AccountExistException, InvalidTypeException {
        AccountDto dto = new AccountDto();
        dto.setName("name");
        dto.setType("interest");
        InterestAccount interestAccount = new InterestAccount();
        interestAccount.setName(dto.getName());
        when(repo.findByName(dto.getName())).thenReturn(Optional.empty());
        when(repo.save(Mockito.any(Account.class))).thenReturn(interestAccount);
        Account acct = service.createOrUpdateAccount(dto);
        verify(repo).findByName(dto.getName());
        verify(repo).save(Mockito.any(Account.class));
        Assertions.assertThat(acct).extracting("name","interestCharge")
                .containsExactly("name",0.03);
    }
    
    @Test
    public void createExistingInterestAccount() {
        AccountDto dto = new AccountDto();
        dto.setName("name");
        dto.setType("interest");
        InterestAccount interestAccount = new InterestAccount();
        interestAccount.setName(dto.getName());
        Optional<Account> existingAcct = Optional.of(interestAccount);
        when(repo.findByName(dto.getName())).thenReturn(existingAcct);
        Exception exception = assertThrows(AccountExistException.class, () -> {
            service.createOrUpdateAccount(dto);
        });
        assertEquals("The account is already exist",exception.getMessage());
    }

    @Test
    public void createCheckingAccountHappyPath() throws RecordNotFoundException, AccountExistException, InvalidTypeException {
        AccountDto dto = new AccountDto();
        dto.setName("name");
        dto.setType("CHECKING");
        CheckingAccount ca = new CheckingAccount();
        ca.setName(dto.getName());
        when(repo.findByName(dto.getName())).thenReturn(Optional.empty());
        when(repo.save(Mockito.any(Account.class))).thenReturn(ca);
        Account acct = service.createOrUpdateAccount(dto);
        verify(repo).findByName(dto.getName());
        verify(repo).save(Mockito.any(Account.class));
        Assertions.assertThat(acct).extracting("name","minimumBalance","penalty","transactionCharge","balance")
                .containsExactly("name",100.0,10.0,1.0,100.0);
    }

    @Test
    public void createExistingCheckingAccount() {
        AccountDto dto = new AccountDto();
        dto.setName("name");
        dto.setType("CHECKING");
        CheckingAccount ca = new CheckingAccount();
        ca.setName(dto.getName());
        Optional<Account> existingAcct = Optional.of(ca);
        when(repo.findByName(dto.getName())).thenReturn(existingAcct);
        Exception exception = assertThrows(AccountExistException.class, () -> {
            service.createOrUpdateAccount(dto);
        });
        assertEquals("The account is already exist",exception.getMessage());
    }

    @Test
    public void invalidAccounType() {
        AccountDto dto = new AccountDto();
        dto.setName("name");
        dto.setType("TD");
        CheckingAccount ca = new CheckingAccount();
        ca.setName(dto.getName());
        Exception exception = assertThrows(InvalidTypeException.class, () -> {
            service.createOrUpdateAccount(dto);
        });
        assertEquals("Invalid Account Type",exception.getMessage());
    }
}
