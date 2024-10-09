package com.erdi.todoapp.exception;

public class PasswordMatchException extends RuntimeException{
    public PasswordMatchException(String message){
        super(message);
    }
}
