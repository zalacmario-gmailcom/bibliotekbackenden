package com.example.bibliotekbackenden.Controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.bibliotekbackenden.Dto.BookCreateDTO;
import com.example.bibliotekbackenden.Dto.BookResponseDTO;
import com.example.bibliotekbackenden.Entity.Book;
import com.example.bibliotekbackenden.Service.BookService;


@RestController
@RequestMapping("/book")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public BookResponseDTO createBook(@RequestBody BookCreateDTO dto) {
        Book book = bookService.createBook(dto.title(), dto.author(), dto.isbn(), dto.publishedYear());

        return new BookResponseDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getPublishedYear());
    }

    @GetMapping("/{id}")
    public BookResponseDTO getBookById(Long id) {
        Book book = bookService.getBookById(id);

        return new BookResponseDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getPublishedYear());
    }

    @PostMapping("/{id}")
    public Book updateBook(Long id, @RequestBody BookCreateDTO dto) {
        return bookService.updateBook(id, dto.title(), dto.author(), dto.isbn(), dto.publishedYear());
    }

    @DeleteMapping("/{id}")
    public Book deleteBook(Long id) {
        return bookService.deleteBook(id);
    }

}
