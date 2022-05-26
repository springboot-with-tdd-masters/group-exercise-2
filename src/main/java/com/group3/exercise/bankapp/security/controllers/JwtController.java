package com.group3.exercise.bankapp.security.controllers;

import com.group3.exercise.bankapp.exceptions.BankAppException;
import com.group3.exercise.bankapp.exceptions.BankAppExceptionCode;
import com.group3.exercise.bankapp.security.entities.UserWrapper;
import com.group3.exercise.bankapp.security.jwt.JwtTokenUtil;
import com.group3.exercise.bankapp.security.requests.JwtTokenRequest;
import com.group3.exercise.bankapp.security.requests.RegisterUserReqDto;
import com.group3.exercise.bankapp.security.responses.JwtTokenResponse;
import com.group3.exercise.bankapp.security.responses.RegisterUserResDto;
import com.group3.exercise.bankapp.security.services.MyUserDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(path = "/jwt")
public class JwtController {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final MyUserDetailsService userDetailsService; // implementation used for custom methods

    public JwtController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, MyUserDetailsService userDetailsService){
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
    }
    @PostMapping("/authenticate")
    public ResponseEntity<JwtTokenResponse> createAuthenticationToken(@RequestBody JwtTokenRequest request) throws Exception {
        authenticate(request.getUsername(), request.getPassword());
        final UserWrapper user = (UserWrapper) userDetailsService.loadUserByUsername(request.getUsername());
        final String token = jwtTokenUtil.generateToken(user);
        return ResponseEntity.ok(new JwtTokenResponse(token));
    }

    @PostMapping("/register")
    public RegisterUserResDto registerUser(@RequestBody RegisterUserReqDto user) {
        return this.userDetailsService.registerUser(user);
    }

    private void authenticate(String userName, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
        } catch (DisabledException e) {
            throw new BankAppException(BankAppExceptionCode.JWT_INVALID_EXCEPTION);
        } catch (BadCredentialsException e) {
            throw new BankAppException(BankAppExceptionCode.CREDENTIALS_INVALID_EXCEPTION);
        }
    }

}
