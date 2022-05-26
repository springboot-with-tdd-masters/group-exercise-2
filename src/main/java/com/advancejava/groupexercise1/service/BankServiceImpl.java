package com.advancejava.groupexercise1.service;

import com.advancejava.groupexercise1.entity.Account;
import com.advancejava.groupexercise1.helper.CheckAccountType;
import com.advancejava.groupexercise1.helper.CheckBalance;
import com.advancejava.groupexercise1.repository.AccountRepository;
import com.advancejava.groupexercise1.model.Deposit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class BankServiceImpl implements BankService{

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CheckBalance checkBalance;

    @Autowired
    CheckAccountType checkAccountType;

    public Account getAccount(Integer id){
        if(accountRepository.findById(id).isPresent()){
            return accountRepository.findById(id).get();
        }else {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found"
            );
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
    public Account updateAccount(Deposit dep, Integer id) {
        Account acct;
        //get Account data by Id
        if (accountRepository.findById(id).isEmpty()){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "entity not found");
        }
        acct = accountRepository.findById(id).get();

        //validate amount
        double amount = dep.getAmount();
        while (amount < 0 ) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "invalid amount entry...");
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
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "no such entry...");
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


}
