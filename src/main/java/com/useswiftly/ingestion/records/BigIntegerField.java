package com.useswiftly.ingestion.records;

import java.math.BigInteger;

/**
 * Record field with the data type {@link BigInteger}.
 */
public abstract class BigIntegerField<RECORD_TYPE> implements Field<BigInteger, RECORD_TYPE> {
    public BigIntegerField() {
    }

    @Override
    public Class<BigInteger> getType() {
        return BigInteger.class;
    }

    public BigInteger convertStringToBigInteger(final String numericString) {
        try {
            return new BigInteger(numericString);
        } catch (RuntimeException e) {
            String msg = String.format("Unable to convert string to BigInteger " +
                            "for field [%s]. Offending String:\n%s",
                    getName(), numericString);
            throw new RecordParseException(msg, e);
        }
    }

    @Override
    public String toString() {
        return Field.toString(this);
    }
}
