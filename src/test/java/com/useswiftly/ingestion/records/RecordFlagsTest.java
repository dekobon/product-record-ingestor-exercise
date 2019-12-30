package com.useswiftly.ingestion.records;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class RecordFlagsTest {
    public void canGetFlagsSize() {
        final int size = 9;
        final RecordFlags recordFlags = new RecordFlags(size);
        Assert.assertEquals(recordFlags.getSize(), size);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void cantInstantiateWithNegativeLength() {
        new RecordFlags(-1);
    }

    public void canAssignAndReadAnAssortmentOfFlags() {
        final RecordFlags recordFlags = new RecordFlags(3);
        recordFlags.setFlagAtPosition(0, true);
        recordFlags.setFlagAtPosition(1, false);
        recordFlags.setFlagAtPosition(2, true);

        Assert.assertTrue(recordFlags.getFlagAtPosition(0),
                "Flag not correctly set to true");
        Assert.assertFalse(recordFlags.getFlagAtPosition(1),
                "Flag not correctly set to false");
        Assert.assertTrue(recordFlags.getFlagAtPosition(2),
                "Flag not correctly set to true");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void cantSetNegativePosition() {
        final RecordFlags recordFlags = new RecordFlags(3);
        recordFlags.setFlagAtPosition(-1, true);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void cantReadNegativePosition() {
        final RecordFlags recordFlags = new RecordFlags(3);
        recordFlags.getFlagAtPosition(-1);
    }
}
