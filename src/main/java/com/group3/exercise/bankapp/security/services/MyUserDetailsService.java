package com.group3.exercise.bankapp.security.services;

import com.group3.exercise.bankapp.exceptions.BankAppException;
import com.group3.exercise.bankapp.exceptions.BankAppExceptionCode;
import com.group3.exercise.bankapp.security.entities.User;
import com.group3.exercise.bankapp.security.entities.UserWrapper;
import com.group3.exercise.bankapp.security.repositories.UserRepository;
import com.group3.exercise.bankapp.security.requests.RegisterUserReqDto;
import com.group3.exercise.bankapp.security.responses.RegisterUserResDto;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository repository;
    private final PasswordEncoder bcryptEncoder;

    public MyUserDetailsService(UserRepository repository, PasswordEncoder bcryptEncoder) {
        this.repository = repository;
        this.bcryptEncoder = bcryptEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> found = this.repository.findByUserName(username);
        if (found.isPresent()) {
            User get = found.get();
            return UserWrapper.builder()
                    .userId(get.getUserId())
                    .userName(get.getUserName())
                    .password(get.getPassword())
                    .build();

        }
        throw new UsernameNotFoundException("Not found");
    }

    public RegisterUserResDto registerUser(RegisterUserReqDto userDto) {
        if (!Optional.ofNullable(userDto.getUsername()).isPresent() ||
                !Optional.ofNullable(userDto.getPassword()).isPresent()) {
            throw new BankAppException(BankAppExceptionCode.REGISTER_EXCEPTION);
        }
        if (this.repository.findByUserName(userDto.getUsername()).isPresent()) {
            throw new BankAppException(BankAppExceptionCode.USER_EXISTS_EXCEPTION);
        }
        try {
            User newUser = new User();
            newUser.setUserName(userDto.getUsername());
            String password = bcryptEncoder.encode(userDto.getPassword());
            newUser.setPassword(password);

            User saved = this.repository.save(newUser);
            return RegisterUserResDto.builder().username(saved.getUserName()).userId(saved.getUserId()).build();
        } catch (Exception e) {
            // TODO
            throw new BankAppException(BankAppExceptionCode.INTERNAL_SERVER_ERROR);
        }

    }
}
