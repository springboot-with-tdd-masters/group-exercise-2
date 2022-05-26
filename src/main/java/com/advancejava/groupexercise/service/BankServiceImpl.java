package com.advancejava.groupexercise.service;

import com.advancejava.groupexercise.entity.Account;
import com.advancejava.groupexercise.entity.AccountTransactions;
import com.advancejava.groupexercise.helper.CheckAccountType;
import com.advancejava.groupexercise.helper.CheckBalance;
import com.advancejava.groupexercise.helper.CustomResponse;
import com.advancejava.groupexercise.model.dto.DTORequest;
import com.advancejava.groupexercise.repository.AccountRepository;
import com.advancejava.groupexercise.repository.AccountTxnRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankServiceImpl extends CustomResponse implements BankService {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CheckBalance checkBalance;

    @Autowired
    CheckAccountType checkAccountType;

    @Autowired
    AccountTxnRepository accountTxnRepository;

    public Account getAccount(Integer id){
        if(accountRepository.findById(id).isPresent()){
            return accountRepository.findById(id).get();
        }else {
            throw NotFound("entity not found");
        }
    }

    public List<Account> getAccounts(){
        return accountRepository.findAll();
    }

    @Override
    public Account createAccount(Account acct) {
        return accountRepository.save(acct);
    }

    @Override
    public Account updateAccount(DTORequest dep, Integer id) {
        Account acct;
        //get Account data by Id
        if (accountRepository.findById(id).isEmpty()){
            throw NotFound("entity not found");
        }
        acct = accountRepository.findById(id).get();

        //validate amount
        double amount = dep.getAmount();
        while (amount < 0 ) {
            throw badRequest("invalid amount...");
        }
        String type = dep.getType();
        switch(type){
            case "deposit":
                acct.setBalance(acct.getBalance() + amount);
                acct.setId(id);
                break;
            case "withdraw":
                acct.setBalance(acct.getBalance() - amount);
                acct.setId(id);
                break;
            default:
                throw badRequest( "no such entry...");
        }
        //check regular, checking
        //TODO: check interest
        checkAccountType.checkAccountType(acct);

        //check deductible if below minimum for regular
        acct.setBalance(checkBalance.isBelowMinimumBalance(acct));
        return accountRepository.save(acct);

    }

    @Override
    public void deleteAccount(Integer id) {
        getAccount(id);
        accountRepository.deleteById(id);
    }

    @Override
    public AccountTransactions updateTxn(Account account, DTORequest dep, Integer id) {

        Account acct;
        //get Account data by Id
        if (accountRepository.findById(id).isEmpty()){
            throw NotFound("entity not found");
        }
        acct = accountRepository.findById(id).get();

        //validate amount
        double amount = dep.getAmount();
        while (amount < 0 ) {
            throw badRequest("invalid amount...");
        }
        String type = dep.getType();
        switch(type){
            case "deposit":
                acct.setBalance(acct.getBalance() + amount);
                acct.setId(id);
                break;
            case "withdraw":
                acct.setBalance(acct.getBalance() - amount);
                acct.setId(id);
                break;
            default:
                throw badRequest( "no such entry...");
        }
        //check regular, checking
        //TODO: check interest
        checkAccountType.checkAccountType(acct);

        //check deductible if below minimum for regular
        acct.setBalance(checkBalance.isBelowMinimumBalance(acct));




        AccountTransactions accountTransactions = new AccountTransactions();
        accountTransactions.setAccount(acct);
        return accountTxnRepository.save(accountTransactions);

    }

    @Override
    public AccountTransactions getTxn(Long id) {
        return accountTxnRepository.findById(id).get();
    }


}
