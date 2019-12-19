package com.useswiftly.ingestion.product.functions;

import com.useswiftly.ingestion.product.ProductRecordFlags;
import com.useswiftly.ingestion.product.UnitOfMeasure;

import javax.inject.Named;
import java.util.function.Function;

/**
 *
 */
@Named("UnitOfMeasureDecider")
public class DeriveUnitOfMeasureFunction implements Function<ProductRecordFlags, UnitOfMeasure> {
    @Override
    public UnitOfMeasure apply(final ProductRecordFlags flags) {
        if (flags == null) {
            return null;
        } else if (flags.isPerWeightItem()) {
            return UnitOfMeasure.POUND;
        } else {
            return UnitOfMeasure.EACH;
        }
    }
}
