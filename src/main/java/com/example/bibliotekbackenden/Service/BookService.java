package com.example.bibliotekbackenden.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import com.example.bibliotekbackenden.Entity.Author;
import com.example.bibliotekbackenden.Entity.Book;
import com.example.bibliotekbackenden.Exception.BookNotFoundException;
import com.example.bibliotekbackenden.Repository.AuthorRepository;
import com.example.bibliotekbackenden.Repository.BookRepository;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;

    /**
     * CRUD methods which will be used in the controller and repository
     */
    @CacheEvict(value = "books", allEntries = true)
    public Book createBook(String title, Long authorId, String isbn, Integer publishedYear) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Failed creating, could not find author with id: " + authorId));
        Book book = new Book();

        book.setTitle(title);
        book.setAuthor(author.getName());
        book.setIsbn(isbn);
        book.setPublishedYear(publishedYear);
        book.setAuthorBook(author);

        return bookRepository.save(book);
    }

    // V2 Method with updated attributes
    @CacheEvict(value = "books", allEntries = true)
    public Book createBookV2(String title, Long authorId, String isbn, Integer publishedYear, boolean isAvailable) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Failed creating, could not find author with id: " + authorId));

        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author.getName());
        book.setIsbn(isbn);
        book.setPublishedYear(publishedYear);
        book.setAvailable(isAvailable);
        book.setAuthorBook(author);

        return bookRepository.save(book);
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
    }

    @Cacheable("books")
    public Iterable<Book> getAllBooks() {
        if (bookRepository.findAll().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No books found");
        } else {
            return bookRepository.findAll();
        }
    }

    @CacheEvict(value = "books", allEntries = true)
    public Book updateBook(Long id, String title, String author, String isbn, Integer publishedYear,
            boolean isAvailable) {
        try {
            Book book = getBookById(id);
            book.setTitle(title);
            book.setAuthor(author);
            book.setIsbn(isbn);
            book.setPublishedYear(publishedYear);
            book.setAvailable(isAvailable);

            return bookRepository.save(book);
        } catch (Exception e) {
            throw new BookNotFoundException(id);
        }
    }

    @CacheEvict(value = "books", allEntries = true)
    public Book deleteBook(Long id) {
        try {
            Book book = getBookById(id);

            bookRepository.deleteById(id);

            return book;
        } catch (Exception e) {
            throw new BookNotFoundException(id);
        }
    }
}
