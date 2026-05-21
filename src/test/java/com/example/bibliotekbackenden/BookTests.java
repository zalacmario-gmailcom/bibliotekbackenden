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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class BookTests {
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

        private ResultActions authorizedPut(String url, Object body) throws Exception {
                return mockMvc.perform(put(url)
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
                        int publishedYear,
                        boolean isAvailable) throws Exception {

                var body = Map.of(
                                "title", title,
                                "authorId", authorId,
                                "isbn", isbn,
                                "publishedYear", publishedYear,
                                "isAvailable", isAvailable);

                String response = authorizedPost("/api/books/v2", body)
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
        void shouldCreateBookV2() throws Exception {

                String authorName = "Author-" + UUID.randomUUID();
                Long authorId = createAuthor(authorName);

                String title = "Computer Science-" + UUID.randomUUID();
                String isbn = "ISBN-" + UUID.randomUUID();

                var body = Map.of(
                                "title", title,
                                "authorId", authorId,
                                "isbn", isbn,
                                "publishedYear", 2025,
                                "isAvailable", true);

                authorizedPost("/api/books/v2", body)
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").exists())
                                .andExpect(jsonPath("$.title").value(title))
                                .andExpect(jsonPath("$.author").value(authorName))
                                .andExpect(jsonPath("$.isbn").value(isbn))
                                .andExpect(jsonPath("$.publishedYear").value(2025))
                                .andExpect(jsonPath("$.isAvailable").value(true))
                                .andExpect(jsonPath("$.version").value("v2"));
        }

        @Test
        void shouldGetBookById() throws Exception {

                String authorName = "Author-" + UUID.randomUUID();
                Long authorId = createAuthor(authorName);

                String title = "Algorithms-" + UUID.randomUUID();
                String isbn = "ISBN-" + UUID.randomUUID();

                Long bookId = createBook(title, authorId, isbn, 2024, true);

                authorizedGet("/api/books/" + bookId)
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(bookId))
                                .andExpect(jsonPath("$.title").value(title))
                                .andExpect(jsonPath("$.author").value(authorName))
                                .andExpect(jsonPath("$.isbn").value(isbn))
                                .andExpect(jsonPath("$.publishedYear").value(2024));
        }

        @Test
        void shouldGetAllBooks() throws Exception {

                Long authorId = createAuthor("Author-" + UUID.randomUUID());

                createBook(
                                "Databases-" + UUID.randomUUID(),
                                authorId,
                                "ISBN-" + UUID.randomUUID(),
                                2023,
                                true);

                mockMvc.perform(get("/api/books")
                                .param("page", "0")
                                .param("size", "5")
                                .header("Authorization", token()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content").isArray())
                                .andExpect(jsonPath("$.content[0].id").exists())
                                .andExpect(jsonPath("$.content[0].title").exists());
        }

        @Test
        void shouldUpdateBook() throws Exception {

                String authorName = "Author-" + UUID.randomUUID();
                Long authorId = createAuthor(authorName);

                Long bookId = createBook(
                                "Networks-" + UUID.randomUUID(),
                                authorId,
                                "ISBN-" + UUID.randomUUID(),
                                2022,
                                true);

                String updatedTitle = "Networks Updated-" + UUID.randomUUID();
                String updatedIsbn = "ISBN-UPDATED-" + UUID.randomUUID();

                var body = Map.of(
                                "id", bookId,
                                "title", updatedTitle,
                                "author", authorName,
                                "isbn", updatedIsbn,
                                "publishedYear", 2026,
                                "isAvailable", false,
                                "version", "v2");

                authorizedPut("/api/books/" + bookId, body)
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(bookId))
                                .andExpect(jsonPath("$.title").value(updatedTitle))
                                .andExpect(jsonPath("$.author").value(authorName))
                                .andExpect(jsonPath("$.isbn").value(updatedIsbn))
                                .andExpect(jsonPath("$.publishedYear").value(2026))
                                .andExpect(jsonPath("$.available").value(false));
        }

        @Test
        void shouldDeleteBook() throws Exception {

                Long authorId = createAuthor("Author-" + UUID.randomUUID());

                Long bookId = createBook(
                                "Delete Me-" + UUID.randomUUID(),
                                authorId,
                                "ISBN-" + UUID.randomUUID(),
                                2020,
                                true);

                authorizedDelete("/api/books/" + bookId)
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(bookId));

                authorizedGet("/api/books/" + bookId)
                                .andExpect(status().isNotFound());
        }
}