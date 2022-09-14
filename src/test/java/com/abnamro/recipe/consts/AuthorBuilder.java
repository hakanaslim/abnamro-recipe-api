package com.abnamro.recipe.consts;

import com.abnamro.recipe.api.model.AuthorCreateUpdate;
import com.abnamro.recipe.persistance.model.Author;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class AuthorBuilder {
    public static final String ID = UUID.randomUUID().toString();
    public static final String FULL_NAME = "HAKAN ASLIM";

    public AuthorCreateUpdate buildBody() {
        AuthorCreateUpdate body = new AuthorCreateUpdate();
        body.setFullName(FULL_NAME);
        return body;
    }

    public Author buildAuthor() {
        Author author = new Author();
        author.setId(ID);
        author.setFullName(FULL_NAME);
        return author;
    }

}
