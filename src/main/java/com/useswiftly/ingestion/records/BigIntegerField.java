package com.useswiftly.ingestion.records;

import com.useswiftly.ingestion.product.fields.ProductIdField;

import java.math.BigInteger;
import java.util.StringJoiner;

/**
 *
 */
public abstract class BigIntegerField<RECORD_TYPE> implements Field<BigInteger, RECORD_TYPE> {
    public BigIntegerField() {
    }

    @Override
    public Class<BigInteger> getType() {
        return BigInteger.class;
    }

    protected BigInteger convertStringToBigInteger(final String numericString) {
        try {
            return new BigInteger(numericString);
        } catch (NumberFormatException e) {
            String msg = String.format("Unable to convert string to BigInteger " +
                            "for field [%s]. Offending String:\n%s",
                    getName(), numericString);
            throw new RecordParseException(msg, e);
        }
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ProductIdField.class.getSimpleName() + "[", "]")
                .add("startPositionInclusive=" + getStartPositionInclusive())
                .add("endPositionExclusive=" + getEndPositionExclusive())
                .add("name='" + getName() + "'")
                .add("type=" + getType())
                .toString();
    }
}
