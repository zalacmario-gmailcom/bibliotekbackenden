package com.example.bibliotekbackenden.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.bibliotekbackenden.Entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
    }
