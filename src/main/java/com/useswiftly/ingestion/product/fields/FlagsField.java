package com.useswiftly.ingestion.product.fields;

import com.useswiftly.ingestion.product.ProductRecord;
import com.useswiftly.ingestion.product.ProductRecordFlags;
import com.useswiftly.ingestion.records.Field;
import com.useswiftly.ingestion.records.RecordParseException;
import org.jetbrains.annotations.NotNull;

/**
 *
 */
public class FlagsField implements Field<ProductRecordFlags, ProductRecord> {
    @Override
    public int getStartPositionInclusive() {
        return 123;
    }

    @Override
    public int getEndPositionExclusive() {
        return 132;
    }

    @Override
    public String getName() {
        return "Flags";
    }

    @Override
    public Class<ProductRecordFlags> getType() {
        return ProductRecordFlags.class;
    }

    @Override
    public void convertAndAssignValueToRecord(@NotNull final String substring, @NotNull final ProductRecord record) {
        final char[] charValues = substring.toCharArray();

        if (ProductRecordFlags.FLAG_COUNT != charValues.length) {
            String msg = String.format("Unexpected number of flags found [%d]" +
                    " in field [%s] - expecting [%d] flags", charValues.length,
                    getName(), ProductRecordFlags.FLAG_COUNT);
            throw new RecordParseException(msg);
        }

        final ProductRecordFlags flags = new ProductRecordFlags();

        for (int position = 0; position < charValues.length; position++) {
            final char character = charValues[position];
            final boolean flag;

            if (character == 'Y') {
                flag = true;
            } else if (character == 'N') {
                flag = false;
            } else {
                String msg = String.format("Invalid flag character encountered " +
                        "[%c] in field [%s] - only 'Y' or 'N' are " +
                        "valid characters", character, getName());
                throw new RecordParseException(msg);
            }

            flags.setFlagAtPosition(position, flag);
        }

        record.setFlags(flags);
    }
}
