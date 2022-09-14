package com.abnamro.recipe.service;

import com.abnamro.recipe.api.model.RecipeCreateUpdate;
import com.abnamro.recipe.api.model.RecipeFilter;
import com.abnamro.recipe.persistance.model.Recipe;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RecipeService {
    Mono<Recipe> create(RecipeCreateUpdate source);

    Mono<Recipe> update(String id, RecipeCreateUpdate source);

    Mono<Recipe> getById(String id);

    Mono<Void> delete(String id);

    Flux<Recipe> filter(RecipeFilter filter);
}
