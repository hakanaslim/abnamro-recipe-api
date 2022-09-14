package com.abnamro.recipe.api;

import com.abnamro.recipe.api.model.RecipeCreateUpdate;
import com.abnamro.recipe.persistance.enums.CourseType;
import com.abnamro.recipe.persistance.enums.SpecialDietType;
import com.abnamro.recipe.persistance.enums.YesNoType;
import com.abnamro.recipe.persistance.model.Recipe;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Validated
@RequestMapping(value = "/recipe")
public interface RecipeApi {

    @PostMapping(produces = {"application/json;charset=utf-8"}, consumes = {"application/json;charset=utf-8"})
    @Operation(description = "Create an Recipe", summary = "This operation creates an Recipe entity.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "405", description = "Method Not allowed"),
            @ApiResponse(responseCode = "409", description = "Conflict"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    Mono<ResponseEntity<Recipe>> createRecipe(@RequestBody RecipeCreateUpdate body);

    @PatchMapping(value = "/{id}",
            produces = {"application/json;charset=utf-8"},
            consumes = {"application/merge-patch+json;charset=utf-8","application/json;charset=utf-8"})
    @Operation(description = "Update an Recipe", summary = "This operation updates an Recipe entity.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Updated"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "405", description = "Method Not allowed"),
            @ApiResponse(responseCode = "409", description = "Conflict"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    Mono<ResponseEntity<Recipe>> updateRecipe(@PathVariable("id")
                                              @Size(max = 36, message = "id size should less then 36")
                                              @NotBlank(message = "id can not be blank") String id,
                                              @RequestBody RecipeCreateUpdate body);

    @GetMapping(value = "/{id}")
    @Operation(description = "Get an Recipe", summary = "This operation gets an Recipe entity by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "405", description = "Method Not allowed"),
            @ApiResponse(responseCode = "409", description = "Conflict"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    Mono<ResponseEntity<Recipe>> getRecipe(@PathVariable("id")
                                           @Size(max = 36, message = "id size should less then 36")
                                           @NotBlank(message = "id can not be blank") String id);

    @DeleteMapping(value = "/{id}")
    @Operation(description = "Delete an Recipe", summary = "This operation deletes an Recipe entity by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Delete"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "405", description = "Method Not allowed"),
            @ApiResponse(responseCode = "409", description = "Conflict"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    Mono<Void> deleteRecipe(@PathVariable("id") String id);

    @GetMapping(value = "/filter")
    @Operation(description = "Filter list of Recipe", summary = "This operation filter list of Recipe entity.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Filter"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "405", description = "Method Not allowed"),
            @ApiResponse(responseCode = "409", description = "Conflict"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    Flux<Recipe> filterRecipe(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "course", required = false) CourseType course,
            @RequestParam(value = "specialDiet", required = false) SpecialDietType specialDiet,
            @RequestParam(value = "vegetarian", required = false) YesNoType vegetarian,
            @RequestParam(value = "servant", required = false) String servant,
            @RequestParam(value = "ingredient", required = false) String ingredient,
            @RequestParam(value = "instruction", required = false) String instruction
    );

}
