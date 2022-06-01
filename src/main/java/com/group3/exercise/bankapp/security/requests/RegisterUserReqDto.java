package com.group3.exercise.bankapp.security.requests;

import lombok.Data;

@Data
public class RegisterUserReqDto {
    private String username;
    private String password;
}
