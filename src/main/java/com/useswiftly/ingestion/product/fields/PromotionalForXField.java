package com.useswiftly.ingestion.product.fields;

import com.useswiftly.ingestion.product.ProductRecord;
import com.useswiftly.ingestion.records.BigIntegerField;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

/**
 * Metadata for promotional for x field.
 */
public class PromotionalForXField extends BigIntegerField<ProductRecord> {
    public PromotionalForXField() {
    }

    @Override
    public int getStartPositionInclusive() {
        return 114;
    }

    @Override
    public int getEndPositionExclusive() {
        return 122;
    }

    @Override
    public String getName() {
        return "Promotional For X";
    }

    @Override
    public void convertAndAssignValueToRecord(@NotNull final String substring, @NotNull final ProductRecord record) {
        final BigInteger forX = convertStringToBigInteger(substring);
        record.setPromotionalForX(forX);
    }
}
