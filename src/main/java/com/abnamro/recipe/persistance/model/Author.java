package com.abnamro.recipe.persistance.model;

import com.querydsl.core.annotations.QueryEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@QueryEntity
@Entity
@Table(name = "AUTHOR")
public class Author extends TrackableEntity<String> {

    @Column(name = "FULL_NAME", nullable = false, length = 125)
    private String fullName;

}
