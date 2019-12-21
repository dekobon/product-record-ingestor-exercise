package com.useswiftly.ingestion.product;

import com.useswiftly.ingestion.records.RecordFlags;

/**
 * Flags wrapper class that allows for accessing specific flags at specific
 * positions via name Java beans in order to provide a human friendly interface
 * for product record flags.
 */
public class ProductRecordFlags extends RecordFlags {
    public static final int FLAG_COUNT = 9;
    private static final int PER_WEIGHT_FLAG_POS = 2;
    private static final int TAXABLE_ITEM_FLAG_POS = 4;

    public ProductRecordFlags() {
        super(FLAG_COUNT);
    }

    public boolean isPerWeightItem() {
        return getFlagAtPosition(PER_WEIGHT_FLAG_POS);
    }

    public ProductRecordFlags setPerWeightItem(final boolean isPerWeightItem) {
        setFlagAtPosition(PER_WEIGHT_FLAG_POS, isPerWeightItem);
        return this;
    }

    public boolean isTaxableItem() {
        return getFlagAtPosition(TAXABLE_ITEM_FLAG_POS);
    }

    public ProductRecordFlags setTaxableItem(final boolean isTaxableItem) {
        setFlagAtPosition(TAXABLE_ITEM_FLAG_POS, isTaxableItem);
        return this;
    }
}
