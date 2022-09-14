package com.abnamro.recipe.util;

import com.abnamro.recipe.persistance.model.Author;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

@Slf4j
@UtilityClass
public class AuthorServiceValidation {

    public void validate(Author source) {
        Assert.hasText(source.getId(), "ID can not be null");
        Assert.hasText(source.getFullName(), "FullName can not be null");
    }
}
