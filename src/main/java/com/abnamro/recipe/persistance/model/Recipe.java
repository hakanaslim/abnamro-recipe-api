package com.abnamro.recipe.persistance.model;

import com.abnamro.recipe.persistance.enums.CourseType;
import com.abnamro.recipe.persistance.enums.SpecialDietType;
import com.querydsl.core.annotations.QueryEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@QueryEntity
@Entity
@Table(name = "RECIPE")
public class Recipe extends TrackableEntity<String> {

    @Column(name = "NAME", nullable = false, length = 100)
    private String name;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "SERVANT", nullable = false, length = 60)
    private String servant;

    @Enumerated(EnumType.STRING)
    @Column(name = "COURSE", nullable = false, length = 30)
    private CourseType course;

    @Enumerated(EnumType.STRING)
    @Column(name = "SPECIAL_DIET", nullable = false, length = 30)
    private SpecialDietType specialDiet;

    @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    private List<RecipeIngredient> recipeIngredients;

    @OneToMany(mappedBy = "recipe", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    private List<RecipeInstruction> recipeInstructions;

}
