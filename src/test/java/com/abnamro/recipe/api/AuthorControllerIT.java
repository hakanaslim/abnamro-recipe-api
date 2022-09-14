package com.abnamro.recipe.api;

import com.abnamro.recipe.api.model.AuthorCreateUpdate;
import com.abnamro.recipe.api.model.ErrorContext;
import com.abnamro.recipe.consts.AuthorBuilder;
import com.abnamro.recipe.consts.RecipeBuilder;
import com.abnamro.recipe.persistance.model.Author;
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
class AuthorControllerIT {

    @LocalServerPort
    private Integer port;

    @Test
    @DisplayName("create an author")
    void test_create_successful() {
        AuthorCreateUpdate body = AuthorBuilder.buildBody();

        Response response = RestAssured.given()
                .body(body)
                .header("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .header("x-user-id", RecipeBuilder.PERSIST_BY)
                .when()
                .post("http://localhost:" + port + "/author");

        assertEquals(HttpStatus.CREATED.value(), response.statusCode());
        Author author = response.getBody().as(Author.class);
        assertNotNull(author);
        assertNotNull(author.getId());
        assertNotNull(author.getFullName());
        assertNotNull(author.getCreatedDate());
        assertNotNull(author.getCreatedBy());
    }

    @Test
    @DisplayName("create an author with invalid fullName fail")
    void test_create_invalid_param_fail() {
        AuthorCreateUpdate body = AuthorBuilder.buildBody();
        body.setFullName(null);

        Response response = RestAssured.given()
                .body(body)
                .header("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .header("x-user-id", RecipeBuilder.PERSIST_BY)
                .when()
                .post("http://localhost:" + port + "/author");

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
        ErrorContext errorContext = response.body().as(ErrorContext.class);
        assertTrue(errorContext.getMessage().contains("FullName can not be null"));
    }

    @Test
    @DisplayName("update an author with invalid fullName fail")
    void test_update_invalid_param_id_greater_then_36_fail() {
        AuthorCreateUpdate body = AuthorBuilder.buildBody();
        String ID = UUID.randomUUID()+"x";

        Response response = RestAssured.given()
                .body(body)
                .header("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .header("x-user-id", RecipeBuilder.PERSIST_BY)
                .when()
                .patch("http://localhost:" + port + "/author/"+ID);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
        ErrorContext errorContext = response.body().as(ErrorContext.class);
        assertTrue(errorContext.getMessage().contains("id size should less then 36"));
    }

    @Test
    @DisplayName("update an author")
    void test_update_valid_param_successful() {
        AuthorCreateUpdate body = AuthorBuilder.buildBody();

        Response responseCreated = RestAssured.given()
                .body(body)
                .header("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .header("x-user-id", RecipeBuilder.PERSIST_BY)
                .when()
                .post("http://localhost:" + port + "/author");

        Author author = responseCreated.getBody().as(Author.class);
        author.setFullName("New Name");

        Response response = RestAssured.given()
                .body(author)
                .header("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .when()
                .patch("http://localhost:" + port + "/author/"+author.getId());

        assertEquals(HttpStatus.OK.value(), response.statusCode());
        Author updatedAuthor = response.body().as(Author.class);
        assertNotNull(updatedAuthor.getId());
        assertEquals(updatedAuthor.getFullName(), author.getFullName());
        assertNotNull(updatedAuthor.getModifiedDate());
        assertNotNull(updatedAuthor.getModifiedBy());
    }

    @Test
    @DisplayName("get a author")
    void test_get_valid_param_successful() {
        AuthorCreateUpdate body = AuthorBuilder.buildBody();

        Response responseCreated = RestAssured.given()
                .body(body)
                .header("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("http://localhost:" + port + "/author");

        Author author = responseCreated.getBody().as(Author.class);

        Response response = RestAssured.given()
                .header("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("http://localhost:" + port + "/author/"+author.getId());

        assertEquals(HttpStatus.OK.value(), response.statusCode());
        Author responseAuthor = response.body().as(Author.class);
        assertNotNull(responseAuthor.getId());
    }

    @Test
    @DisplayName("get a author with invalid ID fail")
    void test_get_invalid_param_id_successful() {
        String ID = UUID.randomUUID()+"x";

        Response response = RestAssured.given()
                .header("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("http://localhost:" + port + "/author/"+ID);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
        ErrorContext errorContext = response.body().as(ErrorContext.class);
        assertTrue(errorContext.getMessage().contains("id size should less then 36"));
    }

    @Test
    @DisplayName("get a author with invalid ID fail")
    void test_delete_invalid_param_id_successful() {
        AuthorCreateUpdate body = AuthorBuilder.buildBody();

        Response responseCreated = RestAssured.given()
                .body(body)
                .header("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("http://localhost:" + port + "/author");

        Author author = responseCreated.getBody().as(Author.class);

        Response response = RestAssured.given()
                .header("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("http://localhost:" + port + "/author/"+author.getId());

        assertEquals(HttpStatus.NO_CONTENT.value(), response.statusCode());
        String responseBody = response.body().asString();
        assertTrue(StringUtils.isEmpty(responseBody));
    }

    @Test
    @DisplayName("fetch list of author return should return less than zero")
    void test_fetch_list_should_return_less_than_zero_record() {
        Response response = RestAssured.given()
                .header("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("http://localhost:" + port + "/author");

        assertEquals(HttpStatus.OK.value(), response.statusCode());
        List<Author> list = List.of(response.body().as(Author[].class));
        assertFalse(CollectionUtils.isEmpty(list));
    }

    @Test
    @DisplayName("fetch list of author with criteria")
    void test_fetch_list_should_return_less_zero_record() {
        String FULL_NAME = "Mehmet Gurs";

        Response response = RestAssured.given()
                .header("Content-type", MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("http://localhost:" + port + "/author?fullName="+FULL_NAME);

        assertEquals(HttpStatus.OK.value(), response.statusCode());
        List<Author> list = List.of(response.body().as(Author[].class));
        assertFalse(CollectionUtils.isEmpty(list));
        assertEquals(list.iterator().next().getFullName(), FULL_NAME);
    }

}
