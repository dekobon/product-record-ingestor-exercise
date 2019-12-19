package com.useswiftly.ingestion.product.app;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import com.useswiftly.ingestion.product.ProductRecord;
import com.useswiftly.ingestion.product.ProductRecordFlags;
import com.useswiftly.ingestion.product.ProductRecordProvider;
import com.useswiftly.ingestion.product.UnitOfMeasure;
import com.useswiftly.ingestion.product.fields.FlagsField;
import com.useswiftly.ingestion.product.fields.ProductDescriptionField;
import com.useswiftly.ingestion.product.fields.ProductIdField;
import com.useswiftly.ingestion.product.fields.ProductSizeField;
import com.useswiftly.ingestion.product.fields.PromotionalForXField;
import com.useswiftly.ingestion.product.fields.PromotionalSingularPriceField;
import com.useswiftly.ingestion.product.fields.PromotionalSplitPriceField;
import com.useswiftly.ingestion.product.fields.RegularForXField;
import com.useswiftly.ingestion.product.fields.RegularSingularPriceField;
import com.useswiftly.ingestion.product.fields.RegularSplitPriceField;
import com.useswiftly.ingestion.product.functions.CalculateTaxRateFunction;
import com.useswiftly.ingestion.product.functions.DeriveUnitOfMeasureFunction;
import com.useswiftly.ingestion.records.Field;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.function.Function;

/**
 * This implementation of {@link Module} provides dependency injection for the
 * application using Google Guice.
 */
public class ProductRecordIngestorModule implements Module {
    public ProductRecordIngestorModule() {
    }

    @Override
    public void configure(final Binder binder) {
        // The character set for the parse can be configured here
        binder.bind(Charset.class).toInstance(StandardCharsets.US_ASCII);

        // The currency to use for the prices parsed from the record file can be configured here
        final CurrencyUnit currencyUnit = Monetary.getCurrency("USD");
        binder.bind(CurrencyUnit.class).toInstance(currencyUnit);

        // The tax rate settings can be configured here
        binder.bind(BigDecimal.class).annotatedWith(Names.named("TaxRate"))
                .toInstance(new BigDecimal("7.775"));

        // The logic for deciding how the tax rate is applied can be configured here
        binder.bind(new TypeLiteral<Function<ProductRecordFlags, BigDecimal>>(){})
                .annotatedWith(Names.named("TaxRateCalculator"))
                .to(CalculateTaxRateFunction.class)
                .in(Singleton.class);

        // The logic for deciding how the unit of measure is determined can be configured here
        binder.bind(new TypeLiteral<Function<ProductRecordFlags, UnitOfMeasure>>(){})
                .annotatedWith(Names.named("UnitOfMeasureDecider"))
                .to(DeriveUnitOfMeasureFunction.class)
                .in(Singleton.class);

        // The fields to parse from the record file can be configured and defined here
        final List<Field<?, ProductRecord>> fieldsToParse =
                List.of(
                        new ProductIdField(),
                        new ProductDescriptionField(),
                        new RegularSingularPriceField(currencyUnit),
                        new PromotionalSingularPriceField(currencyUnit),
                        new RegularSplitPriceField(currencyUnit),
                        new PromotionalSplitPriceField(currencyUnit),
                        new RegularForXField(),
                        new PromotionalForXField(),
                        new FlagsField(),
                        new ProductSizeField()
                );
        binder.bind(new TypeLiteral<List<Field<?, ProductRecord>>>(){})
                .toInstance(fieldsToParse);

        // Ensure that new ProductRecord objects are created by a provider
        binder.bind(ProductRecord.class).toProvider(ProductRecordProvider.class);
    }
}
