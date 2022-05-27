package com.softvision.bank.tdd.services;

import com.softvision.bank.tdd.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User createUser(User user);
}
