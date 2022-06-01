package com.softvision.bank.tdd.repository;

import com.softvision.bank.tdd.model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.softvision.bank.tdd.UserMocks.*;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTests {
    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("should get the exact saved user given its username")
    void test_findByUsername() {
        User expected = userRepository.save(getMockUser1());

        assertThat(userRepository.findByUsername(MOCK_USER1_USERNAME))
                .map(User::getId)
                .get().isEqualTo(expected.getId());

        assertThat(userRepository.findByUsername(MOCK_USER2_USERNAME)).isEmpty();
    }
}
