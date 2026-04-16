package com.example.bibliotekbackenden.Entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    // Relations
    @OneToMany(mappedBy = "authorBook")
    @JsonManagedReference
    private List<Book> books = new ArrayList<>();

    public Author() {
    }

    public Author(String name) {
        this.name = name;
        this.books = new ArrayList<>(); // Add this line
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Book> getBooks() {
        return books;
    }
}
