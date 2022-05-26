package com.advancejava.groupexercise1.repository;

import com.advancejava.groupexercise1.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Integer> {
}
