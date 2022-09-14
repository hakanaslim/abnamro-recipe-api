package com.abnamro.recipe.service;

import com.abnamro.recipe.api.model.RecipeCreateUpdate;
import com.abnamro.recipe.api.model.RecipeFilter;
import com.abnamro.recipe.consts.RecipeBuilder;
import com.abnamro.recipe.persistance.model.Recipe;
import com.abnamro.recipe.persistance.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ActiveProfiles("junit")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RecipeServiceIT {

    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private RecipeServiceImpl recipeService;

    @BeforeEach
    void setUp() {
        recipeRepository.deleteAll();
    }

    @Test
    @DisplayName("create an recipe")
    void test_create_successful() {
        RecipeCreateUpdate body = RecipeBuilder.buildBody();

        StepVerifier.create(recipeService.create(body))
                .assertNext(actual -> {
                    assertThat(actual).isNotNull();
                    assertThat(actual.getId()).isNotNull();
                    assertThat(actual.getName()).isNotNull();
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("find an recipe")
    void test_getById_successful() {
        RecipeCreateUpdate body = RecipeBuilder.buildBody();
        Recipe recipe = recipeService.create(body).block();

        StepVerifier
                .create(recipeService.getById(recipe.getId()))
                .assertNext(actual -> {
                    assertThat(actual).isNotNull();
                    assertThat(actual.getId()).isNotNull();
                    assertThat(actual.getName()).isNotNull();
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Recipe not found fail")
    void test_getById_thrown_notFoundException_when_not_exists_fail() {
        StepVerifier
                .create(recipeService.getById(RecipeBuilder.ID))
                .expectErrorMatches(throwable -> throwable.getMessage().contains("Recipe not found with id"))
                .verify();
    }

    @Test
    @DisplayName("Delete exists recipe")
    void test_delete_successful() {
        RecipeCreateUpdate body = RecipeBuilder.buildBody();
        Recipe recipe = recipeService.create(body).block();

        assertDoesNotThrow(()-> recipeService.delete(recipe.getId()));
    }

    @Test
    @DisplayName("Throw exception while deleting not exists recipe")
    void test_delete_thrown_notFoundException_fail() {
        StepVerifier
                .create(recipeService.delete(RecipeBuilder.ID))
                .expectErrorMatches(throwable -> throwable.getMessage().contains("Recipe not found with id"))
                .verify();
    }

    @Test
    @DisplayName("filter recipe with predicate")
    void test_filter_with_predicate_return_successful() {
        RecipeCreateUpdate body = RecipeBuilder.buildBody();
        Recipe recipe = recipeService.create(body).block();

        RecipeFilter filter = new RecipeFilter();
        recipe.setName(recipe.getName());

        StepVerifier.create(
                recipeService.filter(filter))
                .assertNext(actual -> {
                    assertThat(actual).isNotNull();
                    assertThat(actual.getId()).isNotNull();
                    assertThat(actual.getName()).isNotNull();
                })
                .expectNextCount(0)
                .verifyComplete();
    }

}
