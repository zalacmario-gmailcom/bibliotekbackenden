package com.example.bibliotekbackenden.Dto.Book.v1;

public record BookCreateDTO(
                String title,
                Long authorId,
                String isbn,
                Integer publishedYear) {
}