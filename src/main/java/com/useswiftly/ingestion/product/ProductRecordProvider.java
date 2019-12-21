package com.useswiftly.ingestion.product;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import java.math.BigDecimal;
import java.util.function.Function;

/**
 * Class that provides configured instances of {@link ProductRecord}.
 */
public class ProductRecordProvider implements Provider<ProductRecord> {
    private final Function<ProductRecordFlags, BigDecimal> taxRateCalculatorFunction;
    private final Function<ProductRecordFlags, UnitOfMeasure> unitOfMeasureDeciderFunction;

    @Inject
    public ProductRecordProvider(
            @Named("TaxRateCalculator") Function<ProductRecordFlags, BigDecimal> taxRateCalculatorFunction,
            @Named("UnitOfMeasureDecider") Function<ProductRecordFlags, UnitOfMeasure> unitOfMeasureDeciderFunction) {
        this.taxRateCalculatorFunction = taxRateCalculatorFunction;
        this.unitOfMeasureDeciderFunction = unitOfMeasureDeciderFunction;
    }

    @Override
    public ProductRecord get() {
        return new ProductRecord(taxRateCalculatorFunction, unitOfMeasureDeciderFunction);
    }
}
