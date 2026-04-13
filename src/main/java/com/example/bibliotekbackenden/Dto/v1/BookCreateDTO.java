package com.example.bibliotekbackenden.Dto.v1;

public record BookCreateDTO(String title, String author, String isbn, Integer publishedYear) {
    }