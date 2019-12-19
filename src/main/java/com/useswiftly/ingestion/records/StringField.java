package com.useswiftly.ingestion.records;

import com.useswiftly.ingestion.product.fields.ProductIdField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.StringJoiner;

/**
 *
 */
public abstract class StringField<RECORD_TYPE> implements Field<String, RECORD_TYPE> {
    public StringField() {
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }

    @Nullable
    protected String convertSubstringToDisplayString(@NotNull final String substring) {
        final String trimmed = substring.trim();

        if (trimmed.isEmpty()) {
            return null;
        }

        return trimmed;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ProductIdField.class.getSimpleName() + "[", "]")
                .add("startPositionInclusive=" + getStartPositionInclusive())
                .add("endPositionExclusive=" + getEndPositionExclusive())
                .add("name='" + getName() + "'")
                .add("type=" + getType())
                .toString();
    }
}
