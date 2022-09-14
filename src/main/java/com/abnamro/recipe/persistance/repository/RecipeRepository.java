package com.abnamro.recipe.persistance.repository;

import com.abnamro.recipe.persistance.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface RecipeRepository extends JpaRepository<Recipe, String>, QuerydslPredicateExecutor<Recipe> {

}
