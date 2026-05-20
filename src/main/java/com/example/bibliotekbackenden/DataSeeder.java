package com.example.bibliotekbackenden;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.vault.core.VaultKeyValueOperations;
import org.springframework.vault.core.VaultKeyValueOperationsSupport;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.support.VaultResponse;

import com.example.bibliotekbackenden.Entity.Author;
import com.example.bibliotekbackenden.Entity.Book;
import com.example.bibliotekbackenden.Repository.AuthorRepository;
import com.example.bibliotekbackenden.Repository.BookRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookRepository bookRepository;

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

    @Autowired
    private VaultTemplate vaultTemplate;

    private void seedUserToVault() {
        VaultKeyValueOperations keyValueOperations = vaultTemplate.opsForKeyValue("secret",
                VaultKeyValueOperationsSupport.KeyValueBackend.KV_2);

        // Check if jwt-secret already exists
        try {
            VaultResponse existing = keyValueOperations.get("jwt-secret");
            if (existing != null && existing.getRequiredData().get("value") != null) {
                System.out.println("JWT Secret already exists in Vault, skipping seed");
                return; // Don't re-seed
            }
        } catch (Exception e) {
            // Secret doesn't exist yet, proceed to store it
        }

        // Store the JWT secret in Vault
        keyValueOperations.put("jwt-secret", Collections.singletonMap("value",
                "624938d7fa8990846531893214ac19adf0782a39e37b50a30b5ead96ac02a0e03d2"));

        VaultResponse read = keyValueOperations.get("jwt-secret");
        System.out.println("JWT Secret stored in Vault: " + read.getRequiredData().get("value"));
    }

    // Implement CommandLineRunner to run the seedData method at startup
    @Override
    public void run(String... args) throws Exception {
        seedData();
        seedUserToVault();
    }
}
