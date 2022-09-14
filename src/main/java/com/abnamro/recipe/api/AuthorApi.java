package com.abnamro.recipe.api;

import com.abnamro.recipe.api.model.AuthorCreateUpdate;
import com.abnamro.recipe.persistance.model.Author;
import com.querydsl.core.types.Predicate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Validated
@RequestMapping(value = "/author")
public interface AuthorApi {

    @PostMapping(produces = {"application/json"}, consumes = {"application/json"})
    @Operation(description = "Create an Author", summary = "This operation creates an Author.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "405", description = "Method Not allowed"),
            @ApiResponse(responseCode = "409", description = "Conflict"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    Mono<ResponseEntity<Author>> createAuthor(@RequestBody @NotNull AuthorCreateUpdate body);

    @PatchMapping(value = "/{id}",
            produces = {"application/json"},
            consumes = {"application/json"})
    @Operation(description = "Update an Author", summary = "This operation updates an Author.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Updated"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "405", description = "Method Not allowed"),
            @ApiResponse(responseCode = "409", description = "Conflict"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    Mono<ResponseEntity<Author>> updateAuthor(@PathVariable("id")
                                              @Size(max = 36, message = "id size should less then 36")
                                              @NotBlank(message = "id can not be blank") String id,
                                              @RequestBody AuthorCreateUpdate body);

    @GetMapping(value = "/{id}",
            consumes = {"application/json"})
    @Operation(description = "Get an Author", summary = "This operation gets an Author entity by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "405", description = "Method Not allowed"),
            @ApiResponse(responseCode = "409", description = "Conflict"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    Mono<ResponseEntity<Author>> getAuthor(@PathVariable("id")
                                           @Size(max = 36, message = "id size should less then 36")
                                           @NotBlank(message = "id can not be blank") String id);

    @DeleteMapping(value = "/{id}",
            produces = {"application/json"})
    @Operation(description = "Delete an Author", summary = "This operation deletes an Author entity by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Delete"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "405", description = "Method Not allowed"),
            @ApiResponse(responseCode = "409", description = "Conflict"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    Mono<Void> deleteAuthor(@PathVariable("id")
                            @Size(max = 36, message = "id size should less then 36")
                            @NotBlank(message = "id can not be blank") String id);

    @GetMapping(produces = {"application/json"})
    @Operation(description = "fetch list of Author", summary = "This operation fetches list of Author.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fetch"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "405", description = "Method Not allowed"),
            @ApiResponse(responseCode = "409", description = "Conflict"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    Flux<Author> listAuthor(@QuerydslPredicate(root = Author.class) Predicate predicate);
}
