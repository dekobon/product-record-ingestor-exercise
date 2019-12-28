package com.useswiftly.ingestion.product;

import org.javamoney.moneta.FastMoney;
import org.javamoney.moneta.format.CurrencyStyle;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.MonetaryRounding;
import javax.money.RoundingQueryBuilder;
import javax.money.format.AmountFormatQueryBuilder;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryFormats;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Locale;

@Test
public class ProductRecordImplTest {
    private static final CurrencyUnit CURRENCY = Monetary.getCurrency("USD");
    private static final MonetaryAmountFormat FORMAT = MonetaryFormats.getAmountFormat(
            AmountFormatQueryBuilder.of(Locale.US).set(CurrencyStyle.SYMBOL).build());
    private static final MonetaryRounding ROUNDING = Monetary.getRounding(
            RoundingQueryBuilder.of()
                    .setScale(4)
                    .set(RoundingMode.HALF_DOWN).build());

    // Regular price tests

    public void regularDisplayPriceForSingularPositivePriceFormatsCorrectly() {
        final ProductRecordImpl record = new ProductRecordImpl(null, null, FORMAT, ROUNDING)
                .setRegularSplitPrice(FastMoney.zero(CURRENCY))
                .setRegularSingularPrice(FastMoney.of(345.49, CURRENCY));
        final String expected = "$345.49";
        final String actual = record.regularDisplayPrice();

        Assert.assertEquals(actual, expected);
    }

    public void regularDisplayPriceForSingularNegativePriceFormatsCorrectly() {
        final ProductRecordImpl record = new ProductRecordImpl(null, null, FORMAT, ROUNDING)
                .setRegularSplitPrice(FastMoney.zero(CURRENCY))
                .setRegularSingularPrice(FastMoney.of(-12.00, CURRENCY));
        final String expected = "$-12.00";
        final String actual = record.regularDisplayPrice();

        Assert.assertEquals(actual, expected);
    }

    public void regularDisplayPriceForSingularPriceWith5DecimalsFormatsCorrectly() {
        final ProductRecordImpl record = new ProductRecordImpl(null, null, FORMAT, ROUNDING)
                .setRegularSplitPrice(FastMoney.zero(CURRENCY))
                .setRegularSingularPrice(FastMoney.of(13312.12945, CURRENCY));
        final String expected = "$13,312.13";
        final String actual = record.regularDisplayPrice();

        Assert.assertEquals(actual, expected);
    }

    public void regularDisplayPriceForPositiveSplitPriceFormatsCorrectly() {
        final ProductRecordImpl record = new ProductRecordImpl(null, null, FORMAT, ROUNDING)
                .setRegularForX(BigInteger.valueOf(2L))
                .setRegularSplitPrice(FastMoney.of(10.99, CURRENCY))
                .setRegularSingularPrice(FastMoney.zero(CURRENCY));
        final String expected = "2 for $10.99";
        final String actual = record.regularDisplayPrice();

        Assert.assertEquals(actual, expected);
    }

    public void regularDisplayPriceForNegativeSplitPriceFormatsCorrectly() {
        final ProductRecordImpl record = new ProductRecordImpl(null, null, FORMAT, ROUNDING)
                .setRegularForX(BigInteger.valueOf(2L))
                .setRegularSplitPrice(FastMoney.of(-12230.99, CURRENCY))
                .setRegularSingularPrice(FastMoney.zero(CURRENCY));
        final String expected = "2 for $-12,230.99";
        final String actual = record.regularDisplayPrice();

        Assert.assertEquals(actual, expected);
    }

    public void canCalculateRegularCalculatorPriceForSingularPriceWithNoRoundingNeeded() {
        final MonetaryAmount expected = FastMoney.of(345.49, CURRENCY);
        final ProductRecordImpl record = new ProductRecordImpl(null, null, FORMAT, ROUNDING)
                .setRegularSplitPrice(FastMoney.zero(CURRENCY))
                .setRegularSingularPrice(expected);
        final MonetaryAmount actual = record.calculateRegularCalculatorPrice();

        Assert.assertEquals(actual, expected);
    }

    public void canCalculateRegularCalculatorPriceForSingularPriceWithRoundingNeeded() {
        final ProductRecordImpl record = new ProductRecordImpl(null, null, FORMAT, ROUNDING)
                .setRegularSplitPrice(FastMoney.zero(CURRENCY))
                .setRegularSingularPrice(FastMoney.of(345.42949, CURRENCY));
        final MonetaryAmount actual = record.calculateRegularCalculatorPrice();
        final MonetaryAmount expected = FastMoney.of(345.4295, CURRENCY);

        Assert.assertEquals(actual, expected);
    }

    public void canCalculateRegularCalculatorPriceForSplitPriceWithNoRoundingNeeded() {
        final ProductRecordImpl record = new ProductRecordImpl(null, null, FORMAT, ROUNDING)
                .setRegularForX(BigInteger.valueOf(3))
                .setRegularSplitPrice(FastMoney.of(9, CURRENCY))
                .setRegularSingularPrice(FastMoney.zero(CURRENCY));
        final MonetaryAmount actual = record.calculateRegularCalculatorPrice();
        final MonetaryAmount expected = FastMoney.of(3.00, CURRENCY);

        Assert.assertEquals(actual, expected);
    }

