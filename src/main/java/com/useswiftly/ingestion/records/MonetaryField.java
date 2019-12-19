package com.useswiftly.ingestion.records;

import com.useswiftly.ingestion.product.fields.PromotionalSingularPriceField;
import org.javamoney.moneta.FastMoney;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.util.StringJoiner;

/**
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

    protected MonetaryAmount convertStringToMonetaryAmount(final String currencyString) {
        final BigDecimal amount = new BigDecimal(currencyString).movePointLeft(2);
        return Monetary.getAmountFactory(FastMoney.class)
                .setCurrency(currencyUnit).setNumber(amount).create().stripTrailingZeros();
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", PromotionalSingularPriceField.class.getSimpleName() + "[", "]")
                .add("startPositionInclusive=" + getStartPositionInclusive())
                .add("endPositionExclusive=" + getEndPositionExclusive())
                .add("name='" + getName() + "'")
                .add("type=" + getType())
                .toString();
    }
}
