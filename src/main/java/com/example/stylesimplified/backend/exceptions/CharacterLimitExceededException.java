package com.example.stylesimplified.backend.exceptions;

// backend logic to verify that database entries will never be too long
public class CharacterLimitExceededException extends RuntimeException{
    public CharacterLimitExceededException(String m) {
        super(m);
    }
}
