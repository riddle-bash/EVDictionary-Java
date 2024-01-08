package com.example.dictionary.handleException;

public class NotFoundWordException extends Exception{
    public NotFoundWordException(String str) {
        super(str);
    }
}
