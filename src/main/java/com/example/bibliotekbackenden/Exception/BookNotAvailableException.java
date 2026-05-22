package com.example.bibliotekbackenden.Exception;

public class BookNotAvailableException extends RuntimeException {
    public BookNotAvailableException(Long id) {
        super("Book with id " + id + " is not available");
    }
}
