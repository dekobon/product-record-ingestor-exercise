package com.useswiftly.ingestion.product.fields;

import com.useswiftly.ingestion.product.ProductRecord;
import com.useswiftly.ingestion.records.BigIntegerField;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;

/**
 * Metadata for product ID field.
 */
public class ProductIdField extends BigIntegerField<ProductRecord> {
    public ProductIdField() {
    }

    @Override
    public int getStartPositionInclusive() {
        return 0;
    }

    @Override
    public int getEndPositionExclusive() {
        return 8;
    }

    @Override
    public String getName() {
        return "Product ID";
    }

    @Override
    public void convertAndAssignValueToRecord(@NotNull final String substring, @NotNull final ProductRecord record) {
        final BigInteger productId = convertStringToBigInteger(substring);
        record.setProductId(productId);
    }
}
