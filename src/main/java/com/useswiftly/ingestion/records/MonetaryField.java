package com.useswiftly.ingestion.records;

import org.javamoney.moneta.FastMoney;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.math.BigDecimal;

/**
 * Record field with the data type {@link MonetaryAmount}.
 */
public abstract class MonetaryField<RECORD_TYPE> implements Field<MonetaryAmount, RECORD_TYPE> {
    private final CurrencyUnit currencyUnit;

    public MonetaryField(final CurrencyUnit currencyUnit) {
        this.currencyUnit = currencyUnit;
    }

    @Override
    public Class<MonetaryAmount> getType() {
        return MonetaryAmount.class;
    }

    public MonetaryAmount convertStringToMonetaryAmount(final String currencyString) {
        try {
            final BigDecimal amount = new BigDecimal(currencyString).movePointLeft(2);
            return Monetary.getAmountFactory(FastMoney.class)
                    .setCurrency(currencyUnit).setNumber(amount).create().stripTrailingZeros();
        } catch (RuntimeException e) {
               String msg = String.format("Problem parsing currency numeric " +
                               "string value as currency [%s]",
                       currencyString);
               throw new RecordParseException(msg);
        }
    }

    @Override
    public String toString() {
        return Field.toString(this);
    }
}
