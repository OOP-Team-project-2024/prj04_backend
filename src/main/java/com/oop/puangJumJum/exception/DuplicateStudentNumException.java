package com.oop.puangJumJum.exception;

public class DuplicateStudentNumException extends RuntimeException {
    public DuplicateStudentNumException(String studentNumber) {
        super("Student number already exists: " + studentNumber);
    }
}