package com.example.bibliotekbackenden.Exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.HttpStatus;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthorNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleAuthorNotFound(AuthorNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleBookNotFound(BookNotFoundException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(BookNotAvailableException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleBookNotAvailable(BookNotAvailableException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(LoanNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleLoanNotFound(LoanNotFoundException ex) {
        return ex.getMessage();
    }
}
