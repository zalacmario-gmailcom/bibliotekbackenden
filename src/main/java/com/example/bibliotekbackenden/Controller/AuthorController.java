package com.example.bibliotekbackenden.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.bibliotekbackenden.Dto.Author.v1.AuthorCreateDTO;
import com.example.bibliotekbackenden.Dto.Author.v1.AuthorResponseDTO;
import com.example.bibliotekbackenden.Entity.Author;
import com.example.bibliotekbackenden.Entity.Book;
import com.example.bibliotekbackenden.Service.AuthorService;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping("api/v1/authors")
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AuthorResponseDTO createAuthor(@RequestBody AuthorCreateDTO dto) {
        Author author = authorService.createAuthor(dto.name());

        return new AuthorResponseDTO(
                author.getId(),
                author.getName(),
                author.getBooks() != null ? author.getBooks().size() : 0);
    }

    @PutMapping("/{id}")
    public Author updateAuthor(@PathVariable("id") Long id, @RequestBody AuthorCreateDTO dto) {
        try {
            return authorService.updateAuthor(id, dto.name());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found");
        }
    }

    @GetMapping("/{id}")
    public AuthorResponseDTO getAuthor(@PathVariable("id") Long id) {
        try {
            Author author = authorService.getAuthorById(id);
            return new AuthorResponseDTO(
                    author.getId(),
                    author.getName(),
                    author.getBooks() != null ? author.getBooks().size() : 0);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found");
        }
    }

    @GetMapping("/{id}/books")
    public List<Book> getBooksByAuthorId(@PathVariable("id") Long id) {
        try {
            return authorService.getBooksForAuthor(id).getBooks();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found");
        }
    }

    @GetMapping
    public Iterable<Author> getAllAuthors() {
        try {
            return authorService.getAllAuthors();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No authors found");
        }
    }

    @DeleteMapping("/{id}")
    public void deleteAuthor(@PathVariable("id") Long id) {
        try {
            authorService.deleteAuthor(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found");
        }
    }
}