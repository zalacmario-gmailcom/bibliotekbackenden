package com.example.bibliotekbackenden;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.bibliotekbackenden.Entity.Author;
import com.example.bibliotekbackenden.Entity.Book;
import com.example.bibliotekbackenden.Repository.AuthorRepository;
import com.example.bibliotekbackenden.Repository.BookRepository;

@Component
public class DataSeeder implements CommandLineRunner {
    AuthorRepository authorRepository;
    BookRepository bookRepository;

    public DataSeeder(AuthorRepository authorRepository, BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    // You can add methods here to seed initial data into the database
    public void seedData() {
        if (authorRepository.findAll().size() > 0) {
            return;
        }

        if (bookRepository.findAll().size() > 0) {
            return;
        }

        for (int i = 0; i < 100; i++) {
            Author author = new Author();
            author.setName("Author " + i);
            authorRepository.save(author);

            Book book = new Book();
            book.setTitle("Book " + i);
            book.setAuthorBook(author);
            book.setIsbn("ISBN-" + i);
            book.setPublishedYear(2000);
            bookRepository.save(book);
        }
    }

    // Implement CommandLineRunner to run the seedData method at startup
    @Override
    public void run(String... args) throws Exception {
        seedData();
    }
}
