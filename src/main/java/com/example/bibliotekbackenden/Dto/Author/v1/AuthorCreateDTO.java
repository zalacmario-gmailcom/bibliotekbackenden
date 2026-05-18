package com.example.bibliotekbackenden.Dto.Author.v1;

import jakarta.validation.constraints.NotEmpty;

public record AuthorCreateDTO(
        @NotEmpty(message = "Name is required") String name,
        @Deprecated Integer bookCount) {
}
