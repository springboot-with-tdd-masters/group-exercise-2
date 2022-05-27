package com.softvision.bank.tdd;

import com.softvision.bank.tdd.controllers.web.LoginForm;
import com.softvision.bank.tdd.controllers.web.SignupForm;
import com.softvision.bank.tdd.model.User;

public class UserMocks {
    public static final String MOCK_USER1_USERNAME = "mhatch";
    public static final String MOCK_USER1_PASSWORD = "hello123";
    public static final String MOCK_USER1_ROLE = "admin";
    public static final String MOCK_USER2_USERNAME = "sdiamon";
    public static final String MOCK_USER2_PASSWORD = "welcome123";
    public static final String MOCK_USER2_ROLE = "user";

    public static User getMockUser1() {
        return new User(MOCK_USER1_USERNAME, MOCK_USER1_PASSWORD, MOCK_USER1_ROLE);
    }

    public static User getMockUser2() {
        return new User(MOCK_USER2_USERNAME, MOCK_USER2_PASSWORD, MOCK_USER2_ROLE);
    }

    public static SignupForm getSignupForm1() {
        return new SignupForm(MOCK_USER1_USERNAME, MOCK_USER1_PASSWORD, MOCK_USER1_ROLE);
    }

    public static SignupForm getSignupForm2() {
        return new SignupForm(MOCK_USER2_USERNAME, MOCK_USER2_PASSWORD, MOCK_USER2_ROLE);
    }

    public static LoginForm getLoginForm1() {
        return new LoginForm(MOCK_USER1_USERNAME, MOCK_USER1_PASSWORD);
    }

    public static LoginForm getLoginForm2() {
        return new LoginForm(MOCK_USER2_USERNAME, MOCK_USER2_PASSWORD);
    }
}
