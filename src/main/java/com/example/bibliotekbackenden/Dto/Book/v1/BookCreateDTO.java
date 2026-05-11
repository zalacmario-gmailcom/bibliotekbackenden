package com.example.bibliotekbackenden.Dto.Book.v1;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record BookCreateDTO(
                @NotEmpty(message = "Title is required") String title,

                @NotNull(message = "Author ID is required") Long authorId,

                @NotEmpty(message = "ISBN is required") String isbn,

                @NotNull(message = "Published year is required") @Min(value = 0, message = "Published year cannot be negative") Integer publishedYear) {
}