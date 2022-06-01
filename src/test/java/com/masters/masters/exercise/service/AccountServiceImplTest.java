package com.masters.masters.exercise.service;

import com.masters.masters.exercise.exception.AccountExistException;
import com.masters.masters.exercise.exception.AmountExceededException;
import com.masters.masters.exercise.exception.InvalidTypeException;
import com.masters.masters.exercise.exception.RecordNotFoundException;
import com.masters.masters.exercise.model.Account;
import com.masters.masters.exercise.model.CheckingAccount;
import com.masters.masters.exercise.model.InterestAccount;
import com.masters.masters.exercise.model.TransactionType;
import com.masters.masters.exercise.model.dto.AccountDto;
import com.masters.masters.exercise.model.dto.TransactionDto;
import com.masters.masters.exercise.repository.AccountRepository;
import com.masters.masters.exercise.services.AccountServiceImpl;
import com.masters.masters.exercise.services.TransactionImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AccountServiceImplTest {

    @Mock
    AccountRepository repo;

    @Mock
    TransactionImpl transaction;

    @InjectMocks
    AccountServiceImpl service;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
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
        verify(repo).save(Mockito.any(Account.class));
        Assertions.assertThat(acct).extracting("name","minimumBalance","penalty","transactionCharge","balance")
                .containsExactly("name",100.0,10.0,1.0,100.0);
    }

    @Test
    public void invalidAccounType() {
        AccountDto dto = new AccountDto();
        dto.setName("name");
        dto.setType("CHECKING");
        CheckingAccount ca = new CheckingAccount();
        ca.setName(dto.getName());
        when(repo.save(Mockito.any(Account.class))).thenReturn(ca);
    }

    @Test
    public void getAllAccounts() {
        CheckingAccount ca = new CheckingAccount();
        ca.setName("name");
        CheckingAccount ca2 = new CheckingAccount();
        ca2.setName("name2");
        Pageable pageable = PageRequest.of(0,20);
        when(repo.findAll(pageable)).thenReturn(new PageImpl<>(List.of(ca,ca2)));
        Page<Account> accountPage = service.getAllAccounts(pageable);
        verify(repo).findAll(pageable);
        assertEquals(2,accountPage.getContent().size());
    }

    @Test
    public void getAccountById() throws RecordNotFoundException, AccountExistException, InvalidTypeException {
        CheckingAccount ca = new CheckingAccount();
        ca.setName("name");
        ca.setAcctNumber("123123");
        ca.setBalance(100.0);
        ca.setId(Long.parseLong("1"));
        when(repo.findById(Long.parseLong("1"))).thenReturn(Optional.of(ca));
        Account acct = service.getAccountById(Long.parseLong("1"));
        verify(repo).findById(Long.parseLong("1"));
        Assertions.assertThat(acct).extracting("name","acctNumber","balance","id")
                .containsExactly("name","123123",100.0,Long.parseLong("1"));
    }

    @Test
    public void getAccountByIdAccountDoesNotExist() throws AmountExceededException {
        when(repo.findById(Long.parseLong("1"))).thenReturn(Optional.empty());
        Exception exception = assertThrows(RecordNotFoundException.class, () -> {
            service.getAccountById(Long.parseLong("1"));
        });
        assertEquals("No Record found with id: 1",exception.getMessage());
    }

    @Test
    public void transact() {
        Account ca = new CheckingAccount();
        ca.setName("name");
        ca.setBalance(100.0);
        ca.setAcctNumber("123123");
        ca.setId(Long.parseLong("1"));
        TransactionDto dto = new TransactionDto();
        dto.setType(TransactionType.DEPOSIT.name());
        dto.setAmount(1.0);
        when(repo.findById(Long.parseLong("1"))).thenReturn(Optional.of(ca));
        when(repo.save(ca)).thenReturn(ca);
        Account newAcct =  service.transact(Long.parseLong("1"),dto);
        verify(repo).findById(Long.parseLong("1"));
        verify(repo).save(ca);
        verify(transaction).create(ca,dto);
        assertEquals(100,newAcct.getBalance());
    }

}
