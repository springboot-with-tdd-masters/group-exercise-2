package com.masters.masters.exercise.service;

import com.masters.masters.exercise.exception.AmountExceededException;
import com.masters.masters.exercise.model.Account;
import com.masters.masters.exercise.model.CheckingAccount;
import com.masters.masters.exercise.model.InterestAccount;
import com.masters.masters.exercise.repository.AccountRepository;
import com.masters.masters.exercise.services.TransactionImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.time.Clock;
import java.time.LocalDate;
import java.time.ZoneId;
public class TransactionImplTest {

    @Mock
    AccountRepository repo;

    @InjectMocks
    TransactionImpl service;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }
    
    
    @Test
    public void depositInterestAccount() throws AmountExceededException{
    	InterestAccount mockInterest = new InterestAccount();
    	mockInterest.setName("interestName");
    	InterestAccount interestAccount = new InterestAccount();
    	interestAccount.setName("interestName");
    	interestAccount.setBalance(50.0);
        when(repo.save(Mockito.any(Account.class))).thenReturn(interestAccount);
        Account updatedAccount = service.deposit(mockInterest,50.0);
        Assertions.assertEquals(updatedAccount.getBalance(),50.0);
    }
    
    @Test
    public void withdrawInterestAccount() throws AmountExceededException{
    	InterestAccount mockInterest = new InterestAccount();
    	mockInterest.setName("interestName");
    	mockInterest.setBalance(100.00);
    	InterestAccount interestAccouint = new InterestAccount();
    	interestAccouint.setName("interestName");
    	interestAccouint.setBalance(50.0);
        when(repo.save(Mockito.any(Account.class))).thenReturn(interestAccouint);
        Account updatedAccount = service.withdraw(mockInterest,50.0);
        Assertions.assertEquals(updatedAccount.getBalance(),50.0);
    }

    @Test
    public void depositCheckingAccount() throws AmountExceededException{
        CheckingAccount mockRequestCa = new CheckingAccount();
        mockRequestCa.setName("ca");
        CheckingAccount mockCa = new CheckingAccount();
        mockCa.setName("ca");
        mockCa.setBalance(109.0);
        when(repo.save(Mockito.any(Account.class))).thenReturn(mockCa);
        Account updatedAccount = service.deposit(mockRequestCa,10.0);
        Assertions.assertEquals(updatedAccount.getBalance(),109.0);
    }

    @Test
    public void withdrawCheckingAccount() throws AmountExceededException{
        CheckingAccount mockRequestCa = new CheckingAccount();
        mockRequestCa.setName("ca");
        CheckingAccount mockCa = new CheckingAccount();
        mockCa.setName("ca");
        mockCa.setBalance(89.0);
        when(repo.save(Mockito.any(Account.class))).thenReturn(mockCa);
        Account updatedAccount = service.withdraw(mockRequestCa,10.0);
        Assertions.assertEquals(updatedAccount.getBalance(),89.0);
    }

    @Test
    public void withdrawCheckingAccountBelowBalance() throws AmountExceededException{
        CheckingAccount mockRequestCa = new CheckingAccount();
        mockRequestCa.setName("ca");
        mockRequestCa.setBalance(90);
        CheckingAccount mockCa = new CheckingAccount();
        mockCa.setName("ca");
        mockCa.setBalance(69.0);
        when(repo.save(Mockito.any(Account.class))).thenReturn(mockCa);
        Account updatedAccount = service.withdraw(mockRequestCa,10.0);
        Assertions.assertEquals(updatedAccount.getBalance(),69.0);
    }

    @Test
    public void withdrawAccountInsufficientBalance() throws AmountExceededException{
    	InterestAccount account = new InterestAccount();
    	account.setName("ia");
    	account.setBalance(100.0);
    	Exception exception = assertThrows(AmountExceededException.class, () -> {
    		service.withdraw(account,200.0);
        });
        assertEquals("Insufficient Funds",exception.getMessage());
    }
    
    @Test
    public void depositAccountInsufficientBalance() throws AmountExceededException{
    	InterestAccount account = new InterestAccount();
    	account.setName("ia");
    	account.setBalance(100.0);
    	Exception exception = assertThrows(AmountExceededException.class, () -> {
    		service.deposit(account,-1);
        });
        assertEquals("Insufficient Funds",exception.getMessage());
    }

}
