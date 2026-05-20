package com.example.bibliotekbackenden.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.example.bibliotekbackenden.Dto.Book.v1.BookCreateDTO;
import com.example.bibliotekbackenden.Dto.Book.v1.BookResponseDTO;
import com.example.bibliotekbackenden.Dto.Book.v2.BookCreateDTOv2;
import com.example.bibliotekbackenden.Dto.Book.v2.BookResponseDTOv2;
import com.example.bibliotekbackenden.Entity.Book;
import com.example.bibliotekbackenden.Service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/books")
@SecurityRequirement(name = "Bearer")
@Tag(name = "Book", description = "The Book API")
public class BookController {
        @Autowired
        private BookService bookService;

        /**
         * CRUD operations for Book entity.
         */
        @PostMapping("/v1")
        @ResponseStatus(HttpStatus.CREATED)
        @Operation(summary = "Create book")
        public BookResponseDTO createBook(@Valid @RequestBody BookCreateDTO dto) {
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
        @Operation(summary = "Create book v2")
        public BookResponseDTOv2 createBookV2(@Valid @RequestBody BookCreateDTOv2 dto) {
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
        }

        @GetMapping("/{id}")
        @Operation(summary = "Get book by id")
        public BookResponseDTO getBookById(@PathVariable("id") Long id) {

                // TODO: Add versioning to this method to return BookResponseDTOv2
                // if the book version is v2, otherwise return BookResponseDTO
                Book book = bookService.getBookById(id);

                return new BookResponseDTO(
                                book.getId(),
                                book.getTitle(),
                                book.getAuthor() != null ? book.getAuthor() : "Unknown Author",
                                book.getIsbn(),
                                book.getPublishedYear());
        }

        @GetMapping
        @Operation(summary = "Get all books")
        public Iterable<Book> getAllBooks() {
                return bookService.getAllBooks();
        }

        @PutMapping("/{id}")
        @Operation(summary = "Update book")
        public Book updateBook(@PathVariable("id") Long id, @Valid @RequestBody BookResponseDTOv2 dto) {
                return bookService.updateBook(id, dto.title(), dto.author() != null ? dto.author() : "Unknown Author",
                                dto.isbn(), dto.publishedYear(),
                                dto.isAvailable());
        }

        @DeleteMapping("/{id}")
        @Operation(summary = "Delete book by id")
        public Book deleteBook(@PathVariable("id") Long id) {
                return bookService.deleteBook(id);
        }
}
