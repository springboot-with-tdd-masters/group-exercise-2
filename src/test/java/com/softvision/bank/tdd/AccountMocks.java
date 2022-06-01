package com.softvision.bank.tdd;

import com.softvision.bank.tdd.model.CheckingAccount;
import com.softvision.bank.tdd.model.InterestAccount;
import com.softvision.bank.tdd.model.RegularAccount;

public class AccountMocks {

	public static final String MOCK_NAME = "Juan Dela Cruz";

    public static final long REG_MOCK_ACCT_ID = 1L;
    public static final String REG_MOCK_ACCT_NO = "213141323";
    public static final double REG_MOCK_BALANCE = 1500;
    public static final double REG_MOCK_INTEREST = 0.0;
    public static final double REG_MOCK_TRANSACTION = 0.0;

    public static final long CHK_MOCK_ACCT_ID = 2L;
    public static final String CHK_MOCK_ACCT_NO = "12315123";
    public static final double CHK_MOCK_BALANCE = 1000;
    public static final double CHK_MOCK_INTEREST = 0.0;

    public static final long INT_MOCK_ACCT_ID = 3L;
    public static final String INT_MOCK_ACCT_NO = "32132451";
    public static final double INT_MOCK_BALANCE = 1000;

    private AccountMocks() {}

    /** Checking Account
     *  Minimum/starting balance 100.00
     *  Penalty of 10.00 if balance falls below minimum
     *  Charge of 1.00 per transaction
     **/
    public static CheckingAccount getMockCheckingAccount() {
        CheckingAccount checkingAccount = new CheckingAccount();
        checkingAccount.setId(CHK_MOCK_ACCT_ID);
        checkingAccount.setName(MOCK_NAME);
        checkingAccount.setAcctNumber(CHK_MOCK_ACCT_NO);
        checkingAccount.setMinimumBalance(ApplicationConstants.CHK_MIN_BALANCE);
        checkingAccount.setPenalty(ApplicationConstants.CHK_PENALTY);
        checkingAccount.setTransactionCharge(ApplicationConstants.CHK_CHARGE);
        checkingAccount.setBalance(CHK_MOCK_BALANCE);
        return checkingAccount;
    }

    /** Regular Account
     *  No interest
     *  Minimum/starting balance 500.00
     *  Penalty of 10.00 if balance falls below minimum
     */
    public static RegularAccount getMockRegularAccount() {
        RegularAccount regularAccount = new RegularAccount();
        regularAccount.setId(REG_MOCK_ACCT_ID);
        regularAccount.setName(MOCK_NAME);
        regularAccount.setAcctNumber(REG_MOCK_ACCT_NO);
        regularAccount.setMinimumBalance(ApplicationConstants.REG_MIN_BALANCE);
        regularAccount.setPenalty(ApplicationConstants.REG_PENALTY);
        regularAccount.setBalance(REG_MOCK_BALANCE);
        regularAccount.setInterestCharge(REG_MOCK_INTEREST);
        return regularAccount;
    }

    /** Interest Account
     *  Interest of 3% paid monthly
     *  No minimum balance
     */
    public static InterestAccount getMockInterestAccount() {
        InterestAccount interestAccount = new InterestAccount();
        interestAccount.setName(MOCK_NAME);
        interestAccount.setId(INT_MOCK_ACCT_ID);
        interestAccount.setAcctNumber(INT_MOCK_ACCT_NO);
        interestAccount.setInterestCharge(ApplicationConstants.INT_INTEREST);
        interestAccount.setBalance(INT_MOCK_BALANCE);
        return interestAccount;
    }
}
