package com.abnamro.recipe.service;

import com.abnamro.recipe.api.model.RecipeCreateUpdate;
import com.abnamro.recipe.api.model.RecipeFilter;
import com.abnamro.recipe.consts.RecipeBuilder;
import com.abnamro.recipe.filter.RequestHeaderStore;
import com.abnamro.recipe.persistance.enums.SpecialDietType;
import com.abnamro.recipe.persistance.model.Recipe;
import com.abnamro.recipe.persistance.repository.RecipeRepository;
import com.querydsl.core.types.Predicate;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class RecipeServiceTests {

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private RequestHeaderStore requestHeaderStore;

    private RecipeServiceImpl recipeService;

    @BeforeEach
    void setUp() {
        recipeService = new RecipeServiceImpl(recipeRepository, requestHeaderStore);
    }

    @Test
    @DisplayName("create a recipe")
    void test_create_successful() {
        RecipeCreateUpdate body = RecipeBuilder.buildBody();

        when(requestHeaderStore.getUserID()).thenReturn(RecipeBuilder.PERSIST_BY);
        when(recipeRepository.save(any(Recipe.class))).thenReturn(RecipeBuilder.buildRecipe());
        assertDoesNotThrow(()-> recipeService.create(body));
    }

    @Test
    @DisplayName("create a recipe when invalid param fail")
    void test_create_with_invalid_param_fail() {
        RecipeCreateUpdate bodyName = RecipeBuilder.buildBody();
        bodyName.setName(null);
        when(requestHeaderStore.getUserID()).thenReturn(RecipeBuilder.PERSIST_BY);

        var ex = assertThrows(IllegalArgumentException.class, ()-> recipeService.create(bodyName));
        assertTrue(ex.getMessage().contains("Name can not be null"));
        verify(recipeRepository, times(0)).save(any());

        RecipeCreateUpdate bodyServant = RecipeBuilder.buildBody();
        bodyServant.setServant(null);
        ex = assertThrows(IllegalArgumentException.class, ()-> recipeService.create(bodyServant));
        assertTrue(ex.getMessage().contains("Servant can not be null"));
        verify(recipeRepository, times(0)).save(any());

        RecipeCreateUpdate bodyCourse = RecipeBuilder.buildBody();
        bodyCourse.setCourse(null);
        ex = assertThrows(IllegalArgumentException.class, ()-> recipeService.create(bodyCourse));
        assertTrue(ex.getMessage().contains("Course can not be null"));
        verify(recipeRepository, times(0)).save(any());

        RecipeCreateUpdate bodySpecialDiet = RecipeBuilder.buildBody();
        bodySpecialDiet.setSpecialDiet(null);
        ex = assertThrows(IllegalArgumentException.class, ()-> recipeService.create(bodySpecialDiet));
        assertTrue(ex.getMessage().contains("SpecialDiet can not be null"));
        verify(recipeRepository, times(0)).save(any());
    }

    @Test
    @DisplayName("update a recipe")
    void test_update_successful() {
        RecipeCreateUpdate body = RecipeBuilder.buildBody();
        String ID = UUID.randomUUID().toString();

        when(requestHeaderStore.getUserID()).thenReturn(RecipeBuilder.PERSIST_BY);
        when(recipeRepository.existsById(anyString())).thenReturn(true);
        when(recipeRepository.save(any(Recipe.class))).thenReturn(RecipeBuilder.buildRecipe());
        assertDoesNotThrow(()-> recipeService.update(ID, body));
    }

    @Test
    @DisplayName("update a recipe - thrown exception when it is not exists")
    void test_update_nonExists_fail() {
        RecipeCreateUpdate body = RecipeBuilder.buildBody();
        String ID = UUID.randomUUID().toString();

        when(recipeRepository.existsById(anyString())).thenReturn(false);
        StepVerifier
                .create(recipeService.update(ID, body))
                .expectErrorMatches(throwable -> throwable.getMessage().contains("Recipe not found with id"))
                .verify();
        verify(recipeRepository, times(0)).save(any(Recipe.class));
    }

    @Test
    @DisplayName("get a recipe successful")
    void test_getById_successful() {
        when(recipeRepository.findById(anyString())).thenReturn(Optional.of(RecipeBuilder.buildRecipe()));

        assertDoesNotThrow(()-> recipeService.getById(RecipeBuilder.ID));
    }

    @Test
    @DisplayName("get a recipe when non existing id thrown exception")
    void test_get_with_noExists_id_should_thrown_exception() {
        when(recipeRepository.findById(anyString())).thenReturn(Optional.empty());

        StepVerifier.create(recipeService.getById(RecipeBuilder.ID))
                .expectErrorMatches(throwable -> throwable.getMessage().contains("Recipe not found with id"))
                .verify();
    }

    @Test
    @DisplayName("delete an recipe with id")
    void test_delete_when_exists_one_return_successful() {
        when(recipeRepository.existsById(anyString())).thenReturn(true);
        doNothing().when(recipeRepository).deleteById(any());

        StepVerifier
                .create(recipeService.delete(RecipeBuilder.ID))
                .verifyComplete();
        verify(recipeRepository, times(1)).deleteById(any());
    }

    @Test
    @DisplayName("thrown an exception when not exists recipe")
    void test_delete_when_not_exists_one_return_fail() {
        when(recipeRepository.existsById(anyString())).thenReturn(false);

        StepVerifier
                .create(recipeService.delete(RecipeBuilder.ID))
                .expectErrorMatches(throwable -> throwable.getMessage().contains("Recipe not found with id"))
                .verify();
        verify(recipeRepository, times(0)).deleteById(any());
    }

    @Test
    @DisplayName("filter recipe with predicate")
    void test_filter_with_predicate_return_successful() {
        RecipeFilter recipeFilter = new RecipeFilter();
        recipeFilter.setName(RecipeBuilder.NAME);

        when(recipeRepository.findAll(any(Predicate.class))).thenReturn(List.of(RecipeBuilder.buildRecipe()));
        StepVerifier
                .create(recipeService.filter(recipeFilter))
                .assertNext(actual -> {
                    assertNotNull(actual);
                    assertNotNull(actual.getId());
                    assertNotNull(actual.getName());
                    assertNotNull(actual.getCourse());
                    assertNotNull(actual.getServant());
                    assertNotNull(actual.getSpecialDiet());
                }).verifyComplete();
    }

    @Test
    @DisplayName("filter recipe with vegetarian")
    void test_filter_with_vegetarian_return_successful() {
        RecipeFilter recipeFilter = new RecipeFilter();
        recipeFilter.setVegetarian(true);

        when(recipeRepository.findAll(any(Predicate.class))).thenReturn(List.of(RecipeBuilder.buildRecipe()));
        StepVerifier
                .create(recipeService.filter(recipeFilter))
                .assertNext(actual -> {
                    assertNotNull(actual);
                    assertNotNull(actual.getId());
                    assertTrue(actual.getSpecialDiet().equals(SpecialDietType.VEGETARIAN));
                }).verifyComplete();
    }

    @Test
    @DisplayName("filter recipe with non vegetarian")
    void test_filter_with_non_vegetarian_return_successful() {
        RecipeFilter recipeFilter = new RecipeFilter();
        recipeFilter.setVegetarian(false);

        Recipe recipe = RecipeBuilder.buildRecipe();
        recipe.setSpecialDiet(SpecialDietType.NO_DIET);
        when(recipeRepository.findAll(any(Predicate.class))).thenReturn(List.of(recipe));
        StepVerifier
                .create(recipeService.filter(recipeFilter))
                .assertNext(actual -> {
                    assertNotNull(actual);
                    assertNotNull(actual.getId());
                    assertFalse(actual.getSpecialDiet().equals(SpecialDietType.VEGETARIAN));
                }).verifyComplete();
    }

    @Test
    @DisplayName("filter recipe with no filter")
    void test_filter_with_no_filter_return_successful() {
        RecipeFilter recipeFilter = new RecipeFilter();

        Recipe recipe = RecipeBuilder.buildRecipe();
        recipe.setSpecialDiet(SpecialDietType.NO_DIET);
        when(recipeRepository.findAll()).thenReturn(List.of(recipe));
        StepVerifier
                .create(recipeService.filter(recipeFilter))
                .assertNext(actual -> {
                    assertNotNull(actual);
                }).verifyComplete();
    }

}
