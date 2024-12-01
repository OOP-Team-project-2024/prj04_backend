package com.oop.puangJumJum.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String studentNum) {
        super("User not found with studentNum: " + studentNum);
    }
}
