package com.useswiftly.ingestion.records;

import org.jetbrains.annotations.NotNull;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.math.BigInteger;

@Test
public class BigIntegerFieldTest {
    private static class FakeRecord implements Record {
    }

    private static class FakeField extends BigIntegerField<FakeRecord> {
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
        public void convertAndAssignValueToRecord(@NotNull final String substring,
                                                  @NotNull final FakeRecord record) {
            throw new UnsupportedOperationException("Not implemented for test");
        }
    }

    public void canConvertPositiveNumericStringToBigInteger() {
        final String original = "99999999";
        final BigInteger expected = BigInteger.valueOf(99999999L);

        final FakeField field = new FakeField();
        final BigInteger actual = field.convertStringToBigInteger(original);

        Assert.assertEquals(actual, expected,
                "Didn't convert String to BigInteger as expected");
    }

    public void canConvertNegativeNumericStringToBigInteger() {
        final String original = "-12";
        final BigInteger expected = BigInteger.valueOf(-12L);

        final FakeField field = new FakeField();
        final BigInteger actual = field.convertStringToBigInteger(original);

        Assert.assertEquals(actual, expected,
                "Didn't convert String to BigInteger as expected");
    }

    public void canConvertPositiveNumericStringWithLeadingZerosToBigInteger() {
        final String original = "00000349";
        final BigInteger expected = BigInteger.valueOf(349L);

        final FakeField field = new FakeField();
        final BigInteger actual = field.convertStringToBigInteger(original);

        Assert.assertEquals(actual, expected,
                "Didn't convert String to BigInteger as expected");
    }

    public void canConvertNegativeNumericStringWithLeadingZerosToBigInteger() {
        final String original = "-0000349";
        final BigInteger expected = BigInteger.valueOf(-349L);

        final FakeField field = new FakeField();
        final BigInteger actual = field.convertStringToBigInteger(original);

        Assert.assertEquals(actual, expected,
                "Didn't convert String to BigInteger as expected");
    }

    @Test(expectedExceptions = RecordParseException.class)
    public void cantParseNonNumericString() {
        final FakeField field = new FakeField();
        field.convertStringToBigInteger("Something");
    }
}
