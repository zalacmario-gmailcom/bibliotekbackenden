package com.example.bibliotekbackenden.Controller;

import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.example.bibliotekbackenden.Dto.Author.v1.AuthorCreateDTO;
import com.example.bibliotekbackenden.Dto.Author.v1.AuthorResponseDTO;
import com.example.bibliotekbackenden.Entity.Author;
import com.example.bibliotekbackenden.Entity.Book;
import com.example.bibliotekbackenden.Service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/authors")
@SecurityRequirement(name = "Bearer")
@Tag(name = "Authors", description = "Endpoints for managing authors")
public class AuthorController {
        @Autowired
        private AuthorService authorService;

        @PostMapping
        @ResponseStatus(HttpStatus.CREATED)
        @Operation(summary = "Create author")
        public AuthorResponseDTO createAuthor(@Valid @RequestBody AuthorCreateDTO dto) {
                Author author = authorService.createAuthor(dto.name());

                return new AuthorResponseDTO(
                                author.getId(),
                                author.getName(),
                                author.getBooks() != null ? author.getBooks().size() : 0);
        }

        @PutMapping("/{id}")
        @Operation(summary = "Update author")
        public Author updateAuthor(@PathVariable("id") Long id, @Valid @RequestBody AuthorCreateDTO dto) {
                return authorService.updateAuthor(id, dto.name());
        }

        @GetMapping("/{id}")
        @Operation(summary = "Get author by id")
        public AuthorResponseDTO getAuthor(@PathVariable("id") Long id) {
                Author author = authorService.getAuthorById(id);
                return new AuthorResponseDTO(
                                author.getId(),
                                author.getName(),
                                author.getBooks() != null ? author.getBooks().size() : 0);
        }

        @GetMapping("/{id}/books")
        @Operation(summary = "Get books by author id")
        public List<Book> getBooksByAuthorId(@PathVariable("id") Long id) {
                return authorService.getBooksForAuthor(id).getBooks();
        }

        @Transactional
        @GetMapping
        @Operation(summary = "Get all authors")
        public Page<Author> getAllAuthors(Pageable pageable) {
                return authorService.getAllAuthors(pageable);
        }

        @DeleteMapping("/{id}")
        @Operation(summary = "Delete author by id")
        public void deleteAuthor(@PathVariable("id") Long id) {
                authorService.deleteAuthor(id);
        }
}