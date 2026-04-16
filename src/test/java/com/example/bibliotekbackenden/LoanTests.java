package com.example.bibliotekbackenden;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import com.example.bibliotekbackenden.Dto.Author.v1.AuthorCreateDTO;
import com.example.bibliotekbackenden.Dto.Author.v1.AuthorResponseDTO;
import com.example.bibliotekbackenden.Dto.Book.v2.BookCreateDTOv2;
import com.example.bibliotekbackenden.Dto.Book.v2.BookResponseDTOv2;
import com.example.bibliotekbackenden.Dto.Loan.v1.LoanCreateDTO;
import com.example.bibliotekbackenden.Dto.Loan.v1.LoanResponseDTO;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class LoanTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldCreateLoan() {
        AuthorCreateDTO author = new AuthorCreateDTO("Mario Z.", 0);
        ResponseEntity<AuthorResponseDTO> authorResponse = restTemplate.postForEntity(
                "/api/v1/authors", author, AuthorResponseDTO.class);

        Long authorId = authorResponse.getBody().id();
        BookCreateDTOv2 book = new BookCreateDTOv2("Computer Science", authorId, "123XAB", 2025, true);
        ResponseEntity<BookResponseDTOv2> bookResponse = restTemplate.postForEntity(
                "/books/v2", book, BookResponseDTOv2.class);

        Long bookId = bookResponse.getBody().id();
        LoanCreateDTO loan = new LoanCreateDTO(
                bookId,
                java.sql.Date.valueOf("2024-07-01"),
                java.sql.Date.valueOf("2024-07-15"));
        ResponseEntity<LoanResponseDTO> loanResponse = restTemplate.postForEntity(
                "/loans", loan, LoanResponseDTO.class);

        assertEquals(HttpStatus.CREATED, loanResponse.getStatusCode());
    }

    @Test
    void shouldGetLoanById() {
        AuthorCreateDTO author = new AuthorCreateDTO("Mario Z.", 0);
        ResponseEntity<AuthorResponseDTO> authorResponse = restTemplate.postForEntity(
                "/api/v1/authors", author, AuthorResponseDTO.class);

        Long authorId = authorResponse.getBody().id();
        BookCreateDTOv2 book = new BookCreateDTOv2("Computer Science", authorId, "123XAB", 2025, true);
        ResponseEntity<BookResponseDTOv2> bookResponse = restTemplate.postForEntity(
                "/books/v2", book, BookResponseDTOv2.class);

        Long bookId = bookResponse.getBody().id();
        LoanCreateDTO loan = new LoanCreateDTO(
                bookId,
                java.sql.Date.valueOf("2024-07-01"),
                java.sql.Date.valueOf("2024-07-15"));
        ResponseEntity<LoanResponseDTO> loanResponse = restTemplate.postForEntity(
                "/loans", loan, LoanResponseDTO.class);

        Long loanId = loanResponse.getBody().id();
        ResponseEntity<LoanResponseDTO> getLoanByIdResponse = restTemplate.getForEntity(
                "/loans/" + loanId, LoanResponseDTO.class);

        assertEquals(HttpStatus.OK, getLoanByIdResponse.getStatusCode());
    }

    @Test
    void shouldDeleteLoan() {
        AuthorCreateDTO author = new AuthorCreateDTO("Mario Z.", 0);
        ResponseEntity<AuthorResponseDTO> authorResponse = restTemplate.postForEntity(
                "/api/v1/authors", author, AuthorResponseDTO.class);

        Long authorId = authorResponse.getBody().id();
        BookCreateDTOv2 book = new BookCreateDTOv2("Computer Science", authorId, "123XAB", 2025, true);
        ResponseEntity<BookResponseDTOv2> bookResponse = restTemplate.postForEntity(
                "/books/v2", book, BookResponseDTOv2.class);

        Long bookId = bookResponse.getBody().id();
        LoanCreateDTO loan = new LoanCreateDTO(
                bookId,
                java.sql.Date.valueOf("2024-07-01"),
                java.sql.Date.valueOf("2024-07-15"));
        ResponseEntity<LoanResponseDTO> loanResponse = restTemplate.postForEntity(
                "/loans", loan, LoanResponseDTO.class);

        Long loanId = loanResponse.getBody().id();
        restTemplate.delete("/loans/" + loanId);

        ResponseEntity<LoanResponseDTO> getLoanResponse = restTemplate.getForEntity(
                "/loans/" + loanId, LoanResponseDTO.class);

        assertEquals(HttpStatus.NOT_FOUND, getLoanResponse.getStatusCode());
    }
}
