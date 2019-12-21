package com.useswiftly.ingestion.product.fields;

import com.useswiftly.ingestion.product.ProductRecord;
import com.useswiftly.ingestion.records.MonetaryField;
import org.jetbrains.annotations.NotNull;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;

/**
 * Metadata for regular split price field.
 */
public class RegularSplitPriceField extends MonetaryField<ProductRecord> {
    public RegularSplitPriceField(final CurrencyUnit currencyUnit) {
        super(currencyUnit);
    }

    @Override
    public int getStartPositionInclusive() {
        return 87;
    }

    @Override
    public int getEndPositionExclusive() {
        return 95;
    }

    @Override
    public String getName() {
        return "Regular Split Price";
    }

    @Override
    public void convertAndAssignValueToRecord(@NotNull final String substring, @NotNull final ProductRecord record) {
        final MonetaryAmount price = convertStringToMonetaryAmount(substring);
        record.setRegularSplitPrice(price);
    }
}
