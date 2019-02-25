package com.interview.task.enums;

/**
 * Represents currencies.
 */
public enum Currency {
    UAH("UAH"),
    USD("USD"),
    EUR("EUR");

    private final String typeValue;

    Currency(String typeValue) {
        this.typeValue = typeValue;
    }

    public String getTypeValue() {
        return typeValue;
    }
}
