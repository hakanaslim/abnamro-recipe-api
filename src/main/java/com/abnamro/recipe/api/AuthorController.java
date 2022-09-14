package com.abnamro.recipe.api;

import com.abnamro.recipe.api.model.AuthorCreateUpdate;
import com.abnamro.recipe.persistance.model.Author;
import com.abnamro.recipe.service.AuthorService;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class AuthorController implements AuthorApi {
    private final AuthorService authorService;

    @Override
    public Mono<ResponseEntity<Author>> createAuthor(AuthorCreateUpdate body) {
        return authorService.create(body)
                .map(recipe -> new ResponseEntity<>(recipe, HttpStatus.CREATED))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Override
    public Mono<ResponseEntity<Author>> updateAuthor(String id, AuthorCreateUpdate body) {
        return authorService.update(id, body)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Override
    public Mono<ResponseEntity<Author>> getAuthor(String id) {
        return authorService.getById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Override
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteAuthor(String id) {
        return authorService.delete(id);
    }

    @Override
    public Flux<Author> listAuthor(Predicate predicate) {
        return authorService.filter(predicate);
    }
}
