package com.example.bibliotekbackenden;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import com.example.bibliotekbackenden.Dto.Author.v1.AuthorCreateDTO;
import com.example.bibliotekbackenden.Dto.Author.v1.AuthorResponseDTO;
import com.example.bibliotekbackenden.Entity.Author;
import com.example.bibliotekbackenden.Entity.Book;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class WrongResult {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testUpdateAuthorNotFound() {
        Long nonExistentId = 999L;
        AuthorCreateDTO updatedAuthor = new AuthorCreateDTO("Mario Z.", 0);

        ResponseEntity<Author> updateResponse = restTemplate.exchange(
                "/api/v1/authors/" + nonExistentId,
                HttpMethod.PUT,
                new HttpEntity<>(updatedAuthor),
                Author.class);

        assertEquals(HttpStatus.NOT_FOUND, updateResponse.getStatusCode());
    }

    @Test
    void testGetAuthorByIdNotFound() {
        Long nonExistentId = 999L;

        ResponseEntity<String> response = restTemplate.getForEntity(
                "/api/v1/authors/" + nonExistentId, String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteAuthorNotFound() {
        Long nonExistentId = 999L;

        restTemplate.delete("/api/v1/authors/" + nonExistentId);

        ResponseEntity<String> getResponse = restTemplate.getForEntity(
                "/api/v1/authors/" + nonExistentId, String.class);

        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

}
