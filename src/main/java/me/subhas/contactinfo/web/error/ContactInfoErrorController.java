package me.subhas.contactinfo.web.error;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import me.subhas.contactinfo.business.exception.ContactNotFoundException;
import me.subhas.contactinfo.business.exception.DuplicateContactNameException;
import me.subhas.contactinfo.web.exception.IllegalIdException;

@RestControllerAdvice
public class ContactInfoErrorController {
    private record ErrorResponse(String errorMessage) {
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> validations = ex.getAllErrors().stream().map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());
        ErrorResponse response = new ErrorResponse(validations.get(0));
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(DuplicateContactNameException.class)
    ResponseEntity<ErrorResponse> handleDuplicateContactNameException(DuplicateContactNameException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(ContactNotFoundException.class)
    ResponseEntity<ErrorResponse> handleContactNotFoundException(ContactNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(IllegalIdException.class)
    ResponseEntity<ErrorResponse> handleIllegalIdException(IllegalIdException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse(ex.getMessage()));
    }
}
