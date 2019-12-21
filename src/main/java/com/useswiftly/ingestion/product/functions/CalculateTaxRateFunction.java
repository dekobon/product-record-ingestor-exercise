package com.useswiftly.ingestion.product.functions;

import com.useswiftly.ingestion.product.ProductRecordFlags;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import java.math.BigDecimal;
import java.util.function.Function;

/**
 * Closure that calculates the applicable tax rate for a given
 * {@link com.useswiftly.ingestion.product.ProductRecord} based on the
 * record's {@link ProductRecordFlags}.
 */
@Named("TaxRateCalculator")
public class CalculateTaxRateFunction implements Function<ProductRecordFlags, BigDecimal> {
    private final Provider<BigDecimal> taxRateProvider;

    @Inject
    public CalculateTaxRateFunction(@Named("TaxRate") final Provider<BigDecimal> taxRateProvider) {
        this.taxRateProvider = taxRateProvider;
    }

    @Override
    public BigDecimal apply(@Nullable final ProductRecordFlags flags) {
        if (flags == null) {
            return null;
        } else if (flags.isTaxableItem()) {
            return taxRateProvider.get();
        } else {
            return new BigDecimal("0");
        }
    }
}
