package com.useswiftly.ingestion.product.fields;

import com.useswiftly.ingestion.product.ProductRecord;
import com.useswiftly.ingestion.product.ProductRecordFlags;
import com.useswiftly.ingestion.records.RecordParseException;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Test
public class FlagsFieldTest {
    public void canConvertAllTrue() {
        final FlagsField flagsField = new FlagsField();
        final char[] ys = new char[ProductRecordFlags.FLAG_COUNT];
        Arrays.fill(ys, 'Y');

        final ProductRecord record = mock(ProductRecord.class);
        when(record.setFlags(any(ProductRecordFlags.class)))
                .then(invocationOnMock -> {
            final ProductRecordFlags flags = invocationOnMock.getArgument(0);

            Assert.assertNotNull(flags);
            Assert.assertEquals(flags.getSize(), ProductRecordFlags.FLAG_COUNT,
                    "Unexpected number of flags");

            for (Boolean flag : flags) {
                Assert.assertTrue(flag);
            }

            return null;
        });
        verify(record, atMostOnce()).setFlags(any(ProductRecordFlags.class));

        flagsField.convertAndAssignValueToRecord(String.valueOf(ys), record);
    }

    public void canConvertAllFalse() {
        final FlagsField flagsField = new FlagsField();
        final char[] ys = new char[ProductRecordFlags.FLAG_COUNT];
        Arrays.fill(ys, 'N');

        final ProductRecord record = mock(ProductRecord.class);
        when(record.setFlags(any(ProductRecordFlags.class)))
                .then(invocationOnMock -> {
                    final ProductRecordFlags flags = invocationOnMock.getArgument(0);

                    Assert.assertNotNull(flags);
                    Assert.assertEquals(flags.getSize(), ProductRecordFlags.FLAG_COUNT,
                            "Unexpected number of flags");

                    for (Boolean flag : flags) {
                        Assert.assertFalse(flag);
                    }

                    return null;
                });
        verify(record, atMostOnce()).setFlags(any(ProductRecordFlags.class));

        flagsField.convertAndAssignValueToRecord(String.valueOf(ys), record);
    }

    @Test(expectedExceptions = RecordParseException.class)
    public void wontConvertBadCharacters() {
        final FlagsField flagsField = new FlagsField();
        final char[] ys = new char[ProductRecordFlags.FLAG_COUNT];
        ys[0] = 'n';

        final ProductRecord record = mock(ProductRecord.class);
        flagsField.convertAndAssignValueToRecord(String.valueOf(ys), record);
    }

    @Test(expectedExceptions = RecordParseException.class)
    public void wontConvertInvalidNumberOfFlags() {
        final FlagsField flagsField = new FlagsField();
        final char[] ys = new char[3];

        final ProductRecord record = mock(ProductRecord.class);
        flagsField.convertAndAssignValueToRecord(String.valueOf(ys), record);
    }
}
