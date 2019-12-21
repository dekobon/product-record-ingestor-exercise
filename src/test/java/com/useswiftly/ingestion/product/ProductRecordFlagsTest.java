package com.useswiftly.ingestion.product;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class ProductRecordFlagsTest {
    public void canReadTaxableItemFromFlag() {
        final ProductRecordFlags flags = new ProductRecordFlags();
        flags.setFlagAtPosition(4, true);

        Assert.assertTrue(flags.isTaxableItem(),
                "Taxable item flag wasn't set properly");
    }

    public void canReadIsPerWeightItemFromFlag() {
        final ProductRecordFlags flags = new ProductRecordFlags();
        flags.setFlagAtPosition(2, true);

        Assert.assertTrue(flags.isPerWeightItem(),
                "Per weight item flag wasn't set properly");
    }
}
