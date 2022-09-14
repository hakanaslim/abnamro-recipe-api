package com.abnamro.recipe.api;

import com.abnamro.recipe.api.model.RecipeCreateUpdate;
import com.abnamro.recipe.api.model.RecipeFilter;
import com.abnamro.recipe.persistance.enums.CourseType;
import com.abnamro.recipe.persistance.enums.SpecialDietType;
import com.abnamro.recipe.persistance.enums.YesNoType;
import com.abnamro.recipe.persistance.model.Recipe;
import com.abnamro.recipe.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
public class RecipeController implements RecipeApi {
    private final RecipeService recipeService;

    @Override
    public Mono<ResponseEntity<Recipe>> createRecipe(RecipeCreateUpdate body) {
        return recipeService.create(body)
                .map(recipe -> new ResponseEntity<>(recipe, HttpStatus.CREATED))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Override
    public Mono<ResponseEntity<Recipe>> updateRecipe(String id, RecipeCreateUpdate body) {
        return recipeService.update(id, body)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Override
    public Mono<ResponseEntity<Recipe>> getRecipe(String id) {
        return recipeService.getById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Override
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteRecipe(String id) {
        return recipeService.delete(id);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public Flux<Recipe> filterRecipe(String name,
                                     CourseType course,
                                     SpecialDietType specialDiet,
                                     YesNoType vegetarian,
                                     String servant,
                                     String ingredient,
                                     String instruction) {
        RecipeFilter filter = buildRecipeFilter(name, course, specialDiet, vegetarian, servant, ingredient, instruction);
        return recipeService.filter(filter);
    }

    private static RecipeFilter buildRecipeFilter(String name, CourseType course, SpecialDietType specialDiet, YesNoType vegetarian, String servant, String ingredient, String instruction) {
        RecipeFilter filter = new RecipeFilter();
        filter.setName(name);
        filter.setCourse(course);
        filter.setServant(servant);
        filter.setIngredient(ingredient);
        if (Objects.nonNull(vegetarian)) {
            filter.setVegetarian(YesNoType.YES.equals(vegetarian));
        }
        filter.setSpecialDiet(specialDiet);
        filter.setInstruction(instruction);
        return filter;
    }
}