    public void canCalculateRegularCalculatorPriceForSplitPriceWithRoundingNeeded() {
        final ProductRecordImpl record = new ProductRecordImpl(null, null, FORMAT, ROUNDING)
                .setRegularForX(BigInteger.valueOf(3))
                .setRegularSplitPrice(FastMoney.of(10, CURRENCY))
                .setRegularSingularPrice(FastMoney.zero(CURRENCY));
        final MonetaryAmount actual = record.calculateRegularCalculatorPrice();
        final MonetaryAmount expected = FastMoney.of(3.3333, CURRENCY);

        Assert.assertEquals(actual, expected);
    }

    // Promotional price tests

    public void promotionalDisplayPriceForSingularPositivePriceFormatsCorrectly() {
        final ProductRecordImpl record = new ProductRecordImpl(null, null, FORMAT, ROUNDING)
                .setPromotionalSplitPrice(FastMoney.zero(CURRENCY))
                .setPromotionalSingularPrice(FastMoney.of(345.49, CURRENCY));
        final String expected = "$345.49";
        final String actual = record.promotionalDisplayPrice();

        Assert.assertEquals(actual, expected);
    }

    public void promotionalDisplayPriceForSingularNegativePriceFormatsCorrectly() {
        final ProductRecordImpl record = new ProductRecordImpl(null, null, FORMAT, ROUNDING)
                .setPromotionalSplitPrice(FastMoney.zero(CURRENCY))
                .setPromotionalSingularPrice(FastMoney.of(-12.00, CURRENCY));
        final String expected = "$-12.00";
        final String actual = record.promotionalDisplayPrice();

        Assert.assertEquals(actual, expected);
    }

    public void promotionalDisplayPriceForSingularPriceWith5DecimalsFormatsCorrectly() {
        final ProductRecordImpl record = new ProductRecordImpl(null, null, FORMAT, ROUNDING)
                .setPromotionalSplitPrice(FastMoney.zero(CURRENCY))
                .setPromotionalSingularPrice(FastMoney.of(13312.12945, CURRENCY));
        final String expected = "$13,312.13";
        final String actual = record.promotionalDisplayPrice();

        Assert.assertEquals(actual, expected);
    }

    public void promotionalDisplayPriceForPositiveSplitPriceFormatsCorrectly() {
        final ProductRecordImpl record = new ProductRecordImpl(null, null, FORMAT, ROUNDING)
                .setPromotionalForX(BigInteger.valueOf(2L))
                .setPromotionalSplitPrice(FastMoney.of(10.99, CURRENCY))
                .setPromotionalSingularPrice(FastMoney.zero(CURRENCY));
        final String expected = "2 for $10.99";
        final String actual = record.promotionalDisplayPrice();

        Assert.assertEquals(actual, expected);
    }

    public void promotionalDisplayPriceForNegativeSplitPriceFormatsCorrectly() {
        final ProductRecordImpl record = new ProductRecordImpl(null, null, FORMAT, ROUNDING)
                .setPromotionalForX(BigInteger.valueOf(2L))
                .setPromotionalSplitPrice(FastMoney.of(-12230.99, CURRENCY))
                .setPromotionalSingularPrice(FastMoney.zero(CURRENCY));
        final String expected = "2 for $-12,230.99";
        final String actual = record.promotionalDisplayPrice();

        Assert.assertEquals(actual, expected);
    }

    public void canCalculatePromotionalCalculatorPriceForSingularPriceWithNoRoundingNeeded() {
        final MonetaryAmount expected = FastMoney.of(345.49, CURRENCY);
        final ProductRecordImpl record = new ProductRecordImpl(null, null, FORMAT, ROUNDING)
                .setPromotionalSplitPrice(FastMoney.zero(CURRENCY))
                .setPromotionalSingularPrice(expected);
        final MonetaryAmount actual = record.calculatePromotionalCalculatorPrice();

        Assert.assertEquals(actual, expected);
    }

    public void canCalculatePromotionalCalculatorPriceForSingularPriceWithRoundingNeeded() {
        final ProductRecordImpl record = new ProductRecordImpl(null, null, FORMAT, ROUNDING)
                .setPromotionalSplitPrice(FastMoney.zero(CURRENCY))
                .setPromotionalSingularPrice(FastMoney.of(345.42949, CURRENCY));
        final MonetaryAmount actual = record.calculatePromotionalCalculatorPrice();
        final MonetaryAmount expected = FastMoney.of(345.4295, CURRENCY);

        Assert.assertEquals(actual, expected);
    }

    public void canCalculatePromotionalCalculatorPriceForSplitPriceWithNoRoundingNeeded() {
        final ProductRecordImpl record = new ProductRecordImpl(null, null, FORMAT, ROUNDING)
                .setPromotionalForX(BigInteger.valueOf(3))
                .setPromotionalSplitPrice(FastMoney.of(9, CURRENCY))
                .setPromotionalSingularPrice(FastMoney.zero(CURRENCY));
        final MonetaryAmount actual = record.calculatePromotionalCalculatorPrice();
        final MonetaryAmount expected = FastMoney.of(3.00, CURRENCY);

        Assert.assertEquals(actual, expected);
    }

    public void canCalculatePromotionalCalculatorPriceForSplitPriceWithRoundingNeeded() {
        final ProductRecordImpl record = new ProductRecordImpl(null, null, FORMAT, ROUNDING)
                .setPromotionalForX(BigInteger.valueOf(3))
                .setPromotionalSplitPrice(FastMoney.of(10, CURRENCY))
                .setPromotionalSingularPrice(FastMoney.zero(CURRENCY));
        final MonetaryAmount actual = record.calculatePromotionalCalculatorPrice();
        final MonetaryAmount expected = FastMoney.of(3.3333, CURRENCY);

        Assert.assertEquals(actual, expected);
    }
}
