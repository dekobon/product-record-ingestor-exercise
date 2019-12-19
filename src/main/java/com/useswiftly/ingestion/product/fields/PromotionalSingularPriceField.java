package com.useswiftly.ingestion.product.fields;

import com.useswiftly.ingestion.product.ProductRecord;
import com.useswiftly.ingestion.records.MonetaryField;
import org.jetbrains.annotations.NotNull;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;

/**
 *
 */
public class PromotionalSingularPriceField extends MonetaryField<ProductRecord> {
    public PromotionalSingularPriceField(final CurrencyUnit currencyUnit) {
        super(currencyUnit);
    }

    @Override
    public int getStartPositionInclusive() {
        return 78;
    }

    @Override
    public int getEndPositionExclusive() {
        return 86;
    }

    @Override
    public String getName() {
        return "Promotional Singular Price";
    }

    @Override
    public void convertAndAssignValueToRecord(@NotNull final String substring, @NotNull final ProductRecord record) {
        final MonetaryAmount price = convertStringToMonetaryAmount(substring);
        record.setPromotionalSingularPrice(price);
    }
}
