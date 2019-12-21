package com.useswiftly.ingestion.records;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Record field with the data type {@link String}.
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
        return Field.toString(this);
    }
}
