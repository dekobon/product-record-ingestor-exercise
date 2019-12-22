package com.useswiftly.ingestion.records;

import org.jetbrains.annotations.Range;

import java.util.Arrays;

/**
 * Class that allows for an arbitrary number of boolean values to be logically
 * grouped and accessed by position.
 */
public class RecordFlags implements PositionalFlags {
    private final boolean[] values;

    @SuppressWarnings("ConstantConditions")
    public RecordFlags(@Range(from = 0, to = Integer.MAX_VALUE) final int flagCount) {
        if (flagCount < 0) {
            String msg = String.format("Total flag count must not be negative - " +
                            "Flag count [%d] value is invalid",
                    flagCount);
            throw new IllegalArgumentException(msg);
        }

        this.values = new boolean[flagCount];
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean getFlagAtPosition(@Range(from = 0, to = Integer.MAX_VALUE) final int position) {
        if (position < 0 || position > values.length) {
            String msg = String.format("Invalid flag position provided [%d] the valid " +
                    "values for flag positions are [0-%d]",
                    position, values.length - 1);
            throw new IllegalArgumentException(msg);
        }

        return values[position];
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void setFlagAtPosition(@Range(from = 0, to = Integer.MAX_VALUE) final int position, final boolean flag) {
        if (position < 0 || position > values.length) {
            String msg = String.format("Invalid flag position provided [%d] the valid " +
                            "values for flag positions are [0-%d]",
                    position, values.length - 1);
            throw new IllegalArgumentException(msg);
        }

        values[position] = flag;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final RecordFlags that = (RecordFlags) o;
        return Arrays.equals(values, that.values);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }

    @Override
    public String toString() {
        final char[] charValues = new char[values.length];

        for (int i = 0; i < values.length; i++) {
            charValues[i] = values[i] ? 'Y' : 'N';
        }

        return new String(charValues);
    }
}
