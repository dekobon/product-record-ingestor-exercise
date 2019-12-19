/*
 * Copyright (c) 2019, Joyent, Inc. All rights reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
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
