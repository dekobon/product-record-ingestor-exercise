package com.useswiftly.ingestion.product.fields;

import com.useswiftly.ingestion.product.ProductRecord;
import com.useswiftly.ingestion.records.MonetaryField;
import org.jetbrains.annotations.NotNull;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;

/**
 * Metadata for promotional split price field.
 */
public class PromotionalSplitPriceField extends MonetaryField<ProductRecord> {
    public PromotionalSplitPriceField(final CurrencyUnit currencyUnit) {
        super(currencyUnit);
    }

    @Override
    public int getStartPositionInclusive() {
        return 96;
    }

    @Override
    public int getEndPositionExclusive() {
        return 104;
    }

    @Override
    public String getName() {
        return "Promotional Split Price";
    }

    @Override
    public void convertAndAssignValueToRecord(@NotNull final String substring, @NotNull final ProductRecord record) {
        final MonetaryAmount price = convertStringToMonetaryAmount(substring);
        record.setPromotionalSplitPrice(price);
    }
}
