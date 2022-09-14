package com.abnamro.recipe.persistance.repository;

import com.abnamro.recipe.persistance.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface AuthorRepository extends JpaRepository<Author, String>, QuerydslPredicateExecutor<Author> {
}
