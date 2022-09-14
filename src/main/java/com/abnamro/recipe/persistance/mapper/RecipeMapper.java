package com.abnamro.recipe.persistance.mapper;

import com.abnamro.recipe.api.model.RecipeCreateUpdate;
import com.abnamro.recipe.persistance.model.Recipe;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RecipeMapper {

    RecipeMapper INSTANCE = Mappers.getMapper(RecipeMapper.class);

    Recipe fromRequest(RecipeCreateUpdate source);

}
