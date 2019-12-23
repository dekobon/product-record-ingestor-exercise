package com.useswiftly.ingestion.product.functions;

import com.useswiftly.ingestion.product.ProductRecordFlags;
import com.useswiftly.ingestion.product.UnitOfMeasure;
import org.jetbrains.annotations.Nullable;

import javax.inject.Named;
import java.util.function.Function;

/**
 * Closure that determines the correct unit of measure for a product record.
 */
@Named("UnitOfMeasureDecider")
public class DeriveUnitOfMeasureFunction implements Function<ProductRecordFlags, UnitOfMeasure> {
    @Nullable
    @Override
    public UnitOfMeasure apply(@Nullable final ProductRecordFlags flags) {
        if (flags == null) {
            return null;
        } else if (flags.isPerWeightItem()) {
            return UnitOfMeasure.POUND;
        } else {
            return UnitOfMeasure.EACH;
        }
    }
}
