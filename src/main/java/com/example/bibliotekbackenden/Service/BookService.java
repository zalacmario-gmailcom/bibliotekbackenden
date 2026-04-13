package com.example.bibliotekbackenden.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import com.example.bibliotekbackenden.Entity.Book;
import com.example.bibliotekbackenden.Repository.BookRepository;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * @return
     *         four functions of service CRUD which will be used in
     *         controller and repository
     */
    public Book createBook(String title, String author, String isbn, Integer publishedYear) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setIsbn(isbn);
        book.setPublishedYear(publishedYear);

        return bookRepository.save(book);
    }

    //Method with updated attributes
    public Book createBookV2(String title, String author, String isbn, Integer publishedYear, boolean isAvailable) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setIsbn(isbn);
        book.setPublishedYear(publishedYear);
        book.setAvailable(isAvailable);

        return bookRepository.save(book);
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));
    }

    public Iterable<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book updateBook(Long id, String title, String author, String isbn, Integer publishedYear, boolean isAvailable) {
        try {
            Book book = getBookById(id);
            book.setTitle(title);
            book.setAuthor(author);
            book.setIsbn(isbn);
            book.setPublishedYear(publishedYear);
            book.setAvailable(isAvailable);

            return bookRepository.save(book);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        }
    }

    public Book deleteBook(Long id) {
        try {
            Book book = getBookById(id);

            bookRepository.deleteById(id);

            return book;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found");
        }
    }
}
