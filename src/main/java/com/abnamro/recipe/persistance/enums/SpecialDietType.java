package com.abnamro.recipe.persistance.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.security.InvalidParameterException;

public enum SpecialDietType {
    NO_DIET,
    ALLERGIES,
    DIABETIC,
    FAT_FREE,
    FOR_KIDS,
    GLUTEN_FREE,
    HEALTH_NUT,
    HEALTHY,
    HIGH_FIBRE,
    KIDS,
    KOSHER,
    LACTO,
    LAMB,
    LIGHT,
    LOW_CALORIE,
    LOW_CARB,
    LOW_FAT,
    LOW_GLUTEN,
    MEATLESS,
    MILK_FREE,
    NO_GLUTEN,
    OVO_LACTO,
    PALEO,
    PASSOVER,
    BABY_FOOD,
    VEGETARIAN,
    WEIGHTWATCHERS,
    WHEAT_FREE,
    WHOLE_WHEAT;

    @JsonCreator
    public static SpecialDietType fromValue(String text) {
        for (SpecialDietType b : SpecialDietType.values()) {
            if (b.name().equals(text)) {
                return b;
            }
        }
        throw new InvalidParameterException("Invalid special diet type :" + text);
    }

    @Override
    @JsonValue
    public String toString() {
        return this.name();
    }
}
