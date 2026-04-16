package com.example.bibliotekbackenden;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.annotation.DirtiesContext;
import com.example.bibliotekbackenden.Dto.Author.v1.AuthorCreateDTO;
import com.example.bibliotekbackenden.Dto.Author.v1.AuthorResponseDTO;
import com.example.bibliotekbackenden.Dto.Book.v2.BookCreateDTOv2;
import com.example.bibliotekbackenden.Dto.Book.v2.BookResponseDTOv2;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldCreateBookV2() {
        AuthorCreateDTO author = new AuthorCreateDTO("Mario Z.", 0);
        ResponseEntity<AuthorResponseDTO> authorResponse = restTemplate.postForEntity(
                "/api/v1/authors", author, AuthorResponseDTO.class);

        Long authorId = authorResponse.getBody().id();

        BookCreateDTOv2 book = new BookCreateDTOv2("Computer Science", authorId, "123XAB", 2025, true);

        ResponseEntity<BookResponseDTOv2> bookResponse = restTemplate.postForEntity(
                "/books/v2", book, BookResponseDTOv2.class);

        assertEquals(HttpStatus.CREATED, bookResponse.getStatusCode());
    }

    @Test
    void shouldGetBookById() {
        AuthorCreateDTO author = new AuthorCreateDTO("Mario Z.", 0);
        ResponseEntity<AuthorResponseDTO> authorResponse = restTemplate.postForEntity(
                "/api/v1/authors", author, AuthorResponseDTO.class);

        Long authorId = authorResponse.getBody().id();

        BookCreateDTOv2 book = new BookCreateDTOv2("Computer Science", authorId, "123XAB", 2025, true);
        ResponseEntity<BookResponseDTOv2> bookResponse = restTemplate.postForEntity(
                "/books/v2", book, BookResponseDTOv2.class);

        Long bookId = bookResponse.getBody().id();

        ResponseEntity<BookResponseDTOv2> getBookByIdResponse = restTemplate.getForEntity(
                "/books/" + bookId, BookResponseDTOv2.class);

        assertEquals(HttpStatus.OK, getBookByIdResponse.getStatusCode());
    }

    @Test
    void shouldGetAllBooks() {
        AuthorCreateDTO author = new AuthorCreateDTO("Mario Z.", 0);
        ResponseEntity<AuthorResponseDTO> authorResponse = restTemplate.postForEntity(
                "/api/v1/authors", author, AuthorResponseDTO.class);

        Long authorId = authorResponse.getBody().id();

        BookCreateDTOv2 book = new BookCreateDTOv2("Computer Science", authorId, "123XAB", 2025, true);
        ResponseEntity<BookResponseDTOv2> bookResponse = restTemplate.postForEntity(
                "/books/v2", book, BookResponseDTOv2.class);

        ResponseEntity<BookResponseDTOv2[]> getAllBooksResponse = restTemplate.getForEntity(
                "/books", BookResponseDTOv2[].class);

        assertEquals(HttpStatus.CREATED, bookResponse.getStatusCode());
        assertEquals(HttpStatus.OK, getAllBooksResponse.getStatusCode());
    }

    @Test
    void shouldUpdateBook() {
        AuthorCreateDTO author = new AuthorCreateDTO("Mario Z.", 0);
        ResponseEntity<AuthorResponseDTO> authorResponse = restTemplate.postForEntity(
                "/api/v1/authors", author, AuthorResponseDTO.class);

        Long authorId = authorResponse.getBody().id();

        BookCreateDTOv2 book = new BookCreateDTOv2("Computer Science", authorId, "123XAB", 2025, true);
        ResponseEntity<BookResponseDTOv2> bookResponse = restTemplate.postForEntity(
                "/books/v2", book, BookResponseDTOv2.class);

        Long bookId = bookResponse.getBody().id();

        BookCreateDTOv2 updatedBook = new BookCreateDTOv2("Computer ScienceV2", authorId, "123XABv2", 2022, false);
        ResponseEntity<BookResponseDTOv2> updatedBookResponse = restTemplate.exchange(
                "/books/" + bookId, HttpMethod.PUT, new HttpEntity<>(updatedBook), BookResponseDTOv2.class);

        assertEquals(HttpStatus.OK, updatedBookResponse.getStatusCode());
    }

    @Test
    void shouldDeleteBook() {
        AuthorCreateDTO author = new AuthorCreateDTO("Mario Z.", 0);
        ResponseEntity<AuthorResponseDTO> authorResponse = restTemplate.postForEntity(
                "/api/v1/authors", author, AuthorResponseDTO.class);

        Long authorId = authorResponse.getBody().id();

        BookCreateDTOv2 book = new BookCreateDTOv2("Computer Science", authorId, "123XAB", 2025, true);
        ResponseEntity<BookResponseDTOv2> bookResponse = restTemplate.postForEntity(
                "/books/v2", book, BookResponseDTOv2.class);

        Long bookId = bookResponse.getBody().id();

        restTemplate.delete("/books/" + bookId);

        ResponseEntity<BookResponseDTOv2[]> getAllBooksResponse = restTemplate.getForEntity(
                "/books", BookResponseDTOv2[].class);

        assertEquals(HttpStatus.OK, getAllBooksResponse.getStatusCode());
        assertEquals(0, getAllBooksResponse.getBody().length);
    }

}
