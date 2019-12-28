package com.useswiftly.ingestion.product;

import com.useswiftly.ingestion.records.Record;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * @author <a href="https://github.com/dekobon">Elijah Zupancic</a>
 * @since 3.0.0
 */
public interface ProductRecord extends Record {
    @Nullable BigInteger getProductId();

    ProductRecord setProductId(@Nullable BigInteger productId);

    @Nullable String getProductDescription();

    ProductRecord setProductDescription(@Nullable String productDescription);

    @Nullable MonetaryAmount getRegularSingularPrice();

    ProductRecord setRegularSingularPrice(@Nullable MonetaryAmount regularSingularPrice);

    @Nullable MonetaryAmount getPromotionalSingularPrice();

    ProductRecord setPromotionalSingularPrice(@Nullable MonetaryAmount promotionalSingularPrice);

    @Nullable MonetaryAmount getRegularSplitPrice();

    ProductRecord setRegularSplitPrice(@Nullable MonetaryAmount regularSplitPrice);

    @Nullable MonetaryAmount getPromotionalSplitPrice();

    ProductRecord setPromotionalSplitPrice(@Nullable MonetaryAmount promotionalSplitPrice);

    @Nullable BigInteger getRegularForX();

    ProductRecord setRegularForX(@Nullable BigInteger regularForX);

    @Nullable BigInteger getPromotionalForX();

    ProductRecord setPromotionalForX(@Nullable BigInteger promotionalForX);

    @Nullable ProductRecordFlags getFlags();

    ProductRecord setFlags(@Nullable ProductRecordFlags flags);

    @Nullable String getProductSize();

    ProductRecord setProductSize(@Nullable String productSize);

    @NotNull String regularDisplayPrice();

    @Nullable MonetaryAmount calculateRegularCalculatorPrice();

    @Nullable String promotionalDisplayPrice();

    @Nullable MonetaryAmount calculatePromotionalCalculatorPrice();

    /**
     * Determines if the unit of measure for the current product record. See
     * {@link com.useswiftly.ingestion.product.functions.DeriveUnitOfMeasureFunction}
     * for the relevant business logic.
     *
     * @return null if unknown or enum representing the unit of measure associated with the current record
     */
    @Nullable UnitOfMeasure deriveUnitOfMeasure();

    /**
     * Retrieves the <em>current</em> associated tax rate for this item. This
     * value may change at any time and caution should be paid if considering
     * to persist it. See
     * {@link com.useswiftly.ingestion.product.functions.CalculateTaxRateFunction}
     * for the relevant business logic.
     *
     * @return null if unknown or tax rate as a decimal value
     */
    @Nullable BigDecimal calculateTaxRate();
}
