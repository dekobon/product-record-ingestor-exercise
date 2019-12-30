package com.useswiftly.ingestion.product;

import com.useswiftly.ingestion.records.RecordFormattable;
import org.javamoney.moneta.FastMoney;
import org.javamoney.moneta.format.CurrencyStyle;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.format.AmountFormatQueryBuilder;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryFormats;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Locale;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Test
public class ProductRecordFormatterTest {
    private static final Locale LOCALE = Locale.US;
    private static final MonetaryAmountFormat MONETARY_AMOUNT_FORMAT =
            MonetaryFormats.getAmountFormat(
            AmountFormatQueryBuilder.of(LOCALE).set(CurrencyStyle.SYMBOL)
                    .build());
    private static final RecordFormattable<ProductRecord> FORMATTER =
            new ProductRecordFormatter(MONETARY_AMOUNT_FORMAT);

    public void canFormatFullProductRecord() {
        final BigInteger productId = new BigInteger("12345678");
        final String description = "This is a description";
        final String regularDisplayPrice = "12 for $120.00";
        final MonetaryAmount regularCalculatorPrice = amount(10.00);
        final String promotionalDisplayPrice = "24 for $120.00";
        final MonetaryAmount promotionalCalculatorPrice = amount(50.00);
        final UnitOfMeasure unitOfMeasure = UnitOfMeasure.POUND;
        final String productSize = "lb";
        final BigDecimal taxRate = new BigDecimal("5.4");

        final ProductRecord record = mock(ProductRecord.class);
        when(record.getProductId()).thenReturn(productId);
        when(record.getProductDescription()).thenReturn(description);
        when(record.regularDisplayPrice()).thenReturn(regularDisplayPrice);
        when(record.calculateRegularCalculatorPrice()).thenReturn(regularCalculatorPrice);
        when(record.promotionalDisplayPrice()).thenReturn(promotionalDisplayPrice);
        when(record.calculatePromotionalCalculatorPrice()).thenReturn(promotionalCalculatorPrice);
        when(record.deriveUnitOfMeasure()).thenReturn(unitOfMeasure);
        when(record.getProductSize()).thenReturn(productSize);
        when(record.calculateTaxRate()).thenReturn(taxRate);

        final String actual = FORMATTER.format(record);
        final String expected = "[productId=12345678, productDescription='This is a description', regularDisplayPrice=12 for $120.00, regularCalculatorPrice=$10.00, promotionalDisplayPrice=24 for $120.00, promotionalCalculatorPrice=$50.00, unitOfMeasure=Pound, productSize='lb', taxRate=5.4]";
        Assert.assertEquals(actual, expected);
    }

    public void canFormatProductIdWithLeadingZeros() {
        final BigInteger productId = new BigInteger("1");
        final ProductRecord record = mock(ProductRecord.class);
        when(record.getProductId()).thenReturn(productId);

        final String actual = FORMATTER.format(record);
        final String expected = "[productId=00000001, productDescription='null', regularDisplayPrice=null, regularCalculatorPrice=null, promotionalDisplayPrice=null, promotionalCalculatorPrice=null, unitOfMeasure=null, productSize='n/a', taxRate=null]";
        Assert.assertEquals(actual, expected);
    }

    public void canFormatNegativePrices() {
        final BigInteger productId = new BigInteger("12");
        final MonetaryAmount regularCalculatorPrice = amount(-10000.24);
        final MonetaryAmount promotionalCalculatorPrice = amount(-19222.333);

        final ProductRecord record = mock(ProductRecord.class);
        when(record.getProductId()).thenReturn(productId);
        when(record.getProductId()).thenReturn(productId);
        when(record.calculateRegularCalculatorPrice()).thenReturn(regularCalculatorPrice);
        when(record.calculatePromotionalCalculatorPrice()).thenReturn(promotionalCalculatorPrice);

        final String actual = FORMATTER.format(record);
        final String expected = "[productId=00000012, productDescription='null', regularDisplayPrice=null, regularCalculatorPrice=$-10,000.24, promotionalDisplayPrice=null, promotionalCalculatorPrice=$-19,222.33, unitOfMeasure=null, productSize='n/a', taxRate=null]";
        Assert.assertEquals(actual, expected);
    }

    private static MonetaryAmount amount(final Number amount) {
        return Monetary.getAmountFactory(FastMoney.class)
                .setCurrency(Monetary.getCurrency("USD"))
                .setNumber(amount)
                .create();
    }
}
