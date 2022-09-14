package com.abnamro.recipe.api;

import com.abnamro.recipe.api.model.ErrorContext;
import com.abnamro.recipe.api.model.RecipeCreateUpdate;
import com.abnamro.recipe.consts.RecipeBuilder;
import com.abnamro.recipe.persistance.model.Recipe;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("junit")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext
@AutoConfigureTestEntityManager
@Order(100)
class RecipeControllerIT {

    @LocalServerPort
    private Integer port;

    @Test
    @DisplayName("create an recipe")
    void test_create_successful() {
        RecipeCreateUpdate body = RecipeBuilder.buildBody();

        Response response = RestAssured.given()
                .body(body)
                .header("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .header("x-user-id", RecipeBuilder.PERSIST_BY)
                .when()
                .post("http://localhost:" + port + "/recipe");

        assertEquals(HttpStatus.CREATED.value(), response.statusCode());
        Recipe recipe = response.getBody().as(Recipe.class);
        assertNotNull(recipe);
        assertNotNull(recipe.getId());
        assertNotNull(recipe.getName());
        assertNotNull(recipe.getCreatedDate());
        assertNotNull(recipe.getCreatedBy());
    }

    @Test
    @DisplayName("create an recipe with invalid name fail")
    void test_create_invalid_param_fail() {
        RecipeCreateUpdate body = RecipeBuilder.buildBody();
        body.setName(null);

        Response response = RestAssured.given()
                .body(body)
                .header("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .header("x-user-id", RecipeBuilder.PERSIST_BY)
                .when()
                .post("http://localhost:" + port + "/recipe");

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
        ErrorContext errorContext = response.body().as(ErrorContext.class);
        assertTrue(errorContext.getMessage().contains("Name can not be null"));
    }

    @Test
    @DisplayName("update an recipe with invalid name fail")
    void test_update_invalid_param_id_greater_then_36_fail() {
        RecipeCreateUpdate body = RecipeBuilder.buildBody();
        final String ID = UUID.randomUUID()+"x";

        Response response = RestAssured.given()
                .body(body)
                .header("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .header("x-user-id", RecipeBuilder.PERSIST_BY)
                .when()
                .patch("http://localhost:" + port + "/recipe/"+ID);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
        ErrorContext errorContext = response.body().as(ErrorContext.class);
        assertTrue(errorContext.getMessage().contains("id size should less then 36"));
    }

    @Test
    @DisplayName("update an recipe")
    void test_update_valid_param_successful() {
        RecipeCreateUpdate body = RecipeBuilder.buildBody();

        Response responseCreated = RestAssured.given()
                .body(body)
                .header("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .header("x-user-id", RecipeBuilder.PERSIST_BY)
                .when()
                .post("http://localhost:" + port + "/recipe");

        Recipe recipe = responseCreated.getBody().as(Recipe.class);
        recipe.setName("New Name");

        Response response = RestAssured.given()
                .body(recipe)
                .header("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .header("x-user-id", RecipeBuilder.PERSIST_BY)
                .when()
                .patch("http://localhost:" + port + "/recipe/"+recipe.getId());

        assertEquals(HttpStatus.OK.value(), response.statusCode());
        Recipe updatedRecipe = response.body().as(Recipe.class);
        assertNotNull(updatedRecipe.getId());
        assertEquals(updatedRecipe.getName(), recipe.getName());
        assertNotNull(updatedRecipe.getModifiedDate());
        assertNotNull(updatedRecipe.getModifiedBy());
    }

    @Test
    @DisplayName("gets an recipe")
    void test_get_valid_param_successful() {
        RecipeCreateUpdate body = RecipeBuilder.buildBody();

        Response responseCreated = RestAssured.given()
                .body(body)
                .header("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("http://localhost:" + port + "/recipe");

        Recipe recipe = responseCreated.getBody().as(Recipe.class);

        Response response = RestAssured.given()
                .header("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("http://localhost:" + port + "/recipe/"+recipe.getId());

        assertEquals(HttpStatus.OK.value(), response.statusCode());
        Recipe responseRecipe = response.body().as(Recipe.class);
        assertNotNull(responseRecipe.getId());
    }

    @Test
    @DisplayName("get a recipe with invalid ID fail")
    void test_get_invalid_param_id_successful() {
        String ID = UUID.randomUUID()+"x";

        Response response = RestAssured.given()
                .header("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("http://localhost:" + port + "/recipe/"+ID);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
        ErrorContext errorContext = response.body().as(ErrorContext.class);
        assertTrue(errorContext.getMessage().contains("id size should less then 36"));
    }

    @Test
    @DisplayName("get a recipe with invalid ID fail")
    void test_delete_invalid_param_id_successful() {
        RecipeCreateUpdate body = RecipeBuilder.buildBody();

        Response responseCreated = RestAssured.given()
                .body(body)
                .header("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("http://localhost:" + port + "/recipe");

        Recipe recipe = responseCreated.getBody().as(Recipe.class);

        Response response = RestAssured.given()
                .header("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("http://localhost:" + port + "/recipe/"+recipe.getId());

        assertEquals(HttpStatus.NO_CONTENT.value(), response.statusCode());
        String responseBody = response.body().asString();
        assertTrue(StringUtils.isEmpty(responseBody));
    }

    @Test
    @DisplayName("fetch list of recipe return should return less than zero")
    void test_fetch_list_should_return_less_than_zero_record() {
        Response response = RestAssured.given()
                .header("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("http://localhost:" + port + "/recipe/filter");

        assertEquals(HttpStatus.OK.value(), response.statusCode());
        List<Recipe> list = List.of(response.body().as(Recipe[].class));
        assertFalse(CollectionUtils.isEmpty(list));
    }

    @Test
    @DisplayName("fetch list of recipe with criteria (name=Baklava)")
    void test_fetch_list_should_return_less_zero_record() {
        final String NAME = "Baklava";

        Response response = RestAssured.given()
                .header("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("http://localhost:" + port + "/recipe/filter?name="+NAME);

        assertEquals(HttpStatus.OK.value(), response.statusCode());
        List<Recipe> list = List.of(response.body().as(Recipe[].class));
        assertFalse(CollectionUtils.isEmpty(list));
        assertEquals(NAME, list.iterator().next().getName());
    }

    @Test
    @DisplayName("fetch list of recipe with criteria (vegetarian=YES)")
    void test_fetch_list_should_return_list() {
        final String VEGETARIAN = "YES";

        Response response = RestAssured.given()
                .header("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("http://localhost:" + port + "/recipe/filter?vegetarian="+VEGETARIAN);

        assertEquals(HttpStatus.OK.value(), response.statusCode());
        List<Recipe> list = List.of(response.body().as(Recipe[].class));
        assertFalse(CollectionUtils.isEmpty(list));
    }

    @Test
    @DisplayName("fetch list of recipe with criteria (vegetarian=NO)")
    void test_fetch_list_should_not_return_any_record() {
        final String VEGETARIAN = "NO";

        Response response = RestAssured.given()
                .header("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("http://localhost:" + port + "/recipe/filter?vegetarian="+VEGETARIAN);

        assertEquals(HttpStatus.OK.value(), response.statusCode());
        List<Recipe> list = List.of(response.body().as(Recipe[].class));
        assertTrue(CollectionUtils.isEmpty(list));
    }

    @Test
    @DisplayName("fetch list of recipe with criteria (servant=1)")
    void test_fetch_list_with_servant_should_return_list() {
        final String SERVANT = "1";

        Response response = RestAssured.given()
                .header("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("http://localhost:" + port + "/recipe/filter?servant="+SERVANT);

        assertEquals(HttpStatus.OK.value(), response.statusCode());
        List<Recipe> list = List.of(response.body().as(Recipe[].class));
        assertFalse(CollectionUtils.isEmpty(list));
    }

    @Test
    @DisplayName("fetch list of recipe with criteria (ingredient=ice;juice)")
    void test_fetch_list_with_ingredient_should_return_list() {
        final String INGREDIENT = "ice;juice";

        Response response = RestAssured.given()
                .header("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("http://localhost:" + port + "/recipe/filter?ingredient="+INGREDIENT);

        assertEquals(HttpStatus.OK.value(), response.statusCode());
        List<Recipe> list = List.of(response.body().as(Recipe[].class));
        assertFalse(CollectionUtils.isEmpty(list));
    }

    @Test
    @DisplayName("fetch list of recipe with criteria (ingredient=-ice)")
    void test_fetch_list_with_ingredient_should_return_empty_list() {
        final String INGREDIENT = "-ice";

        Response response = RestAssured.given()
                .header("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("http://localhost:" + port + "/recipe/filter?ingredient="+INGREDIENT);

        assertEquals(HttpStatus.OK.value(), response.statusCode());
        List<Recipe> list = List.of(response.body().as(Recipe[].class));
        assertFalse(CollectionUtils.isEmpty(list));
    }

    @Test
    @DisplayName("fetch list of recipe with criteria (instruction=-mixture)")
    void test_fetch_list_with_instruction_should_return_list() {
        final String INSTRUCTION = "mixture";

        Response response = RestAssured.given()
                .header("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("http://localhost:" + port + "/recipe/filter?instruction="+INSTRUCTION);

        assertEquals(HttpStatus.OK.value(), response.statusCode());
        List<Recipe> list = List.of(response.body().as(Recipe[].class));
        assertFalse(CollectionUtils.isEmpty(list));
    }

    @Test
    @DisplayName("fetch list of recipe with criteria (instruction=-mixture)")
    void test_fetch_list_with_instruction_should_return_empty_list() {
        final String INSTRUCTION = "-mixture";

        Response response = RestAssured.given()
                .header("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("http://localhost:" + port + "/recipe/filter?instruction="+INSTRUCTION);

        assertEquals(HttpStatus.OK.value(), response.statusCode());
        List<Recipe> list = List.of(response.body().as(Recipe[].class));
        assertTrue(CollectionUtils.isEmpty(list));
    }

}
