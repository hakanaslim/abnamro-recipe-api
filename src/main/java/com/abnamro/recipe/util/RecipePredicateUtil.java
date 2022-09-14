package com.abnamro.recipe.util;

import com.abnamro.recipe.api.model.RecipeFilter;
import com.abnamro.recipe.persistance.enums.SpecialDietType;
import com.abnamro.recipe.persistance.model.QRecipe;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@UtilityClass
public class RecipePredicateUtil {

    private final String FILTER_COLUMN = ";";
    private final String FILTER_EXISTS = "+";
    private final String FILTER_NOT_EXISTS = "-";

    public BooleanExpression toPredicate(RecipeFilter filter) {
        List<BooleanExpression> list = new ArrayList<>();

        if (StringUtils.isNoneEmpty(filter.getName())) {
            list.add(QRecipe.recipe.name.eq(filter.getName()));
        }

        if (Objects.nonNull(filter.getCourse())) {
            list.add(QRecipe.recipe.course.eq(filter.getCourse()));
        }

        if (Boolean.TRUE.equals(filter.getVegetarian())) {
            list.add(QRecipe.recipe.specialDiet.eq(SpecialDietType.VEGETARIAN));
        } else if (Boolean.FALSE.equals(filter.getVegetarian())) {
            list.add(QRecipe.recipe.specialDiet.ne(SpecialDietType.VEGETARIAN));
        }

        if (StringUtils.isNotBlank(filter.getServant())) {
            list.add(QRecipe.recipe.servant.like(getLike(filter.getServant())));
        }

        if (StringUtils.isNoneEmpty(filter.getIngredient())) {
            prepareIngredient(filter.getIngredient(), list);
        }

        if (StringUtils.isNoneEmpty(filter.getInstruction())) {
            prepareInstruction(filter.getInstruction(), list);
        }

        if (Objects.nonNull(filter.getSpecialDiet())) {
            BooleanExpression predicate = QRecipe.recipe.specialDiet.eq(filter.getSpecialDiet());
            list.add(predicate);
        }
        return anyOf(list);
    }

    private static void prepareIngredient(String filter, List<BooleanExpression> list) {
        String[] criteria = filter.split(FILTER_COLUMN);
        BooleanExpression predicate = null;
        for(String criterion: criteria) {
            String operator = (criterion.startsWith(FILTER_NOT_EXISTS) ? FILTER_NOT_EXISTS : FILTER_EXISTS);
            String value = (criterion.startsWith(FILTER_NOT_EXISTS) || criterion.startsWith(FILTER_EXISTS) ?
                    criterion.substring(1) : criterion);
            BooleanExpression subPredicate;
            if (operator.equals(FILTER_NOT_EXISTS)) {
                subPredicate = QRecipe.recipe.recipeIngredients.any().text.in(value).not();
            } else {
                subPredicate = QRecipe.recipe.recipeIngredients.any().text.in(value);
            }
            predicate = predicate == null ? subPredicate : predicate.and(subPredicate);
        }
        if (Objects.nonNull(predicate)) {
            list.add(predicate);
        }
    }

    private static void prepareInstruction(String filter, List<BooleanExpression> list) {
        String[] criteria = filter.split(FILTER_COLUMN);
        BooleanExpression predicate = null;
        for(String value: criteria) {
            BooleanExpression subPredicate =
                    QRecipe.recipe.recipeInstructions.any().text.like(getLike(value));
            predicate = predicate == null ? subPredicate : predicate.and(subPredicate);
        }
        if (predicate != null) {
            list.add(predicate);
        }
    }

    private static String getLike(String value) {
        if (StringUtils.startsWith(value, "%") || StringUtils.endsWith(value, "%")) {
            return String.format("%s", value);
        }
        return String.format("%%%s%%", value);
    }

    private static BooleanExpression anyOf(List<BooleanExpression> expressions) {
        BooleanExpression rv = null;
        for (BooleanExpression b : expressions) {
            rv = rv == null ? b : rv.and(b);
        }
        return rv;
    }
}
