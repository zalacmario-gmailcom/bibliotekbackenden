package com.example.bibliotekbackenden.Dto.Book.v2;

public record BookResponseDTOv2(
        Long id,
        String title,
        String author,
        String isbn,
        Integer publishedYear,
        boolean isAvailable,
        String version) {
}
