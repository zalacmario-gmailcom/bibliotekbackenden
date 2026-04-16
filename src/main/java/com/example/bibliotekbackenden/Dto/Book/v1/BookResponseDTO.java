package com.example.bibliotekbackenden.Dto.Book.v1;

public record BookResponseDTO(
                Long id,
                String title,
                String author,
                String isbn,
                Integer publishedYear) {
}