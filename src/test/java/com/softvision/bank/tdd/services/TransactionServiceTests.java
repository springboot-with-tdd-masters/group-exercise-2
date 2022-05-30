package com.softvision.bank.tdd.services;

import com.softvision.bank.tdd.AccountMocks;
import com.softvision.bank.tdd.ApplicationConstants;
import com.softvision.bank.tdd.exceptions.BadRequestException;
import com.softvision.bank.tdd.exceptions.RecordNotFoundException;
import com.softvision.bank.tdd.model.Account;
import com.softvision.bank.tdd.model.CheckingAccount;
import com.softvision.bank.tdd.model.RegularAccount;
import com.softvision.bank.tdd.model.Transaction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import com.softvision.bank.tdd.repository.AccountRepository;
import com.softvision.bank.tdd.repository.TransactionRepository;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.softvision.bank.tdd.AccountMocks.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static java.util.Optional.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTests {

    @Mock
    AccountRepository mockedAccountRepository;
    
    @Mock 
    TransactionRepository mockedTransactionRepository;
    
    @InjectMocks
    TransactionService transactionService = new TransactionServiceImpl();

    @Captor
    ArgumentCaptor<Account> saveAccountCaptor;

    @Test
    @DisplayName("Deposit Regular Account - Should add account deposited")
    void test_deposit_regular()  {
        //setup mocked returns
        double amountDeposited = 100.0;
        Account mockedRegularAccount = getMockRegularAccount();
        when(mockedAccountRepository.findById(REG_MOCK_ACCT_ID)).thenReturn(of(mockedRegularAccount));
        when(mockedAccountRepository.save(saveAccountCaptor.capture())).thenReturn(mockedRegularAccount);

        //run
        Account actualAccount = transactionService.transact(REG_MOCK_ACCT_ID,
                new Transaction(ApplicationConstants.DEPOSIT, amountDeposited));

        //check argument passed at save call
        assertSame(saveAccountCaptor.getValue(), actualAccount);
        //check returned account
        assertEquals(REG_MOCK_BALANCE + amountDeposited,
                actualAccount.getBalance());
        //verify number of resource calls
        verify(mockedAccountRepository, atMostOnce()).findById(anyLong());
        verify(mockedAccountRepository, atMostOnce()).save(any());
    }

    @Test
    @DisplayName("Deposit Checking Account - Should add account deposited, with charge")
    void test_deposit_checking()  {
        //setup mocked returns
        double amountDeposited = 100.0;
        Account mockCheckingAccount = getMockCheckingAccount();
        when(mockedAccountRepository.findById(CHK_MOCK_ACCT_ID)).thenReturn(of(mockCheckingAccount));
        when(mockedAccountRepository.save(saveAccountCaptor.capture())).thenReturn(mockCheckingAccount);

        //run
        Account actualAccount = transactionService.transact(CHK_MOCK_ACCT_ID,
                new Transaction(ApplicationConstants.DEPOSIT, amountDeposited));

        //check argument passed at save call
        assertSame(saveAccountCaptor.getValue(), actualAccount);
        //check returned account
        assertEquals(CHK_MOCK_BALANCE + amountDeposited - ApplicationConstants.CHK_CHARGE,
                actualAccount.getBalance());
        //verify number of resource calls
        verify(mockedAccountRepository, atMostOnce()).findById(anyLong());
        verify(mockedAccountRepository, atMostOnce()).save(any());
    }

    @Test
    @DisplayName("Withdraw Regular Account - Should subtract amount withdrawn")
    void test_withdraw_regularAcct()  {
        //setup mocked returns
        double amountWithdrawn = 100.0;
        Account mockedRegularAccount = getMockRegularAccount();
        when(mockedAccountRepository.findById(REG_MOCK_ACCT_ID)).thenReturn(of(mockedRegularAccount));
        when(mockedAccountRepository.save(saveAccountCaptor.capture())).thenReturn(mockedRegularAccount);

        //run
        Account actualAccount = transactionService.transact(REG_MOCK_ACCT_ID,
                new Transaction(ApplicationConstants.WITHDRAW, amountWithdrawn));

        //check argument passed at save call
        assertSame(saveAccountCaptor.getValue(), actualAccount);
        //check returned account
        assertEquals(REG_MOCK_BALANCE - amountWithdrawn,
                actualAccount.getBalance());
        //verify number of resource calls
        verify(mockedAccountRepository, atMostOnce()).findById(anyLong());
        verify(mockedAccountRepository, atMostOnce()).save(any());
    }

    @Test
    @DisplayName("Withdraw Regular Account - Should subtract amount withdrawn with penalties")
    void test_withdraw_regularAcct_withPenalties()  {
        //setup mocked returns
        double balanceUnderMinimum = ApplicationConstants.REG_MIN_BALANCE - 1;
        double amountWithdrawn = 100;
        Account mockedRegularAccount = getMockRegularAccount();
        mockedRegularAccount.setBalance(balanceUnderMinimum);
        when(mockedAccountRepository.findById(REG_MOCK_ACCT_ID)).thenReturn(of(mockedRegularAccount));
        when(mockedAccountRepository.save(saveAccountCaptor.capture())).thenReturn(mockedRegularAccount);

        //run - should fall under minimum balance (500)
        Account actualAccount = transactionService.transact(REG_MOCK_ACCT_ID,
                new Transaction(ApplicationConstants.WITHDRAW, amountWithdrawn));

        //check argument passed at save call
        assertSame(saveAccountCaptor.getValue(), actualAccount);
        //check returned account
        assertEquals(balanceUnderMinimum - amountWithdrawn - ApplicationConstants.REG_PENALTY,
                actualAccount.getBalance());
        //verify number of resource calls
        verify(mockedAccountRepository, atMostOnce()).findById(anyLong());
        verify(mockedAccountRepository, atMostOnce()).save(any());
    }


    @Test
    @DisplayName("Withdraw Checking Account- Should subtract amount withdrawn with charge")
    void test_withdraw_checking()  {
        //setup mocked returns
        double amountWithdrawn = 100;
        Account mockedCheckingAccount = getMockCheckingAccount();
        when(mockedAccountRepository.findById(CHK_MOCK_ACCT_ID)).thenReturn(of(mockedCheckingAccount));
        when(mockedAccountRepository.save(saveAccountCaptor.capture())).thenReturn(mockedCheckingAccount);

        //run
        Account actualAccount = transactionService.transact(CHK_MOCK_ACCT_ID,
                new Transaction(ApplicationConstants.WITHDRAW, amountWithdrawn));

        //check argument passed at save call
        assertSame(saveAccountCaptor.getValue(), actualAccount);
        //check returned account
        assertEquals(CHK_MOCK_BALANCE - amountWithdrawn - ApplicationConstants.CHK_CHARGE,
                actualAccount.getBalance());
        //verify number of resource calls
        verify(mockedAccountRepository, atMostOnce()).findById(anyLong());
        verify(mockedAccountRepository, atMostOnce()).save(any());
    }

    @Test
    @DisplayName("Withdraw Checking Account- Should subtract amount withdrawn with penalty and charge")
	void test_withdraw_checking_withPenaltyAndCharge() {
		// setup mocked returns
        double amountWithdrawn = 50;
        double balanceUnderMin = 90;
        Account mockedCheckingAccount = getMockCheckingAccount();
        mockedCheckingAccount.setBalance(balanceUnderMin);
        when(mockedAccountRepository.findById(CHK_MOCK_ACCT_ID)).thenReturn(of(mockedCheckingAccount));
        when(mockedAccountRepository.save(saveAccountCaptor.capture())).thenReturn(mockedCheckingAccount);

        //run
        Account actualAccount = transactionService.transact(CHK_MOCK_ACCT_ID,
                new Transaction(ApplicationConstants.WITHDRAW, amountWithdrawn));

        //check argument passed at save call
        assertSame(saveAccountCaptor.getValue(), actualAccount);
        //check returned account
        assertEquals(balanceUnderMin - amountWithdrawn - ApplicationConstants.CHK_CHARGE - ApplicationConstants.CHK_PENALTY,
                actualAccount.getBalance());
        //verify number of resource calls
        verify(mockedAccountRepository, atMostOnce()).findById(anyLong());
        verify(mockedAccountRepository, atMostOnce()).save(any());
    }

    @Test
    @DisplayName("Account - Should throw RecordNotFoundException when account is not found in repo")
    void test_transaction_fail_throwException_RecordNotFoundException() {
        when(mockedAccountRepository.findById(CHK_MOCK_ACCT_ID)).thenReturn(empty());

        assertThrows(RecordNotFoundException.class, () ->
                transactionService.transact(CHK_MOCK_ACCT_ID, new Transaction()));
        verify(mockedAccountRepository, atMostOnce()).findById(anyLong());
    }

    @Test
    @DisplayName("Account - Should throw BadRequestException on bad request/parameters")
    void test_transaction_fail_throwException_BadRequestException() {
        Account mockedCheckingAccount = getMockCheckingAccount();
        when(mockedAccountRepository.findById(CHK_MOCK_ACCT_ID)).thenReturn(of(mockedCheckingAccount));

        assertThrows(BadRequestException.class, () ->
                transactionService.transact(CHK_MOCK_ACCT_ID, new Transaction("illegal transaction", 100)));
        assertThrows(BadRequestException.class, () ->
                transactionService.transact(CHK_MOCK_ACCT_ID, new Transaction(null, 100)));
        verify(mockedAccountRepository, atMost(2)).findById(anyLong());
    }

    @Test
    @DisplayName("Should get transactions with pageable sorted by Amount")
    void test_get_transaction_sorted_by_amount() {
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(buildTransaction(AccountMocks.getMockRegularAccount(), 100, "DEPOSIT"));
        transactions.add(buildTransaction(AccountMocks.getMockRegularAccount(), 200, "WITHDRAW"));

        Pageable pageRequest = PageRequest.of(0, 3, Sort.by("amount").descending());
        List<Transaction> sortedAccounts = transactions.stream().sorted(Comparator.comparing(Transaction::getAmount).reversed()).collect(Collectors.toList());
        Page<Transaction> accountPage = new PageImpl<>(sortedAccounts);
        when(transactionService.readTransactions(pageRequest)).thenReturn(accountPage);

        Page<Transaction> retrievedTransactionPage = transactionService.readTransactions(pageRequest);

         assertAll(() -> assertEquals(200, retrievedTransactionPage.getContent().get(0).getAmount()),
                () -> assertEquals(100, retrievedTransactionPage.getContent().get(1).getAmount()));
    }

    private Transaction buildTransaction(RegularAccount mockRegularAccount, double amount, String type) {
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setAccount(mockRegularAccount);
        return transaction;
    }
}
