package me.subhas.contactinfo.business.exception;

public class DuplicateContactNameException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DuplicateContactNameException(String message) {
        super(message);
    }

}
