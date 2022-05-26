package com.advancejava.groupexercise.helper;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public abstract class CustomResponse {

    public static ResponseStatusException badRequest(String message){
        return new ResponseStatusException(HttpStatus.BAD_REQUEST,message);
    }

    public static ResponseStatusException NotFound(String message){
        return new ResponseStatusException(HttpStatus.NOT_FOUND,message);
    }

}
