package com.abnamro.recipe.persistance.model;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.querydsl.core.annotations.QueryEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@QueryEntity
@Entity
@Table(name = "RECIPE_INSTRUCTION")
public class RecipeInstruction extends IDEntity<String> {

    @ManyToOne
    @JoinColumn(name = "RECIPE_ID", nullable = false)
    @JsonIncludeProperties(value = "id")
    private Recipe recipe;

    @Column(name = "TEXT", nullable = false)
    private String text;

}
