package com.erdi.todoapp.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String username){
        super(username + " does not exist!");
    }
}
