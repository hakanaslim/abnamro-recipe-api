package com.abnamro.recipe.persistance.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.*;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class TrackableEntity<I extends Serializable> extends IDEntity<I> {

    @Column(name = "CREATED_BY", nullable = false, updatable = false)
    @CreatedBy
    protected I createdBy;

    @Column(name = "MODIFIED_BY", nullable = false)
    @LastModifiedBy
    protected I modifiedBy;

    @Column(name = "CREATE_DATE", nullable = false, updatable = false)
    @CreatedDate
    protected LocalDateTime createdDate;

    @Column(name = "MODIFIED_DATE", nullable = false)
    @LastModifiedDate
    @Version
    protected LocalDateTime modifiedDate;

}
