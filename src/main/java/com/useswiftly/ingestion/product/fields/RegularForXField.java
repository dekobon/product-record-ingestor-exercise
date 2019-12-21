package com.useswiftly.ingestion.product.fields;

import com.useswiftly.ingestion.product.ProductRecord;
import com.useswiftly.ingestion.records.BigIntegerField;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

/**
 * Metadata for regular for X field.
 */
public class RegularForXField extends BigIntegerField<ProductRecord> {
    public RegularForXField() {
    }

    @Override
    public int getStartPositionInclusive() {
        return 105;
    }

    @Override
    public int getEndPositionExclusive() {
        return 113;
    }

    @Override
    public String getName() {
        return "Regular For X";
    }

    @Override
    public void convertAndAssignValueToRecord(@NotNull final String substring, @NotNull final ProductRecord record) {
        final BigInteger forX = convertStringToBigInteger(substring);
        record.setRegularForX(forX);
    }
}
