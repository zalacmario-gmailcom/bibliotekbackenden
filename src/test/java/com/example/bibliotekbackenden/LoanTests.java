package com.example.bibliotekbackenden;

import com.example.bibliotekbackenden.Configuration.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class LoanTests {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private JwtUtil jwtUtil;

        @Autowired
        private ObjectMapper objectMapper;

        private String token() {
                return "Bearer " + jwtUtil.generateToken("admin");
        }

        // ---------- Helper Methods ----------

        private ResultActions authorizedPost(String url, Object body) throws Exception {
                return mockMvc.perform(post(url)
                                .header("Authorization", token())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(body)));
        }

        private ResultActions authorizedGet(String url) throws Exception {
                return mockMvc.perform(get(url)
                                .header("Authorization", token()));
        }

        private ResultActions authorizedDelete(String url) throws Exception {
                return mockMvc.perform(delete(url)
                                .header("Authorization", token()));
        }

        // ---------- Test Data Creators ----------

        private Long createAuthor(String name) throws Exception {

                String response = authorizedPost("/api/authors",
                                Map.of("name", name))
                                .andExpect(status().isCreated())
                                .andReturn()
                                .getResponse()
                                .getContentAsString();

                return objectMapper.readTree(response)
                                .get("id")
                                .asLong();
        }

        private Long createBook(
                        String title,
                        Long authorId,
                        String isbn,
                        int publishedYear) throws Exception {

                var body = Map.of(
                                "title", title,
                                "authorId", authorId,
                                "isbn", isbn,
                                "publishedYear", publishedYear);

                String response = authorizedPost("/api/books/v1", body)
                                .andExpect(status().isCreated())
                                .andReturn()
                                .getResponse()
                                .getContentAsString();

                return objectMapper.readTree(response)
                                .get("id")
                                .asLong();
        }

        private Long createLoan(
                        Long bookId,
                        String loanDate,
                        String returnDate) throws Exception {

                var body = Map.of(
                                "bookId", bookId,
                                "loanDate", loanDate,
                                "returnDate", returnDate);

                String response = authorizedPost("/api/loans", body)
                                .andExpect(status().isCreated())
                                .andReturn()
                                .getResponse()
                                .getContentAsString();

                return objectMapper.readTree(response)
                                .get("id")
                                .asLong();
        }

        // ---------- Tests ----------

        @Test
        void shouldCreateLoan() throws Exception {

                Long authorId = createAuthor("Author-" + UUID.randomUUID());

                String title = "Loan Book-" + UUID.randomUUID();

                Long bookId = createBook(
                                title,
                                authorId,
                                "ISBN-" + UUID.randomUUID(),
                                2024);

                var body = Map.of(
                                "bookId", bookId,
                                "loanDate", "2026-05-21",
                                "returnDate", "2026-06-21");

                authorizedPost("/api/loans", body)
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").exists())
                                .andExpect(jsonPath("$.bookId").value(bookId))
                                .andExpect(jsonPath("$.bookTitle").value(title))
                                .andExpect(jsonPath("$.loanDate").value("2026-05-21"))
                                .andExpect(jsonPath("$.returnDate").value("2026-06-21"));
        }

        @Test
        void shouldGetLoanById() throws Exception {

                Long authorId = createAuthor("Author-" + UUID.randomUUID());

                String title = "Loaned Book-" + UUID.randomUUID();

                Long bookId = createBook(
                                title,
                                authorId,
                                "ISBN-" + UUID.randomUUID(),
                                2025);

                Long loanId = createLoan(
                                bookId,
                                "2026-05-21",
                                "2026-06-21");

                authorizedGet("/api/loans/" + loanId)
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(loanId))
                                .andExpect(jsonPath("$.bookId").value(bookId))
                                .andExpect(jsonPath("$.bookTitle").value(title))
                                .andExpect(jsonPath("$.loanDate").value("2026-05-21"))
                                .andExpect(jsonPath("$.returnDate").value("2026-06-21"));
        }

        @Test
        void shouldDeleteLoan() throws Exception {

                Long authorId = createAuthor("Author-" + UUID.randomUUID());

                Long bookId = createBook(
                                "Delete Loan Book-" + UUID.randomUUID(),
                                authorId,
                                "ISBN-" + UUID.randomUUID(),
                                2023);

                Long loanId = createLoan(
                                bookId,
                                "2026-05-21",
                                "2026-06-21");

                authorizedDelete("/api/loans/" + loanId)
                                .andExpect(status().isOk());

                authorizedGet("/api/loans/" + loanId)
                                .andExpect(status().isNotFound());
        }

        @Test
        void shouldHandleConcurrentLoanRequests() throws Exception {

                Long authorId = createAuthor("Author-" + UUID.randomUUID());

                Long bookId = createBook(
                                "Concurrent Loan Book-" + UUID.randomUUID(),
                                authorId,
                                "ISBN-" + UUID.randomUUID(),
                                2022);

                var requestBody = Map.of(
                                "bookId", bookId,
                                "loanDate", "2026-05-21",
                                "returnDate", "2026-06-21");

                ExecutorService executor = Executors.newFixedThreadPool(2);

                CountDownLatch ready = new CountDownLatch(2);
                CountDownLatch start = new CountDownLatch(1);

                Callable<Integer> requestTask = () -> {
                        ready.countDown();
                        start.await();

                        return authorizedPost("/api/loans", requestBody)
                                        .andReturn()
                                        .getResponse()
                                        .getStatus();
                };

                Future<Integer> firstRequest = executor.submit(requestTask);
                Future<Integer> secondRequest = executor.submit(requestTask);

                ready.await();
                start.countDown();

                int firstStatus = firstRequest.get();
                int secondStatus = secondRequest.get();

                executor.shutdown();

                // One request succeeds (201)
                // One request fails (409)
                assertEquals(610, firstStatus + secondStatus);
        }
}