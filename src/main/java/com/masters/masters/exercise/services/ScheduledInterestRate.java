/**
 * 
 */
package com.masters.masters.exercise.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.masters.masters.exercise.model.Account;
import com.masters.masters.exercise.model.InterestAccount;
import com.masters.masters.exercise.repository.AccountRepository;

/**
 * @author michaeldelacruz
 *
 */

@Component
@EnableScheduling
public class ScheduledInterestRate {
	
	@Autowired
	private AccountRepository accountRepository;
	
	@Scheduled(cron = "0 0 0 L * ?")
	public void applyInterestRate() {
		List<Account> accounts = (List<Account>) accountRepository.findAll();
		
		accounts.stream().forEach(account -> {
			if(account instanceof InterestAccount) {
				double interest = account.getBalance() * 0.03;
				account.setBalance(account.getBalance() + interest);
				accountRepository.save(account);
			}
		});
	}
}
