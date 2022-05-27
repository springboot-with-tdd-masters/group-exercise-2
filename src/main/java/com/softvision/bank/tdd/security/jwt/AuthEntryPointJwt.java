package com.softvision.bank.tdd.security.jwt;

import com.softvision.bank.tdd.exceptions.ForbiddenException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthEntryPointJwt implements AuthenticationEntryPoint {
    protected final Log logger = LogFactory.getLog(this.getClass());

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) {
        logger.error(authException);
        throw new ForbiddenException(authException.getMessage());
    }
}
