package com.masters.masters.exercise.repository;

import com.masters.masters.exercise.model.Account;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends PagingAndSortingRepository<Account,Long> {

	public Optional<Account> findByName(String name);

	public Optional<Account> findByAcctNumber(String acctNumber);
	
}
