package com.group3.exercise.bankapp.repository;

import com.group3.exercise.bankapp.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *  TODO delete after JpaRepository implementation
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Override
    Account save(Account e);
}
