package com.useswiftly.ingestion.records;

import org.jetbrains.annotations.NotNull;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test
public class StringFieldTest {
    private static class FakeRecord implements Record {
    }

    private static class FakeField extends StringField<FakeRecord> {
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

    public void stringsWillBeTrimmed() {
        final String original = "   string value   ";

        final FakeField field = new FakeField();
        final String actual = field.convertSubstringToDisplayString(original);
        final String expected = "string value";

        Assert.assertEquals(actual, expected);
    }

    public void stringsEmptyAfterTrimWillBeNull() {
        final String original = "      ";

        final FakeField field = new FakeField();
        final String actual = field.convertSubstringToDisplayString(original);

        Assert.assertNull(actual);
    }
}
