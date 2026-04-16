package com.example.bibliotekbackenden.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponseDTO createBook(@RequestBody BookCreateDTO dto) {
        Book book = bookService.createBook(dto.title(), dto.authorId(), dto.isbn(), dto.publishedYear());

        return new BookResponseDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor() != null ? book.getAuthor() : "Unknown Author",
                book.getIsbn(),
                book.getPublishedYear());
    }

    // Updated method to create book with new attribute "isAvailable"
    @PostMapping("/v2")
    @ResponseStatus(HttpStatus.CREATED)
    public BookResponseDTOv2 createBookV2(@RequestBody BookCreateDTOv2 dto) {
        try {
            Book book2 = bookService.createBookV2(dto.title(), dto.authorId(), dto.isbn(), dto.publishedYear(),
                    dto.isAvailable());

            return new BookResponseDTOv2(
                    book2.getId(),
                    book2.getTitle(),
                    book2.getAuthor() != null ? book2.getAuthor() : "Unknown Author",
                    book2.getIsbn(),
                    book2.getPublishedYear(),
                    book2.isAvailable(),
                    "v2");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Author not found");
        }
    }

    @GetMapping("/{id}")
    public BookResponseDTO getBookById(@PathVariable("id") Long id) {
        try {
            Book book = bookService.getBookById(id);

            return new BookResponseDTO(
                    book.getId(),
                    book.getTitle(),
                    book.getAuthor() != null ? book.getAuthor() : "Unknown Author",
                    book.getIsbn(),
                    book.getPublishedYear());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        }
    }

    @GetMapping
    public Iterable<Book> getAllBooks() {
        try {
            return bookService.getAllBooks();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Books not found");
        }
    }

    @PutMapping("/{id}")
    public Book updateBook(@PathVariable("id") Long id, @RequestBody BookResponseDTOv2 dto) {
        try {
            return bookService.updateBook(id, dto.title(), dto.author() != null ? dto.author() : "Unknown Author",
                    dto.isbn(), dto.publishedYear(),
                    dto.isAvailable());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        }

    }

    @DeleteMapping("/{id}")
    public Book deleteBook(@PathVariable("id") Long id) {
        try {
            return bookService.deleteBook(id);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        }
    }
}
