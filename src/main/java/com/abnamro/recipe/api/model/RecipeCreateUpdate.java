package com.abnamro.recipe.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.abnamro.recipe.persistance.enums.CourseType;
import com.abnamro.recipe.persistance.enums.SpecialDietType;
import com.abnamro.recipe.persistance.model.RecipeIngredient;
import com.abnamro.recipe.persistance.model.RecipeInstruction;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class RecipeCreateUpdate implements Serializable {

    private String name;

    private String description;

    private String servant;

    private CourseType course;

    private SpecialDietType specialDiet;

    @JsonIgnoreProperties(value = {"recipe"})
    private List<RecipeIngredient> recipeIngredients = new ArrayList<>();

    @JsonIgnoreProperties(value = {"recipe"})
    private List<RecipeInstruction> recipeInstructions = new ArrayList<>();
}
