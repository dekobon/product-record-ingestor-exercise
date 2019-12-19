package com.useswiftly.ingestion.records;

import org.jetbrains.annotations.NotNull;

/**
 *
 */
public interface Field<FIELD_TYPE, RECORD_TYPE> {
    int getStartPositionInclusive();
    int getEndPositionExclusive();
    String getName();
    Class<FIELD_TYPE> getType();
    void convertAndAssignValueToRecord(@NotNull String substring, @NotNull RECORD_TYPE record);
}
