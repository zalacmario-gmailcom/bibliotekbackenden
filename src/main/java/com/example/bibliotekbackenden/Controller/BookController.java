package com.example.bibliotekbackenden.Controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.bibliotekbackenden.Dto.Book.v1.BookCreateDTO;
import com.example.bibliotekbackenden.Dto.Book.v1.BookResponseDTO;
import com.example.bibliotekbackenden.Dto.Book.v2.BookCreateDTOv2;
import com.example.bibliotekbackenden.Dto.Book.v2.BookResponseDTOv2;
import com.example.bibliotekbackenden.Entity.Book;
import com.example.bibliotekbackenden.Service.BookService;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    /**
     * CRUD operations for Book entity.
     */
    @PostMapping
    public BookResponseDTO createBook(@RequestBody BookCreateDTO dto) {
        Book book = bookService.createBook(dto.title(), dto.authorId(), dto.isbn(), dto.publishedYear());

        return new BookResponseDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),
                book.getPublishedYear());
    }

    // Updated method to create book with new attribute "isAvailable"
    @PostMapping("/v2")
    public BookResponseDTOv2 createBookV2(@RequestBody BookCreateDTOv2 dto) {
        Book book2 = bookService.createBookV2(dto.title(), dto.authorId(), dto.isbn(), dto.publishedYear(),
                dto.isAvailable());

        return new BookResponseDTOv2(
                book2.getId(),
                book2.getTitle(),
                book2.getAuthor(),
                book2.getIsbn(),
                book2.getPublishedYear(),
                book2.isAvailable(),
                "v2");
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

    @GetMapping
    public Iterable<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @PostMapping("/{id}")
    public Book updateBook(Long id, @RequestBody BookResponseDTOv2 dto) {
        return bookService.updateBook(id, dto.title(), dto.author(), dto.isbn(), dto.publishedYear(),
                dto.isAvailable());
    }

    @DeleteMapping("/{id}")
    public Book deleteBook(Long id) {
        return bookService.deleteBook(id);
    }
}
