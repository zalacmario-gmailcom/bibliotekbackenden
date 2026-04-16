package com.example.bibliotekbackenden.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.bibliotekbackenden.Entity.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}
