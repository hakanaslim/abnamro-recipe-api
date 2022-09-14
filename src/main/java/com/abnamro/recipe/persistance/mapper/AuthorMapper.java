package com.abnamro.recipe.persistance.mapper;

import com.abnamro.recipe.api.model.AuthorCreateUpdate;
import com.abnamro.recipe.persistance.model.Author;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AuthorMapper {

    AuthorMapper INSTANCE = Mappers.getMapper(AuthorMapper.class);

    Author fromRequest(AuthorCreateUpdate source);

}
