package com.abnamro.recipe.consts;

import com.abnamro.recipe.api.model.RecipeCreateUpdate;
import com.abnamro.recipe.persistance.enums.CourseType;
import com.abnamro.recipe.persistance.enums.SpecialDietType;
import com.abnamro.recipe.persistance.mapper.RecipeMapper;
import com.abnamro.recipe.persistance.model.Recipe;
import com.abnamro.recipe.persistance.model.RecipeIngredient;
import com.abnamro.recipe.persistance.model.RecipeInstruction;
import lombok.experimental.UtilityClass;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.UUID;

@UtilityClass
public class RecipeBuilder {
    public static final String ID = UUID.randomUUID().toString();
    public static final String NAME = "Test Recipe";
    public static final String DESCRIPTION = "Test Recipe";
    public static final String PERSIST_BY = "admin";
    public static final String SERVANT = "1 person";
    public static final CourseType COURSE_TYPE = CourseType.MAIN_DISH;
    public static final SpecialDietType SPECIAL_DIET = SpecialDietType.VEGETARIAN;

    public RecipeCreateUpdate buildBody() {
        RecipeCreateUpdate recipeCreateUpdate = new RecipeCreateUpdate();
        recipeCreateUpdate.setName(RecipeBuilder.NAME);
        recipeCreateUpdate.setDescription(RecipeBuilder.DESCRIPTION);
        recipeCreateUpdate.setServant(RecipeBuilder.SERVANT);
        recipeCreateUpdate.setCourse(RecipeBuilder.COURSE_TYPE);
        recipeCreateUpdate.setSpecialDiet(RecipeBuilder.SPECIAL_DIET);
        recipeCreateUpdate.setRecipeIngredients(List.of(buildRecipeIngredient("Ice"), buildRecipeIngredient("Fruit")));
        recipeCreateUpdate.setRecipeInstructions(List.of(buildRecipeInstruction("First Step"), buildRecipeInstruction("Second Step")));
        return recipeCreateUpdate;
    }
    public Recipe buildRecipe() {
        Recipe recipe = RecipeMapper.INSTANCE.fromRequest(buildBody());
        recipe.setId(RecipeBuilder.ID);
        return recipe;
    }

    @NonNull
    public RecipeIngredient buildRecipeIngredient(String text) {
        RecipeIngredient ingredient = new RecipeIngredient();
        ingredient.setText(text);
        return ingredient;
    }

    @NonNull
    public RecipeInstruction buildRecipeInstruction(String text) {
        RecipeInstruction instruction = new RecipeInstruction();
        instruction.setText(text);
        return instruction;
    }

}
