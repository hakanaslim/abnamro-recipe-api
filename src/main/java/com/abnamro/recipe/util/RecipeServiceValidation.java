package com.abnamro.recipe.util;

import com.abnamro.recipe.persistance.model.Recipe;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

@Slf4j
@UtilityClass
public class RecipeServiceValidation {

    public void validate(Recipe source) {
        Assert.hasText(source.getId(), "ID can not be null");
        Assert.hasText(source.getName(), "Name can not be null");
        Assert.hasText(source.getServant(), "Servant can not be null");
        Assert.notNull(source.getCourse(), "Course can not be null");
        Assert.notNull(source.getSpecialDiet(), "SpecialDiet can not be null");
    }
}
