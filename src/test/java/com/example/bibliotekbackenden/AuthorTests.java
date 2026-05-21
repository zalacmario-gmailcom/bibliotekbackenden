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
class AuthorTests {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private ObjectMapper objectMapper;

	private String token() {
		return "Bearer " + jwtUtil.generateToken("admin");
	}

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

	private Long createAuthor(String name) throws Exception {
		var body = Map.of("name", name);

		String response = authorizedPost("/api/authors", body)
				.andExpect(status().isCreated())
				.andReturn()
				.getResponse()
				.getContentAsString();

		return objectMapper.readTree(response)
				.get("id")
				.asLong();
	}

	private void createBook(Long authorId, String title, String isbn) throws Exception {
		var body = Map.of(
				"title", title,
				"authorId", authorId,
				"isbn", isbn,
				"publishedYear", 1954);

		authorizedPost("/api/books/v1", body)
				.andExpect(status().isCreated());
	}

	@Test
	void shouldCreateAuthor() throws Exception {
		String name = "Tolkien-" + UUID.randomUUID();

		authorizedPost("/api/authors", Map.of("name", name))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(jsonPath("$.name").value(name))
				.andExpect(jsonPath("$.bookCount").value(0));
	}

	@Test
	void shouldReturnForbiddenWithoutToken() throws Exception {
		mockMvc.perform(post("/api/authors")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						    "name": "Tolkien"
						}
						"""))
				.andExpect(status().isForbidden());
	}

	@Test
	void shouldUpdateAuthor() throws Exception {
		Long authorId = createAuthor("Tolkien-" + UUID.randomUUID());
		String updatedName = "J.R.R Tolkien-" + UUID.randomUUID();

		authorizedPut("/api/authors/" + authorId,
				Map.of("name", updatedName))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(authorId))
				.andExpect(jsonPath("$.name").value(updatedName));
	}

	@Test
	void shouldGetAuthorById() throws Exception {
		String name = "Lewis-" + UUID.randomUUID();
		Long authorId = createAuthor(name);

		authorizedGet("/api/authors/" + authorId)
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(authorId))
				.andExpect(jsonPath("$.name").value(name))
				.andExpect(jsonPath("$.bookCount").value(0));
	}

	@Test
	void shouldGetBooksForAuthor() throws Exception {
		Long authorId = createAuthor("Rowling-" + UUID.randomUUID());

		String title = "Harry Potter-" + UUID.randomUUID();
		String isbn = "ISBN-" + UUID.randomUUID();

		createBook(authorId, title, isbn);

		authorizedGet("/api/authors/" + authorId + "/books")
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].title").value(title))
				.andExpect(jsonPath("$[0].isbn").value(isbn));
	}

	@Test
	void shouldGetAllAuthors() throws Exception {
		mockMvc.perform(get("/api/authors")
				.param("page", "0")
				.param("size", "5")
				.header("Authorization", token()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content").isArray())
				.andExpect(jsonPath("$.content[0].id").exists())
				.andExpect(jsonPath("$.content[0].name").exists());
	}

	@Test
	void shouldDeleteAuthor() throws Exception {
		Long authorId = createAuthor("DeleteMe-" + UUID.randomUUID());

		authorizedDelete("/api/authors/" + authorId)
				.andExpect(status().isOk());

		authorizedGet("/api/authors/" + authorId)
				.andExpect(status().isNotFound());
	}

	// ---------- Login Tests ----------

	@Test
	void shouldLoginWithValidCredentials() throws Exception {
		mockMvc.perform(post("/api/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						    "username": "admin",
						    "password": "password"
						}
						"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.token").exists())
				.andExpect(jsonPath("$.token").isNotEmpty());
	}

	@Test
	void shouldReturn401WithInvalidCredentials() throws Exception {
		mockMvc.perform(post("/api/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content("""
						{
						    "username": "admin",
						    "password": "wrongpassword"
						}
						"""))
				.andExpect(status().isUnauthorized());
	}
}