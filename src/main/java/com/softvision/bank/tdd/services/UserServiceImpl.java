package com.softvision.bank.tdd.services;

import com.softvision.bank.tdd.exceptions.UserExistsException;
import com.softvision.bank.tdd.exceptions.UserNotFoundException;
import com.softvision.bank.tdd.model.User;
import com.softvision.bank.tdd.repository.UserRepository;
import com.softvision.bank.tdd.utils.UserDetailsAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public User createUser(User user) {
        Optional<User> optUser =  userRepository.findByUsername(user.getUsername());

        if (optUser.isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }

        throw new UserExistsException();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(UserNotFoundException::new);
        return new UserDetailsAdapter(user);
    }
}
