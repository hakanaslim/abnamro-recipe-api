package com.abnamro.recipe.service;

import com.abnamro.recipe.api.model.AuthorCreateUpdate;
import com.abnamro.recipe.exception.ResourceNotFoundException;
import com.abnamro.recipe.filter.RequestHeaderStore;
import com.abnamro.recipe.persistance.mapper.AuthorMapper;
import com.abnamro.recipe.persistance.model.Author;
import com.abnamro.recipe.persistance.repository.AuthorRepository;
import com.abnamro.recipe.util.AuthorServiceValidation;
import com.abnamro.recipe.util.PersistenceUtil;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final RequestHeaderStore store;

    @Override
    public Mono<Author> create(AuthorCreateUpdate source) {
        log.info("AuthorService -> createAuthor started! {}", source.getClass().getName());
        Author author = AuthorMapper.INSTANCE.fromRequest(source);
        author.setId(UUID.randomUUID().toString());
        AuthorServiceValidation.validate(author);
        PersistenceUtil.setOnPrePersist(author, store.getUserID());
        Author saved = authorRepository.save(author);
        return Mono.just(saved);
    }

    @Override
    public Mono<Author> update(String id, AuthorCreateUpdate source) {
        log.info("AuthorService -> updateAuthor started! {}", source.getClass().getName());
        validateIfNotExists(id);
        Author author = AuthorMapper.INSTANCE.fromRequest(source);
        author.setId(id);
        AuthorServiceValidation.validate(author);
        PersistenceUtil.setOnPreUpdate(author, store.getUserID());
        Author saved = authorRepository.save(author);
        return Mono.just(saved);
    }

    private void validateIfNotExists(String id) {
        if (!isExistsById(id)){
            throw new ResourceNotFoundException("Author not found with id :"+ id);
        }
    }

    private boolean isExistsById(String id) {
        return authorRepository.existsById(id);
    }

    @Override
    public Mono<Author> getById(String id) {
        Optional<Author> authorOptional = authorRepository.findById(id);
        if(authorOptional.isEmpty()) {
            return Mono.error(resourceNotFoundByIdException(id));
        }
        return Mono.just(authorOptional.get());
    }

    private ResourceNotFoundException resourceNotFoundByIdException(String id) {
        return new ResourceNotFoundException("Author not found with id: " + id);
    }

    @Override
    public Mono<Void> delete(String id) {
        if (isExistsById(id)){
            authorRepository.deleteById(id);
            return Mono.empty().then();
        }
        throw resourceNotFoundByIdException(id);
    }

    @Override
    public Flux<Author> filter(Predicate predicate) {
        BooleanBuilder booleanBuilder = new BooleanBuilder(predicate);
        Iterable<Author> authors = authorRepository.findAll(booleanBuilder);

        return Flux.fromIterable(authors);
    }
}
