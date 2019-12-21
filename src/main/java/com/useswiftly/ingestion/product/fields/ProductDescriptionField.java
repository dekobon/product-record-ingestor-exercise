package com.useswiftly.ingestion.product.fields;

import com.useswiftly.ingestion.product.ProductRecord;
import com.useswiftly.ingestion.records.StringField;
import org.jetbrains.annotations.NotNull;

/**
 * Metadata for product description field.
 */
public class ProductDescriptionField extends StringField<ProductRecord> {
    @Override
    public int getStartPositionInclusive() {
        return 9;
    }

    @Override
    public int getEndPositionExclusive() {
        return 68;
    }

    @Override
    public String getName() {
        return "Product Description";
    }

    @Override
    public void convertAndAssignValueToRecord(@NotNull final String substring, @NotNull final ProductRecord record) {
        final String displayText = convertSubstringToDisplayString(substring);
        record.setProductDescription(displayText);
    }
}
