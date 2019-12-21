package com.useswiftly.ingestion.records;

import com.useswiftly.ingestion.product.fields.ProductIdField;
import org.jetbrains.annotations.NotNull;

import java.util.StringJoiner;

/**
 * Interface representing a metadata field for a record.
 */
public interface Field<FIELD_TYPE, RECORD_TYPE> {
    /**
     * @return inclusive character position where field starts (zero indexed)
     */
    int getStartPositionInclusive();

    /**
     * @return exclusive character position where field ends (zero indexed)
     */
    int getEndPositionExclusive();

    /**
     * @return plain-text name of field
     */
    String getName();

    /**
     * @return Java data type of field
     */
    Class<FIELD_TYPE> getType();

    /**
     * Converts raw substring from record line to the appropriate data type and
     * assigns it to the specified record instance.
     *
     * @param substring substring to process
     * @param record record to associate field with
     */
    void convertAndAssignValueToRecord(@NotNull String substring, @NotNull RECORD_TYPE record);

    /**
     * Default toString() implementation.
     */
    static String toString(Field<?, ?> field) {
        return new StringJoiner(", ", ProductIdField.class.getSimpleName() + "[", "]")
                .add("startPositionInclusive=" + field.getStartPositionInclusive())
                .add("endPositionExclusive=" + field.getEndPositionExclusive())
                .add("name='" + field.getName() + "'")
                .add("type=" + field.getType())
                .toString();
    }
}
