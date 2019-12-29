package com.useswiftly.ingestion.product;

import com.useswiftly.ingestion.records.RecordFormattable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;
import javax.money.MonetaryAmount;
import javax.money.format.MonetaryAmountFormat;
import java.math.BigInteger;
import java.util.StringJoiner;

/**
 * Formatter for {@link ProductRecord} instances that will display a single
 * record as a single line of easy to read text. This is a useful alternative
 * to the {@link ProductRecord#toString()} method because the output is minimal
 * and all prices are formatted with currency symbols.
 */
public class ProductRecordFormatter implements RecordFormattable<ProductRecord> {
    private final MonetaryAmountFormat displayPriceFormat;

    @Inject
    public ProductRecordFormatter(final MonetaryAmountFormat displayPriceFormat) {
        this.displayPriceFormat = displayPriceFormat;
    }

    @NotNull
    @Override
    public String format(@Nullable final ProductRecord record) {
        if (record == null) {
            return "<null>";
        }

        // We format null to something more human friendly
        final String productSize = record.getProductSize() == null ?
                "n/a" : record.getProductSize();

        return new StringJoiner(", ", "[", "]")
                .add("productId=" + formatProductId(record.getProductId()))
                .add("productDescription='" + record.getProductDescription() + "'")
                .add("regularDisplayPrice=" + record.regularDisplayPrice())
                .add("regularCalculatorPrice=" + formatPrice(record.calculateRegularCalculatorPrice()))
                .add("promotionalDisplayPrice=" + record.promotionalDisplayPrice())
                .add("promotionalCalculatorPrice=" + formatPrice(record.calculatePromotionalCalculatorPrice()))
                .add("unitOfMeasure=" + record.deriveUnitOfMeasure())
                .add("productSize='" + productSize + "'")
                .add("taxRate=" + record.calculateTaxRate())
                .toString();
    }

    @NotNull
    private String formatPrice(final MonetaryAmount amount) {
        if (amount == null) {
            return "null";
        }
        // Price formatted to a friendly string with a currency symbol
        return displayPriceFormat.format(amount);
    }

    @NotNull
    private String formatProductId(final BigInteger productId) {
        if (productId == null) {
            return "null";
        }

        return String.format("%08d", productId);
    }
}
