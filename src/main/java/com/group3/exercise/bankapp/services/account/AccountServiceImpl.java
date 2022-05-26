package com.group3.exercise.bankapp.services.account;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.group3.exercise.bankapp.adapters.AccountAdapter;
import com.group3.exercise.bankapp.entities.Account;
import com.group3.exercise.bankapp.exceptions.BankAppException;
import com.group3.exercise.bankapp.exceptions.BankAppExceptionCode;
import com.group3.exercise.bankapp.repository.AccountRepository;
import com.group3.exercise.bankapp.request.CreateAccountRequest;
import com.group3.exercise.bankapp.request.TransactionRequest;
import com.group3.exercise.bankapp.response.AccountResponse;
import com.group3.exercise.bankapp.services.transaction.TransactionStrategyNavigator;

@Service
public class AccountServiceImpl implements AccountService {

    private final TransactionStrategyNavigator transactionStrategyNavigator;
    private final AccountAdapter accountAdapter;
    private final AccountRepository accountRepository;
    public AccountServiceImpl(
            TransactionStrategyNavigator transactionStrategyNavigator,
            AccountAdapter accountAdapter,
            AccountRepository repository){
        this.accountRepository = repository;
        this.accountAdapter = accountAdapter;
        this.transactionStrategyNavigator = transactionStrategyNavigator;
    }

    @Override
    public AccountResponse register(CreateAccountRequest request) {
        return Optional.ofNullable(request)
                .map(req -> this.transactionStrategyNavigator.generateNewAccountDetails(req.getName(),generateAcctNbr() ,req.getType()))
                .map(this.accountRepository::save)
                .map(accountAdapter::mapToResponse)
                .orElseThrow(() -> new BankAppException(BankAppExceptionCode.INTERNAL_SERVER_ERROR));
    }

    @Override
    public AccountResponse withdraw(Long id, TransactionRequest request) {
        if(!isValidAmount(request.getAmount())){
            throw new BankAppException(BankAppExceptionCode.INVALID_AMOUNT_EXCEPTION);
        }
        Optional<Account> found = accountRepository.findById(id);
        if(!found.isPresent()){
            throw new BankAppException(BankAppExceptionCode.ACCOUNT_NOT_FOUND_EXCEPTION);
        }
        return Optional.of(found)
                .map(Optional::get)
                .map(a -> this.transactionStrategyNavigator.withdraw(a, request.getAmount()))
                .map(accountRepository::save)
                .map(accountAdapter::mapToResponse)
                .orElseThrow(() -> new BankAppException(BankAppExceptionCode.SERVER_TRANSACTION_EXCEPTION));
    }

    @Override
    public AccountResponse deposit(Long id, TransactionRequest request) {
        if(!isValidAmount(request.getAmount())){
            throw new BankAppException(BankAppExceptionCode.INVALID_AMOUNT_EXCEPTION);
        }
        Optional<Account> found = accountRepository.findById(id);
        if(!found.isPresent()){
            throw new BankAppException(BankAppExceptionCode.ACCOUNT_NOT_FOUND_EXCEPTION);
        }
        return Optional.of(found)
                .map(Optional::get)
                .map(a -> this.transactionStrategyNavigator.deposit(a, request.getAmount()))
                .map(accountRepository::save)
                .map(accountAdapter::mapToResponse)
                .orElseThrow(() -> new BankAppException(BankAppExceptionCode.SERVER_TRANSACTION_EXCEPTION));
    }
    private String generateAcctNbr(){
        return String.valueOf(new Random().nextInt(99999999));
    }
    private boolean isValidAmount(Double amount){
        return amount > 0.0;
    }
	@Override
	public List<AccountResponse> getAllAccounts() {
		return accountRepository.findAll().stream().map(accountAdapter::mapToResponse).collect(Collectors.toList());
	}

	@Override
	public AccountResponse getAccountById(Long id) {
		return Optional.of(id).map(accountRepository::findById).map(Optional::get).map(accountAdapter::mapToResponse)
				.orElseThrow(() -> new BankAppException(BankAppExceptionCode.ACCOUNT_NOT_FOUND_EXCEPTION));
	}

	@Override
	public Account deleteAccountById(Long id) {
		Optional<Account> account = accountRepository.findById(id);
		
		if (account.isPresent()) {
			accountRepository.deleteById(id);
			return account.get();
		} else {
			throw new BankAppException(BankAppExceptionCode.ACCOUNT_NOT_FOUND_EXCEPTION);
		}
	}
}
