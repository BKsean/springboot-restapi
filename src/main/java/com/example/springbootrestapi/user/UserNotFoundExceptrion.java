package com.example.springbootrestapi.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundExceptrion extends RuntimeException {
    public UserNotFoundExceptrion(String message) {
        super(message);
    }
}
