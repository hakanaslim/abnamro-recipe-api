package com.abnamro.recipe.api.model;

import com.abnamro.recipe.persistance.enums.CourseType;
import com.abnamro.recipe.persistance.enums.SpecialDietType;
import com.querydsl.core.annotations.QueryEntity;
import lombok.Data;

import java.io.Serializable;

@Data
@QueryEntity
public class RecipeFilter implements Serializable {

    private String name;

    private String description;

    private String servant;

    private CourseType course;

    private Boolean vegetarian;

    private SpecialDietType specialDiet;

    private String ingredient;

    private String instruction;
}
