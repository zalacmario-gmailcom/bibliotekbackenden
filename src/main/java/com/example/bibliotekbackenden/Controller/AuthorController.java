package com.example.bibliotekbackenden.Controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bibliotekbackenden.Dto.Author.v1.AuthorCreateDTO;
import com.example.bibliotekbackenden.Dto.Author.v1.AuthorResponseDTO;
import com.example.bibliotekbackenden.Entity.Author;
import com.example.bibliotekbackenden.Entity.Book;
import com.example.bibliotekbackenden.Service.AuthorService;

@RestController
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping
    public AuthorResponseDTO createAuthor(@RequestBody AuthorCreateDTO dto) {
        Author author = authorService.createAuthor(dto.name());

        return new AuthorResponseDTO(
                author.getId(),
                author.getName(),
                author.getBooks() != null ? author.getBooks().size() : 0);
    }

    @GetMapping("/{id}")
    public AuthorResponseDTO getAuthor(Long id) {
        Author author = authorService.getAuthorById(id);
        return new AuthorResponseDTO(
                author.getId(),
                author.getName(),
                author.getBooks() != null ? author.getBooks().size() : 0);
    }

    @GetMapping("/{id}/books")
    public List<Book> getBooksByAuthorId(@PathVariable Long id) {
        return authorService.getBooksForAuthor(id).getBooks();
    }

    @GetMapping
    public Iterable<Author> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    @DeleteMapping("/{id}")
    public void deleteAuthor(Long id) {
        authorService.deleteAuthor(id);
    }
}