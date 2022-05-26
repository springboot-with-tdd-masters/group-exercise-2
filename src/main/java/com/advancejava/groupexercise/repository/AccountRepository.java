package com.advancejava.groupexercise.repository;

import com.advancejava.groupexercise.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
}
