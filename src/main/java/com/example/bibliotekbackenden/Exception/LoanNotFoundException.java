package com.example.bibliotekbackenden.Exception;

public class LoanNotFoundException extends RuntimeException {
    public LoanNotFoundException(Long id) {
        super("Loan with id " + id + " not found");
    } 
}
