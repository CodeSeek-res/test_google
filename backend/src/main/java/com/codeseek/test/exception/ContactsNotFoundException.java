package com.codeseek.test.exception;

public class ContactsNotFoundException extends RuntimeException {

    public ContactsNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
