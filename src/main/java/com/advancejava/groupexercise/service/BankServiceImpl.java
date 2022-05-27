package com.advancejava.groupexercise.service;

import com.advancejava.groupexercise.entity.Account;
import com.advancejava.groupexercise.entity.BankTransaction;
import com.advancejava.groupexercise.helper.FilterAccount;
import com.advancejava.groupexercise.helper.CustomResponse;
import com.advancejava.groupexercise.model.dto.AccountRequest;
import com.advancejava.groupexercise.model.dto.DTORequest;
import com.advancejava.groupexercise.repository.AccountRepository;
import com.advancejava.groupexercise.repository.AccountTxnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankServiceImpl extends CustomResponse implements BankService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    FilterAccount filterAccount;

    @Autowired
    AccountTxnRepository accountTxnRepository;

    public Account getAccount(Integer id){
        if(accountRepository.findById(id).isPresent()){
            return accountRepository.findById(id).get();
        }else {
            throw NotFound("entity not found");
        }
    }

    public Page<Account> getAccountsWithPaginationAndSort(int offset, int pageSize, String field, String order){
        Sort sOrder = order.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(field).ascending() : Sort.by(field).descending();
        return  accountRepository.findAll(PageRequest.of(offset, pageSize, sOrder));
    }

    @Override
    public Page<BankTransaction> getBankTxnsWithPaginationAndSort(int page, int limit, String field, String order) {
        Sort sOrder = order.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(field).ascending() : Sort.by(field).descending();
        return accountTxnRepository.findAll(PageRequest.of(page, limit, sOrder));
    }

    public List<Account> getAccounts(){
        return accountRepository.findAll();
    }

    @Override
    public Account createAccount(Account acct) {
        return accountRepository.save(acct);
    }

    @Override
    public Account updateAccount(AccountRequest request, Integer id) {
        Account account;
        //get Account data by Id
        if (accountRepository.findById(id).isEmpty()){
            throw NotFound("entity not found");
        }
        account = accountRepository.findById(id).get();
        account.setName(request.getName());
        account.setType(request.getType());
        return accountRepository.save(account);
    }

    @Override
    public void deleteAccount(Integer id) {
        getAccount(id);
        accountRepository.deleteById(id);
    }

    @Override
    public BankTransaction updateTransaction(DTORequest request, Integer id) {

        Account account;
        //get Account data by Id
        if (accountRepository.findById(id).isEmpty()){
            throw NotFound("entity not found");
        }
        account = accountRepository.findById(id).get();

        //validate amount
        double amount = request.getAmount();
        while (amount < 0 ) {
            throw badRequest("invalid amount...");
        }
        //transactionType
        String transactionType = request.getType();
        switch(transactionType){
            case "deposit":
                account.setBalance(account.getBalance() + amount);
                account.setId(id);
                break;
            case "withdraw":
                account.setBalance(account.getBalance() - amount);
                account.setId(id);
                break;
            default:
                throw badRequest( "no such entry...");
        }
        //check regular, checking
        //TODO: check interest
        filterAccount.checkAccountType(account);

        //check deductible if below minimum for regular
        account.setBalance(filterAccount.isBelowMinimumBalance(account));

        BankTransaction accountTransactions = new BankTransaction();
        accountTransactions.setAccount(account);
        return accountTxnRepository.save(accountTransactions);

    }

    @Override
    public BankTransaction getTransaction(Long id) {
        return accountTxnRepository.findById(id).get();
    }


}
