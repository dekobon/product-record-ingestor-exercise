package com.useswiftly.ingestion.product;

/**
 * Enum indicating the given unit of measure for a product.
 */
public enum UnitOfMeasure {
    EACH("Each"),
    POUND("Pound");

    private final String displayText;

    UnitOfMeasure(final String displayText) {
        this.displayText = displayText;
    }

    @Override
    public String toString() {
        return displayText;
    }
}
