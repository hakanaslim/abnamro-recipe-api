package com.abnamro.recipe.api.model;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class AuthorCreateUpdate implements Serializable {

    @NotNull(message = "fullName can not be null")
    private String fullName;
    private String persistBy;

}
