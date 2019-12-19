package com.useswiftly.ingestion.product.fields;

import com.useswiftly.ingestion.product.ProductRecord;
import com.useswiftly.ingestion.records.StringField;
import org.jetbrains.annotations.NotNull;

/**
 */
public class ProductSizeField extends StringField<ProductRecord> {
    @Override
    public int getStartPositionInclusive() {
        return 133;
    }

    @Override
    public int getEndPositionExclusive() {
        return 142;
    }

    @Override
    public String getName() {
        return "Product Size";
    }

    @Override
    public void convertAndAssignValueToRecord(@NotNull final String substring, @NotNull final ProductRecord record) {
        final String displayText = convertSubstringToDisplayString(substring);
        record.setProductSize(displayText);
    }
}
