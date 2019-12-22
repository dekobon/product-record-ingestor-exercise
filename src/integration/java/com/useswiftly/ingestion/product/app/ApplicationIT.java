package com.useswiftly.ingestion.product.app;

import com.google.inject.Injector;
import com.useswiftly.ingestion.product.ProductRecord;
import com.useswiftly.ingestion.product.ProductRecordFlags;
import org.javamoney.moneta.FastMoney;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.money.CurrencyUnit;
import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Test
public class ApplicationIT {
    private ProductRecord instance(final long productId,
                                   final String description,
                                   final double regularSingularPrice,
                                   final double promotionalSingularPrice,
                                   final double regularSplitPrice,
                                   final double promotionalSplitPrice,
                                   final int regularForX,
                                   final int promotionalForX,
                                   final boolean isPerWeight,
                                   final boolean isTaxable,
                                   final String productSize) {
        final ProductRecordFlags flags = new ProductRecordFlags()
                .setPerWeightItem(isPerWeight).setTaxableItem(isTaxable);
        return instance(productId, description, regularSingularPrice,
                promotionalSingularPrice, regularSplitPrice, promotionalSplitPrice,
                regularForX, promotionalForX, flags, productSize);
    }

    private ProductRecord instance(final long productId,
                                   final String description,
                                   final double regularSingularPrice,
                                   final double promotionalSingularPrice,
                                   final double regularSplitPrice,
                                   final double promotionalSplitPrice,
                                   final int regularForX,
                                   final int promotionalForX,
                                   final ProductRecordFlags flags,
                                   final String productSize) {
        final Injector injector = Application.injector;
        final CurrencyUnit currencyUnit = injector.getInstance(CurrencyUnit.class);

        return injector.getInstance(ProductRecord.class)
                .setProductId(BigInteger.valueOf(productId))
                .setProductDescription(description)
                .setRegularSingularPrice(FastMoney.of(regularSingularPrice, currencyUnit))
                .setPromotionalSingularPrice(FastMoney.of(promotionalSingularPrice, currencyUnit))
                .setRegularSplitPrice(FastMoney.of(regularSplitPrice, currencyUnit))
                .setPromotionalSplitPrice(FastMoney.of(promotionalSplitPrice, currencyUnit))
                .setRegularForX(BigInteger.valueOf(regularForX))
                .setPromotionalForX(BigInteger.valueOf(promotionalForX))
                .setFlags(flags)
                .setProductSize(productSize);
    }

    public void canParseExampleTestFile() throws Exception {
        final ProductRecordFlags marlboroFlags = new ProductRecordFlags();
        marlboroFlags.setFlagAtPosition(0, true);

        final List<ProductRecord> expected = List.of(
                instance(80000001, "Kimchi-flavored white rice", 5.67,
                        0, 0, 0, 0, 0,
                        false, false, "18oz"),
                instance(14963801, "Generic Soda 12-pack", 0,
                        5.49, 13.00, 0, 2, 0,
                        false, true, "12x12oz"),
                instance(40123401, "Marlboro Cigarettes", 10.00, 5.49,
                        0, 0, 0, 0,
                        marlboroFlags, null),
                instance(50133333, "Fuji Apples (Organic)", 3.49, 0,
                        0, 0, 0, 0, true,
                        false, "lb")
        );

        final Path recordsFile = Paths.get(ClassLoader.getSystemResource(
                "input-sample.txt").toURI());

        try (Stream<ProductRecord> stream = Application.parsePathForRecordsData(recordsFile)) {
            final List<ProductRecord> actual =  stream.collect(
                    Collectors.toUnmodifiableList());

            Assert.assertEquals(actual, expected);
        }
    }
}
