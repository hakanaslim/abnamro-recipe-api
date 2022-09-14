package com.abnamro.recipe.service;

import com.abnamro.recipe.api.model.AuthorCreateUpdate;
import com.abnamro.recipe.consts.AuthorBuilder;
import com.abnamro.recipe.consts.RecipeBuilder;
import com.abnamro.recipe.exception.ResourceNotFoundException;
import com.abnamro.recipe.filter.RequestHeaderStore;
import com.abnamro.recipe.persistance.model.Author;
import com.abnamro.recipe.persistance.repository.AuthorRepository;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class AuthorServiceTests {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private RequestHeaderStore requestHeaderStore;

    private AuthorServiceImpl authorService;

    @BeforeEach
    void setUp() {
        authorService = new AuthorServiceImpl(authorRepository, requestHeaderStore);
    }

    @Test
    @DisplayName("create an author")
    void test_create_successful() {
        AuthorCreateUpdate body = AuthorBuilder.buildBody();

        when(requestHeaderStore.getUserID()).thenReturn(RecipeBuilder.PERSIST_BY);
        when(authorRepository.save(any(Author.class))).thenReturn(AuthorBuilder.buildAuthor());
        assertDoesNotThrow(()->authorService.create(body));
    }

    @Test
    @DisplayName("create an author when FullName is empty fail")
    void test_create_with_invalid_param_fail() {
        AuthorCreateUpdate body = AuthorBuilder.buildBody();
        body.setFullName(null);

        assertThrows(IllegalArgumentException.class, ()-> authorService.create(body));
        verify(authorRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("update an author")
    void test_update_successful() {
        AuthorCreateUpdate body = AuthorBuilder.buildBody();

        when(requestHeaderStore.getUserID()).thenReturn(RecipeBuilder.PERSIST_BY);
        when(authorRepository.existsById(anyString())).thenReturn(true);
        when(authorRepository.save(any(Author.class))).thenReturn(AuthorBuilder.buildAuthor());
        assertDoesNotThrow(()->authorService.update(AuthorBuilder.ID, body));
    }

    @Test
    @DisplayName("update an author when id is null should thrown exception")
    void test_update_an_author_when_id_is_null_should_thrown_exception() {
        AuthorCreateUpdate body = AuthorBuilder.buildBody();

        when(authorRepository.existsById(any())).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, ()-> authorService.update(null, body));
        verify(authorRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("update an author when FullName is null should thrown exception")
    void test_update_author_when_fullname_is_null_should_thrown_exception() {
        AuthorCreateUpdate body = AuthorBuilder.buildBody();
        body.setFullName(null);

        when(authorRepository.existsById(anyString())).thenReturn(true);
        assertThrows(IllegalArgumentException.class, ()-> authorService.update(AuthorBuilder.ID, body));
        verify(authorRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("get an author successful")
    void test_getById_successful() {
        when(authorRepository.findById(anyString())).thenReturn(Optional.of(AuthorBuilder.buildAuthor()));

        assertDoesNotThrow(()-> authorService.getById(AuthorBuilder.ID));
    }

    @Test
    @DisplayName("get an author when non existing id thrown exception")
    void test_getById_with_noExists_id_should_thrown_exception() {
        when(authorRepository.findById(anyString())).thenReturn(Optional.empty());

        StepVerifier.create(authorService.getById(AuthorBuilder.ID))
                .expectErrorMatches(throwable -> throwable.getMessage().contains("Author not found with id"))
                .verify();
    }

    @Test
    @DisplayName("delete an author with id")
    void test_delete_when_exists_one_return_successful() {
        when(authorRepository.existsById(anyString())).thenReturn(true);
        doNothing().when(authorRepository).deleteById(any());

        assertDoesNotThrow(()-> authorService.delete(AuthorBuilder.ID));
        verify(authorRepository, times(1)).deleteById(any());
    }

    @Test
    @DisplayName("thrown an exception when not exists author")
    void test_delete_when_not_exists_one_return_fail() {
        when(authorRepository.existsById(anyString())).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, ()-> authorService.delete(AuthorBuilder.ID));
        verify(authorRepository, times(0)).deleteById(any());
    }

    @Test
    @DisplayName("filter author with predicate")
    void test_filter_with_predicate_return_successful() {
        PathBuilder<Author> entityPath = new PathBuilder<>(Author.class, "author");
        StringPath path = entityPath.getString("fullName");
        BooleanExpression fullNameExpression = path.containsIgnoreCase(AuthorBuilder.FULL_NAME);

        when(authorRepository.findAll(any(Predicate.class))).thenReturn(List.of(AuthorBuilder.buildAuthor()));
        assertDoesNotThrow(()-> authorService.filter(fullNameExpression));
    }

}
