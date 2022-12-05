package me.subhas.contactinfo.web.error;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import me.subhas.contactinfo.business.exception.DuplicateContactNameException;

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
}
