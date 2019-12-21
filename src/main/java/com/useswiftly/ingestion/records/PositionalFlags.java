package com.useswiftly.ingestion.records;

import org.jetbrains.annotations.Range;

/**
 * Interface representing N number of boolean flags that are modeled as a
 * conceptual grouping within a given record.
 */
public interface PositionalFlags {
    /**
     * Reads the boolean value of a flag at the indicated position.
     *
     * @param position flag position starting at 0
     * @return boolean value associate with flag
     */
    boolean getFlagAtPosition(@Range(from = 0, to = Integer.MAX_VALUE) int position);

    /**
     * Sets the boolean value of a flag at the indicated position.
     * @param position flag position starting at 0
     * @param flag boolean value associate with flag
     */
    void setFlagAtPosition(@Range(from = 0, to = Integer.MAX_VALUE) int position, boolean flag);
}
