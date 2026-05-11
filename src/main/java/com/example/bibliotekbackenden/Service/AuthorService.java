package com.example.bibliotekbackenden.Service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.example.bibliotekbackenden.Entity.Author;
import com.example.bibliotekbackenden.Exception.AuthorNotFoundException;
import com.example.bibliotekbackenden.Repository.AuthorRepository;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    /**
     * CRUD methods which will be used in the controller and repository
     */
    public Author createAuthor(String name) {
        try {
            Author author = new Author();
            author.setName(name);

            return authorRepository.save(author);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed creating author");
        }
    }

    public Author getAuthorById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException(id));
    }

    // Get author with their books (relationship handles loading via @OneToMany)
    public Author getBooksForAuthor(Long id) {
        try {
            Author author = getAuthorById(id);
            // Accessing books to ensure they are loaded (if using lazy loading)
            author.getBooks().size();
            return author;
        } catch (ResponseStatusException e) {
            throw new AuthorNotFoundException(id);
        }
    }

    public Iterable<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public Author updateAuthor(Long id, String name) {
        try {
            Author author = getAuthorById(id);
            author.setName(name);
            return authorRepository.save(author);
        } catch (ResponseStatusException e) {
            throw new AuthorNotFoundException(id);
        }
    }

    public void deleteAuthor(Long id) {
        try {
            Author author = getAuthorById(id);
            authorRepository.delete(author);
        } catch (ResponseStatusException e) {
            throw new AuthorNotFoundException(id);
        }
    }
}
