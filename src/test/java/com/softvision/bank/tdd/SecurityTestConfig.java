package com.softvision.bank.tdd;

import com.softvision.bank.tdd.utils.UserDetailsAdapter;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static com.softvision.bank.tdd.UserMocks.getMockUser1;
import static com.softvision.bank.tdd.UserMocks.getMockUser2;

@TestConfiguration
public class SecurityTestConfig {

    @Bean
    @Primary
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
                new UserDetailsAdapter(getMockUser1()),
                new UserDetailsAdapter(getMockUser2()));
    }
}
