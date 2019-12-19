package com.useswiftly.ingestion.records;

import org.jetbrains.annotations.Range;

import java.util.Arrays;

/**
 */
public class RecordFlags implements PositionalFlags {
    private final boolean[] values;

    public RecordFlags(final int flagCount) {
        this.values = new boolean[flagCount];
    }

    @Override
    public boolean getFlagAtPosition(@Range(from = 0, to = Integer.MAX_VALUE) final int position) {
        if (position > values.length) {
            String msg = String.format("Invalid flag position provided [%d] the valid " +
                    "values for flag positions are [0-%d]",
                    position, values.length - 1);
            throw new IllegalArgumentException(msg);
        }

        return values[position];
    }

    @Override
    public void setFlagAtPosition(@Range(from = 0, to = Integer.MAX_VALUE) final int position, final boolean flag) {
        if (position > values.length) {
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
