package com.abnamro.recipe.util;

import com.abnamro.recipe.config.CommonConstants;
import com.abnamro.recipe.persistance.model.Recipe;
import com.abnamro.recipe.persistance.model.TrackableEntity;
import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@UtilityClass
public class PersistenceUtil {

    public <T extends TrackableEntity<String>> void setOnPrePersist(T entity, String persistBy) {
        entity.setId(generateNewUUID());
        if (Objects.isNull(entity.getCreatedBy())) {
            entity.setCreatedBy(getPersistBy(persistBy));
        }
        entity.setCreatedDate(LocalDateTime.now());
    }

    public <T extends TrackableEntity<String>> void setOnPreUpdate(T entity, String persistBy) {
        if (Objects.isNull(entity.getModifiedBy())) {
            entity.setModifiedBy(getPersistBy(persistBy));
        }
        entity.setModifiedDate(LocalDateTime.now());
    }

    public void fillParentProperties(Recipe entity) {
        if(!CollectionUtils.isEmpty(entity.getRecipeIngredients())) {
            entity.getRecipeIngredients().stream()
                    .filter(item -> !StringUtils.hasText(item.getId()))
                    .forEach(item -> item.setId(generateNewUUID()));
            entity.getRecipeIngredients().stream()
                    .filter(item -> Objects.isNull(item.getRecipe()))
                    .forEach(item -> {
                        Recipe recipe = new Recipe();
                        recipe.setId(entity.getId());
                        item.setRecipe(recipe);
                    });
        }
        if(!CollectionUtils.isEmpty(entity.getRecipeInstructions())) {
            entity.getRecipeInstructions().stream()
                    .filter(item -> !StringUtils.hasText(item.getId()))
                    .forEach(item -> item.setId(generateNewUUID()));
            entity.getRecipeInstructions().stream()
                    .filter(item -> Objects.isNull(item.getRecipe()))
                    .forEach(item -> {
                        Recipe recipe = new Recipe();
                        recipe.setId(entity.getId());
                        item.setRecipe(recipe);
                    });
        }
    }

    private String generateNewUUID() {
        return UUID.randomUUID().toString();
    }

    private String getPersistBy(String persistBy) {
        if (!StringUtils.hasLength(persistBy)) {
            return CommonConstants.DEFAULT_PERSIST_BY;
        }
        return persistBy;
    }

}
