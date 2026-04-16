package com.example.bibliotekbackenden.Dto.Book.v2;

public record BookCreateDTOv2(
                String title,
                Long authorId,
                String isbn,
                Integer publishedYear,
                boolean isAvailable) {
}
