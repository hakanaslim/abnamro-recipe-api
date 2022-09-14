package com.abnamro.recipe.persistance.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@Getter
@Setter
@MappedSuperclass
public abstract class IDEntity<I extends Serializable> implements Serializable {

    @Id
    @Column(name = "ID", nullable = false)
    private I id;

}
