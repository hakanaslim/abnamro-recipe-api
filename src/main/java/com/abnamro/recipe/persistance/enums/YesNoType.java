package com.abnamro.recipe.persistance.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.security.InvalidParameterException;

public enum YesNoType {
    YES("YES"),
    NO("NO");

    private final String value;

    YesNoType(String value) {
        this.value = value;
    }

    @JsonCreator
    public static YesNoType fromValue(String text) {
        for (YesNoType b : YesNoType.values()) {
            if (String.valueOf(b.value).equals(text)) {
                return b;
            }
        }
        throw new InvalidParameterException("Invalid yes/no type :" + text);
    }

    @Override
    @JsonValue
    public String toString() {
        return this.name();
    }
}
