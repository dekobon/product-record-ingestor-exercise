package com.useswiftly.ingestion.records;

import org.javamoney.moneta.FastMoney;
import org.jetbrains.annotations.NotNull;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;

@Test
public class MonetaryFieldTest {
    private static final CurrencyUnit TEST_CURRENCY = Monetary.getCurrency("USD");
    private static class FakeRecord implements Record {
    }

    private static class FakeField extends MonetaryField<FakeRecord> {
        public FakeField() {
            super(TEST_CURRENCY);
        }

        @Override
        public int getStartPositionInclusive() {
            return 0;
        }

        @Override
        public int getEndPositionExclusive() {
            return 1;
        }

        @Override
        public String getName() {
            return "Fake Field";
        }

        @Override
        public void convertAndAssignValueToRecord(@NotNull final String substring, @NotNull final FakeRecord record) {
            throw new UnsupportedOperationException("Not implemented for test");
        }
    }

    public void canConvertPositiveNumericStringToCurrency() {
        final String original = "99999999"; // last two digits are decimal values
        final FakeField field = new FakeField();
        final MonetaryAmount actual = field.convertStringToMonetaryAmount(original);
        final MonetaryAmount expected = Monetary.getAmountFactory(FastMoney.class)
                .setCurrency(TEST_CURRENCY).setNumber(999999.99).create();

        Assert.assertEquals(actual, expected);
    }

    public void canConvertNegativeNumericStringToCurrency() {
        final String original = "-12";

        final FakeField field = new FakeField();
        final MonetaryAmount actual = field.convertStringToMonetaryAmount(original);
        final MonetaryAmount expected = Monetary.getAmountFactory(FastMoney.class)
                .setCurrency(TEST_CURRENCY).setNumber(-0.12).create();

        Assert.assertEquals(actual, expected);
    }

    public void canConvertPositiveNumericStringWithLeadingZerosToCurrency() {
        final String original = "00000349";

        final FakeField field = new FakeField();
        final MonetaryAmount actual = field.convertStringToMonetaryAmount(original);
        final MonetaryAmount expected = Monetary.getAmountFactory(FastMoney.class)
                .setCurrency(TEST_CURRENCY).setNumber(3.49).create();

        Assert.assertEquals(actual, expected);
    }

    public void canConvertNegativeNumericStringWithLeadingZerosToCurrency() {
        final String original = "-0000349";

        final FakeField field = new FakeField();
        final MonetaryAmount actual = field.convertStringToMonetaryAmount(original);
        final MonetaryAmount expected = Monetary.getAmountFactory(FastMoney.class)
                .setCurrency(TEST_CURRENCY).setNumber(-3.49).create();

        Assert.assertEquals(actual, expected);
    }

    @Test(expectedExceptions = RecordParseException.class)
    public void cantParseNonNumericString() {
        final FakeField field = new FakeField();
        field.convertStringToMonetaryAmount("something");
    }
}
