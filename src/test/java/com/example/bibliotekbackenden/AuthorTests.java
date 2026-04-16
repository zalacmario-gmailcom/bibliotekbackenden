package com.example.bibliotekbackenden;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.example.bibliotekbackenden.Dto.Author.v1.AuthorCreateDTO;
import com.example.bibliotekbackenden.Dto.Author.v1.AuthorResponseDTO;
import com.example.bibliotekbackenden.Dto.Book.v1.BookCreateDTO;
import com.example.bibliotekbackenden.Dto.Book.v1.BookResponseDTO;
import com.example.bibliotekbackenden.Entity.Book;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AuthorTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void shouldCreateAuthorAndBook() {
		// Creaate author
		AuthorCreateDTO author = new AuthorCreateDTO("J.K. Rowling", 0);

		ResponseEntity<AuthorResponseDTO> authorResponse = restTemplate.postForEntity("/api/v1/authors", author,
				AuthorResponseDTO.class);

		assertEquals(HttpStatus.CREATED, authorResponse.getStatusCode());
	}

	@Test
	void shuldUpdateAuthor() {
		AuthorCreateDTO author = new AuthorCreateDTO("J.K. Rowling", 0);
		ResponseEntity<AuthorResponseDTO> authorResponse = restTemplate.postForEntity("/api/v1/authors", author,
				AuthorResponseDTO.class);
		assertEquals(HttpStatus.CREATED, authorResponse.getStatusCode());

		Long authorId = authorResponse.getBody().id();
		AuthorCreateDTO updatedAuthor = new AuthorCreateDTO("Mario Z.", 0);
		restTemplate.put("/api/v1/authors/" + authorId, updatedAuthor);

		ResponseEntity<AuthorResponseDTO> authorById = restTemplate.getForEntity("/api/v1/authors/" + authorId,
				AuthorResponseDTO.class);

		assertEquals(HttpStatus.OK, authorById.getStatusCode());
	}

	@Test
	void shouldGetAuthorById() {
		AuthorCreateDTO author = new AuthorCreateDTO("J.K. Rowling", 0);

		ResponseEntity<AuthorResponseDTO> authorResponse = restTemplate.postForEntity("/api/v1/authors", author,
				AuthorResponseDTO.class);

		assertEquals(HttpStatus.CREATED, authorResponse.getStatusCode());

		Long authorId = authorResponse.getBody().id();

		ResponseEntity<AuthorResponseDTO> authorById = restTemplate.getForEntity("/api/v1/authors/" + authorId,
				AuthorResponseDTO.class);

		assertEquals(HttpStatus.OK, authorById.getStatusCode());
	}

	@Test
	void shouldGetBooksForAuthorId() {
		// Create author
		AuthorCreateDTO author = new AuthorCreateDTO("Mario Z.", 0);
		ResponseEntity<AuthorResponseDTO> authorResponse = restTemplate.postForEntity("/api/v1/authors", author,
				AuthorResponseDTO.class);

		// Create book for author
		BookCreateDTO book = new BookCreateDTO(
				"Computer Science",
				authorResponse.getBody().id(),
				"123XAB",
				2025);
		ResponseEntity<BookResponseDTO> bookResponse = restTemplate.postForEntity(
				"/books", book, BookResponseDTO.class);

		assertEquals(HttpStatus.CREATED, authorResponse.getStatusCode());
		assertEquals(HttpStatus.CREATED, bookResponse.getStatusCode());

		Long authorId = authorResponse.getBody().id();

		ResponseEntity<List<Book>> booksResponse = restTemplate.exchange(
				"/api/v1/authors/" + authorId + "/books",
				org.springframework.http.HttpMethod.GET,
				null,
				new ParameterizedTypeReference<List<Book>>() {
				});

		assertEquals(HttpStatus.OK, booksResponse.getStatusCode());
		assertEquals(1, booksResponse.getBody().size());
		assertEquals("Computer Science", booksResponse.getBody().get(0).getTitle());
	}

	@Test
	void shouldGetAllAuthors() {
		AuthorCreateDTO author1 = new AuthorCreateDTO("Author 1", 0);
		AuthorCreateDTO author2 = new AuthorCreateDTO("Author 2", 0);

		ResponseEntity<AuthorResponseDTO> response1 = restTemplate.postForEntity("/api/v1/authors", author1,
				AuthorResponseDTO.class);
		ResponseEntity<AuthorResponseDTO> response2 = restTemplate.postForEntity("/api/v1/authors", author2,
				AuthorResponseDTO.class);

		assertEquals(HttpStatus.CREATED, response1.getStatusCode());
		assertEquals(HttpStatus.CREATED, response2.getStatusCode());

		ResponseEntity<List<AuthorResponseDTO>> getAllResponse = restTemplate.exchange(
				"/api/v1/authors",
				org.springframework.http.HttpMethod.GET,
				null,
				new ParameterizedTypeReference<List<AuthorResponseDTO>>() {
				});

		assertEquals(HttpStatus.OK, getAllResponse.getStatusCode());
		assertEquals(2, getAllResponse.getBody().size());
	}

	@Test
	void shouldDeleteAuthor() {
		AuthorCreateDTO author = new AuthorCreateDTO("Author to Delete", 0);
		ResponseEntity<AuthorResponseDTO> response = restTemplate.postForEntity("/api/v1/authors", author,
				AuthorResponseDTO.class);

		assertEquals(HttpStatus.CREATED, response.getStatusCode());

		Long authorId = response.getBody().id();

		restTemplate.delete("/api/v1/authors/" + authorId);

		ResponseEntity<List<AuthorResponseDTO>> getAllResponse = restTemplate.exchange(
				"/api/v1/authors",
				org.springframework.http.HttpMethod.GET,
				null,
				new ParameterizedTypeReference<List<AuthorResponseDTO>>() {
				});

		assertEquals(HttpStatus.OK, getAllResponse.getStatusCode());
		assertEquals(0, getAllResponse.getBody().size());
	}
}
