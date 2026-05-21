package com.example.bibliotekbackenden;

import com.example.bibliotekbackenden.Configuration.JwtUtil;
import com.example.bibliotekbackenden.Dto.Author.v1.AuthorCreateDTO;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class WrongResultTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private static final Long NON_EXISTENT_ID = 999L;

    private String token() {
        return "Bearer " + jwtUtil.generateToken("admin");
    }

    // ---------- Helper Methods ----------

    private String authorUrl(Long id) {
        return "/api/authors/" + id;
    }

    private ResultActions getAuthor(Long id) throws Exception {
        return mockMvc.perform(
                get(authorUrl(id))
                        .header("Authorization", token()));
    }

    private ResultActions updateAuthor(Long id, Object body) throws Exception {
        return mockMvc.perform(
                put(authorUrl(id))
                        .header("Authorization", token())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)));
    }

    private ResultActions deleteAuthor(Long id) throws Exception {
        return mockMvc.perform(
                delete(authorUrl(id))
                        .header("Authorization", token()));
    }

    private String bookUrl(Long id) {
        return "/api/books/" + id;
    }

    private ResultActions getBook(Long id) throws Exception {
        return mockMvc.perform(
                get(bookUrl(id))
                        .header("Authorization", token()));
    }

    private ResultActions updateBook(Long id, Object body) throws Exception {
        return mockMvc.perform(
                put(bookUrl(id))
                        .header("Authorization", token())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)));
    }

    private ResultActions deleteBook(Long id) throws Exception {
        return mockMvc.perform(
                delete(bookUrl(id))
                        .header("Authorization", token()));
    }

    private String loanUrl(Long id) {
        return "/api/loans/" + id;
    }

    private ResultActions getLoan(Long id) throws Exception {
        return mockMvc.perform(
                get(loanUrl(id))
                        .header("Authorization", token()));
    }

    private ResultActions deleteLoan(Long id) throws Exception {
        return mockMvc.perform(
                delete(loanUrl(id))
                        .header("Authorization", token()));
    }

    // ---------- Tests ----------

    @Test
    void shouldReturn404WhenUpdatingMissingAuthor() throws Exception {

        var request = new AuthorCreateDTO(
                "Mario Z.",
                0);

        updateAuthor(NON_EXISTENT_ID, request)
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404WhenGettingMissingAuthor() throws Exception {

        getAuthor(NON_EXISTENT_ID)
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404WhenDeletingMissingAuthor() throws Exception {

        deleteAuthor(NON_EXISTENT_ID)
                .andExpect(status().isNotFound());
    }

    // ---------- Book Tests ----------

    @Test
    void shouldReturn404WhenGettingMissingBook() throws Exception {

        getBook(NON_EXISTENT_ID)
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404WhenUpdatingMissingBook() throws Exception {

        var body = Map.of(
                "id", NON_EXISTENT_ID,
                "title", "Ghost Book",
                "author", "Unknown",
                "isbn", "ISBN-000",
                "publishedYear", 2020,
                "isAvailable", true,
                "version", "v2");

        updateBook(NON_EXISTENT_ID, body)
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404WhenDeletingMissingBook() throws Exception {

        deleteBook(NON_EXISTENT_ID)
                .andExpect(status().isNotFound());
    }

    // ---------- Loan Tests ----------

    @Test
    void shouldReturn404WhenGettingMissingLoan() throws Exception {

        getLoan(NON_EXISTENT_ID)
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn404WhenDeletingMissingLoan() throws Exception {

        deleteLoan(NON_EXISTENT_ID)
                .andExpect(status().isNotFound());
    }
}