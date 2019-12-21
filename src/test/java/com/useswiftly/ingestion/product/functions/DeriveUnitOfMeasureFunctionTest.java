package com.useswiftly.ingestion.product.functions;

import com.useswiftly.ingestion.product.ProductRecordFlags;
import com.useswiftly.ingestion.product.UnitOfMeasure;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class DeriveUnitOfMeasureFunctionTest {
    public void willReturnPoundWhenPerWeightItem() {
        final UnitOfMeasure expected = UnitOfMeasure.POUND;
        final DeriveUnitOfMeasureFunction function = new DeriveUnitOfMeasureFunction();
        final ProductRecordFlags flags = new ProductRecordFlags().setPerWeightItem(true);
        final UnitOfMeasure actual = function.apply(flags);

        Assert.assertEquals(actual, expected);
    }

    public void willReturnEachWhenNotPerWeightItem() {
        final UnitOfMeasure expected = UnitOfMeasure.EACH;
        final DeriveUnitOfMeasureFunction function = new DeriveUnitOfMeasureFunction();
        final ProductRecordFlags flags = new ProductRecordFlags().setPerWeightItem(false);
        final UnitOfMeasure actual = function.apply(flags);

        Assert.assertEquals(actual, expected);
    }

    @SuppressWarnings("ConstantConditions")
    public void willReturnNullIfFlagsAreNull() {
        final DeriveUnitOfMeasureFunction function = new DeriveUnitOfMeasureFunction();
        final ProductRecordFlags flags = null;
        final UnitOfMeasure actual = function.apply(flags);

        Assert.assertNull(actual);
    }
}
