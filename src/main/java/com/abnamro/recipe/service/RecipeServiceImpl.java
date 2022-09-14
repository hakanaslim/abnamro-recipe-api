package com.abnamro.recipe.service;

import com.abnamro.recipe.api.model.RecipeCreateUpdate;
import com.abnamro.recipe.api.model.RecipeFilter;
import com.abnamro.recipe.exception.ResourceNotFoundException;
import com.abnamro.recipe.filter.RequestHeaderStore;
import com.abnamro.recipe.persistance.mapper.RecipeMapper;
import com.abnamro.recipe.persistance.model.Recipe;
import com.abnamro.recipe.persistance.repository.RecipeRepository;
import com.abnamro.recipe.util.PersistenceUtil;
import com.abnamro.recipe.util.RecipePredicateUtil;
import com.abnamro.recipe.util.RecipeServiceValidation;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final RequestHeaderStore requestHeaderStore;

    @Override
    @Transactional
    public Mono<Recipe> create(RecipeCreateUpdate source) {
        log.info("RecipeService -> createRecipe started! {}", source.getClass().getName());
        Recipe recipe = RecipeMapper.INSTANCE.fromRequest(source);
        recipe.setId(UUID.randomUUID().toString());
        PersistenceUtil.setOnPrePersist(recipe, requestHeaderStore.getUserID());
        PersistenceUtil.fillParentProperties(recipe);
        RecipeServiceValidation.validate(recipe);
        Recipe saved = recipeRepository.save(recipe);
        return Mono.just(saved);
    }

    @Override
    @Transactional
    public Mono<Recipe> update(String id, RecipeCreateUpdate source) {
        try {
            log.info("RecipeService -> updateRecipe started! {}", source.getClass().getName());
            validateIfNotExists(id);
            Recipe recipe = RecipeMapper.INSTANCE.fromRequest(source);
            recipe.setId(id);
            PersistenceUtil.setOnPreUpdate(recipe, requestHeaderStore.getUserID());
            PersistenceUtil.fillParentProperties(recipe);
            RecipeServiceValidation.validate(recipe);
            Recipe saved = recipeRepository.save(recipe);
            return Mono.just(saved);
        } catch (ResourceNotFoundException rex) {
            return Mono.error(new ResourceNotFoundException(rex.getMessage()));
        }
    }

    private void validateIfNotExists(String id) {
        if (!isExistsById(id)){
            throw new ResourceNotFoundException("Recipe not found with id :"+ id);
        }
    }

    private boolean isExistsById(String id) {
        return recipeRepository.existsById(id);
    }

    @Override
    public Mono<Recipe> getById(String id) {
        Optional<Recipe> recipe = recipeRepository.findById(id);
        if (recipe.isEmpty()) {
            return Mono.error(resourceNotFoundByIdException(id));
        }
        return Mono.just(recipe.get());
    }

    private ResourceNotFoundException resourceNotFoundByIdException(String id) {
        return new ResourceNotFoundException("Recipe not found with id: " + id);
    }

    @Override
    @Transactional
    public Mono<Void> delete(String id) {
        if (isExistsById(id)){
            recipeRepository.deleteById(id);
            return Mono.empty().then();
        }
        return Mono.error(resourceNotFoundByIdException(id));
    }

    @Override
    public Flux<Recipe> filter(RecipeFilter filter) {
        BooleanExpression predicate = RecipePredicateUtil.toPredicate(filter);
        if (Objects.isNull(predicate)) {
            return Flux.fromIterable(recipeRepository.findAll());
        }
        Iterable<Recipe> recipes = recipeRepository.findAll(predicate);
        return Flux.fromIterable(recipes);
    }

}
