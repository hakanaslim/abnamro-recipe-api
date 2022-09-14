package com.abnamro.recipe.service;

import com.abnamro.recipe.api.model.AuthorCreateUpdate;
import com.abnamro.recipe.consts.AuthorBuilder;
import com.abnamro.recipe.consts.RecipeBuilder;
import com.abnamro.recipe.exception.ResourceNotFoundException;
import com.abnamro.recipe.filter.RequestHeaderFilter;
import com.abnamro.recipe.filter.RequestHeaderStore;
import com.abnamro.recipe.persistance.model.Author;
import com.abnamro.recipe.persistance.repository.AuthorRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("junit")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthorServiceIT {

    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private AuthorServiceImpl authorService;
    @Autowired
    private RequestHeaderStore requestHeaderStore;
    @Autowired
    private RequestHeaderFilter filter;

    @BeforeEach
    void setUp() throws ServletException, IOException {
        authorRepository.deleteAll();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("x-user-id", RecipeBuilder.PERSIST_BY);

        filter.doFilter(request, new MockHttpServletResponse(), null);
    }

    @Test
    @DisplayName("create an author")
    void test_create_successful() {
        AuthorCreateUpdate body = AuthorBuilder.buildBody();

        StepVerifier.create(authorService.create(body))
                .assertNext(actual -> {
                    assertThat(actual).isNotNull();
                    assertThat(actual.getId()).isNotNull();
                    assertThat(actual.getFullName()).isNotNull();
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("find an author")
    void test_getById_successful() {
        AuthorCreateUpdate body = AuthorBuilder.buildBody();
        Author author = authorService.create(body).block();

        StepVerifier
                .create(authorService.getById(author.getId()))
                .assertNext(actual -> {
                    assertThat(actual).isNotNull();
                    assertThat(actual.getId()).isNotNull();
                    assertThat(actual.getFullName()).isNotNull();
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Author not found fail")
    void test_getById_getById_thrown_notFoundException_when_not_exists_fail() {
        StepVerifier
            .create(authorService.getById(AuthorBuilder.ID))
            .expectErrorMatches(throwable -> throwable.getMessage().contains("Author not found with id"))
            .verify();
    }

    @Test
    @DisplayName("Delete exists author")
    void test_delete_successful() {
        AuthorCreateUpdate body = AuthorBuilder.buildBody();
        Author author = authorService.create(body).block();

        assertDoesNotThrow(()-> authorService.delete(author.getId()));
    }

    @Test
    @DisplayName("Throw exception while deleting not exists author")
    void test_delete_thrown_notFoundException_fail() {
        var ex =
                assertThrows(ResourceNotFoundException.class,
                        ()-> authorService.delete(AuthorBuilder.ID));
        Assertions.assertTrue(ex.getMessage().contains("Author not found with id"));
    }

    @Test
    @DisplayName("filter author with predicate")
    void test_filter_with_predicate_return_successful() {
        AuthorCreateUpdate body = AuthorBuilder.buildBody();
        Author author = authorService.create(body).block();

        PathBuilder<Author> entityPath = new PathBuilder<>(Author.class, "author");
        StringPath path = entityPath.getString("fullName");
        BooleanExpression fullNameExpression = path.equalsIgnoreCase(author.getFullName());

        StepVerifier.create(
                authorService.filter(fullNameExpression))
                .assertNext(actual -> {
                    assertThat(actual).isNotNull();
                    assertThat(actual.getId()).isNotNull();
                    assertThat(actual.getFullName()).isNotNull();
                })
                .expectNextCount(0)
                .verifyComplete();
    }

}
