package com.abnamro.recipe.service;

import com.abnamro.recipe.api.model.AuthorCreateUpdate;
import com.abnamro.recipe.persistance.model.Author;
import com.querydsl.core.types.Predicate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AuthorService {
    Mono<Author> create(AuthorCreateUpdate source);

    Mono<Author> update(String id, AuthorCreateUpdate source);

    Mono<Author> getById(String id);

    Mono<Void> delete(String id);

    Flux<Author> filter(Predicate predicate);
}
