package com.abnamro.recipe.persistance.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.security.InvalidParameterException;

public enum CourseType {
    APPETIZERS,
    BREAD,
    BREAKFASTS,
    BRUNCH,
    CONDIMENTS,
    DESSERT,
    LUNCH,
    MAIN_DISH,
    SALAD,
    SIDE_DISH,
    SNACKS,
    SOUPS_STEWS;

    @JsonCreator
    public static CourseType fromValue(String text) {
        for (CourseType b : CourseType.values()) {
            if (b.name().equals(text)) {
                return b;
            }
        }
        throw new InvalidParameterException("Invalid course type :" + text);
    }

    @Override
    @JsonValue
    public String toString() {
        return this.name();
    }
}
