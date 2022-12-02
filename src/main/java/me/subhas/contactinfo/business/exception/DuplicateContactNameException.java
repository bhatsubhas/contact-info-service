package me.subhas.contactinfo.business.exception;

public class DuplicateContactNameException extends RuntimeException {

    public DuplicateContactNameException(String message) {
        super(message);
    }

}
