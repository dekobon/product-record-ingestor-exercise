package com.useswiftly.ingestion.product.functions;

import com.useswiftly.ingestion.product.ProductRecordFlags;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.inject.Provider;
import java.math.BigDecimal;

@Test
public class CalculateTaxRateFunctionTest {
    public void canReturnCorrectTaxRateIfFlagIsSet() {
        final BigDecimal expected = BigDecimal.valueOf(7.775);
        final Provider<BigDecimal> provider = () -> expected;
        final CalculateTaxRateFunction function = new CalculateTaxRateFunction(provider);
        final ProductRecordFlags flags = new ProductRecordFlags().setTaxableItem(true);
        final BigDecimal actual = function.apply(flags);

        Assert.assertEquals(actual, expected);
    }

    public void canReturnCorrectTaxRateIfFlagIsNotSet() {
        final BigDecimal expected = BigDecimal.valueOf(0);
        final Provider<BigDecimal> provider = () -> BigDecimal.valueOf(7.775);
        final CalculateTaxRateFunction function = new CalculateTaxRateFunction(provider);
        final ProductRecordFlags flags = new ProductRecordFlags().setTaxableItem(false);
        final BigDecimal actual = function.apply(flags);

        Assert.assertEquals(actual, expected);
    }

    @SuppressWarnings("ConstantConditions")
    public void willReturnNullIfFlagsAreNull() {
        final Provider<BigDecimal> provider = () -> BigDecimal.valueOf(7.775);
        final CalculateTaxRateFunction function = new CalculateTaxRateFunction(provider);
        final ProductRecordFlags flags = null;
        final BigDecimal actual = function.apply(flags);

        Assert.assertNull(actual);
    }
}
