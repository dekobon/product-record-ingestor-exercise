package com.useswiftly.ingestion.product;

import com.useswiftly.ingestion.records.Record;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.inject.Inject;
import javax.inject.Named;
import javax.money.MonetaryAmount;
import javax.money.MonetaryRounding;
import javax.money.format.MonetaryAmountFormat;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Function;

/**
 * Domain entity object that represents a single product record.
 */
@SuppressWarnings("UnusedReturnValue")
public class ProductRecord implements Record {
    private BigInteger productId;
    private String productDescription;
    private MonetaryAmount regularSingularPrice;
    private MonetaryAmount promotionalSingularPrice;
    private MonetaryAmount regularSplitPrice;
    private MonetaryAmount promotionalSplitPrice;
    private BigInteger regularForX;
    private BigInteger promotionalForX;
    private ProductRecordFlags flags;
    private String productSize;

    /**
     * Function that dynamically provides that tax rate when requested so that
     * the tax rate can be updated at runtime and does the tax rate calculation
     * as a closure so that the business logic is decoupled.
     */
    private final Function<ProductRecordFlags, BigDecimal> taxRateCalculatorFunction;

    /**
     * Function that dynamically determines what unit of measure is associated with
     * a given product record.
     */
    private final Function<ProductRecordFlags, UnitOfMeasure> unitOfMeasureDeciderFunction;

    private final MonetaryAmountFormat displayPriceFormat;

    private final MonetaryRounding rounding;

    @Inject
    public ProductRecord(@Named("TaxRateCalculator")
                         final Function<ProductRecordFlags, BigDecimal> taxRateCalculator,
                         @Named("UnitOfMeasureDecider")
                         final Function<ProductRecordFlags, UnitOfMeasure> unitOfMeasureDeciderFunction,
                         final MonetaryAmountFormat displayPriceFormat,
                         final MonetaryRounding rounding) {
        this.taxRateCalculatorFunction = taxRateCalculator;
        this.unitOfMeasureDeciderFunction = unitOfMeasureDeciderFunction;
        this.displayPriceFormat = displayPriceFormat;
        this.rounding = rounding;
    }

    @Nullable
    public BigInteger getProductId() {
        return productId;
    }

    public ProductRecord setProductId(@Nullable final BigInteger productId) {
        this.productId = productId;
        return this;
    }

    @Nullable
    public String getProductDescription() {
        return productDescription;
    }

    public ProductRecord setProductDescription(@Nullable final String productDescription) {
        this.productDescription = productDescription;
        return this;
    }

    @Nullable
    public MonetaryAmount getRegularSingularPrice() {
        return regularSingularPrice;
    }

    public ProductRecord setRegularSingularPrice(@Nullable final MonetaryAmount regularSingularPrice) {
        this.regularSingularPrice = regularSingularPrice;
        return this;
    }

    @Nullable
    public MonetaryAmount getPromotionalSingularPrice() {
        return promotionalSingularPrice;
    }

    public ProductRecord setPromotionalSingularPrice(@Nullable final MonetaryAmount promotionalSingularPrice) {
        this.promotionalSingularPrice = promotionalSingularPrice;
        return this;
    }

    @Nullable
    public MonetaryAmount getRegularSplitPrice() {
        return regularSplitPrice;
    }

    public ProductRecord setRegularSplitPrice(@Nullable final MonetaryAmount regularSplitPrice) {
        this.regularSplitPrice = regularSplitPrice;
        return this;
    }

    @Nullable
    public MonetaryAmount getPromotionalSplitPrice() {
        return promotionalSplitPrice;
    }

    public ProductRecord setPromotionalSplitPrice(@Nullable final MonetaryAmount promotionalSplitPrice) {
        this.promotionalSplitPrice = promotionalSplitPrice;
        return this;
    }

    @Nullable
    public BigInteger getRegularForX() {
        return regularForX;
    }

    public ProductRecord setRegularForX(@Nullable final BigInteger regularForX) {
        if (regularForX != null && BigInteger.ZERO.compareTo(regularForX) > 0) {
            throw new IllegalArgumentException("For X values must be zero or greater");
        }

        this.regularForX = regularForX;
        return this;
    }

    @Nullable
    public BigInteger getPromotionalForX() {
        return promotionalForX;
    }

    public ProductRecord setPromotionalForX(@Nullable final BigInteger promotionalForX) {
        if (regularForX != null && BigInteger.ZERO.compareTo(regularForX) > 0) {
            throw new IllegalArgumentException("For X values must be zero or greater");
        }

        this.promotionalForX = promotionalForX;
        return this;
    }

    @Nullable
    public ProductRecordFlags getFlags() {
        return flags;
    }

    public ProductRecord setFlags(@Nullable final ProductRecordFlags flags) {
        this.flags = flags;
        return this;
    }

    @Nullable
    public String getProductSize() {
        return productSize;
    }

    public ProductRecord setProductSize(@Nullable final String productSize) {
        this.productSize = productSize;
        return this;
    }

    /* ====================================================================== *\
     * The below methods calculate their values based on data already stored in
     * this object and/or providers and are not persisted within this object.
     * ====================================================================== */

    @NotNull
    public String regularDisplayPrice() {
        /* If either price is null, it indicates that we don't have a properly
         * populated ProductRecord object and it is likely a test instance.
         * In these cases, we just return a "unknown" string because we aren't
         * following the expected contract. */
        if (getRegularSingularPrice() == null || getRegularSplitPrice() == null) {
            return "unknown";
        }

        /* Both prices being zero is in violation of the contract we have for a
         * valid input format. */
        if (getRegularSingularPrice().isZero() && getRegularSplitPrice().isZero()) {
            throw new IllegalStateException("Either singular price or split " +
                    "price must be enabled");
        }

        // A non-zero singular price will be displayed as-is
        if (!getRegularSingularPrice().isZero()) {
            return displayPriceFormat
                    .format(getRegularSingularPrice().with(rounding));
        // A X for <amount> price will be formatted and displayed
        }

        if (!getRegularSplitPrice().isZero()) {
            String formatted = displayPriceFormat.format(
                    getRegularSplitPrice().with(rounding));
            // Perhaps, this format should be externalized for localization
            return String.format("%d for %s", getRegularForX(), formatted);
        }

        throw new IllegalStateException("Either singular price or split " +
                "price must be enabled - neither were enabled");
    }

    @Nullable
    public MonetaryAmount calculateRegularCalculatorPrice() {
        /* If either price is null, it indicates that we don't have a properly
         * populated ProductRecord object and it is likely a test instance.
         * In these cases, we just return a null because we aren't
         * following the expected contract. */
        if (getRegularSingularPrice() == null || getRegularSplitPrice() == null) {
            return null;
        }

        /* Both prices being zero is in violation of the contract we have for a
         * valid input format. */
        if (getRegularSingularPrice().isPositive() && getRegularSplitPrice().isPositive()) {
            throw new IllegalStateException("Either singular price or split " +
                    "price must be enabled");
        }

        final MonetaryAmount calculatedPrice;

        if (!getRegularSingularPrice().isZero()) {
            calculatedPrice = getRegularSingularPrice().with(rounding);
        } else if (!getRegularSplitPrice().isZero()) {
            Objects.requireNonNull(getRegularForX(), "Regular for " +
                    "X must not be null when split price is present");

            calculatedPrice = getRegularSplitPrice()
                    .divide(getRegularForX()).with(rounding).stripTrailingZeros();
        } else {
            throw new IllegalStateException("Either singular price or split " +
                    "price must be enabled - neither were enabled");
        }

        return calculatedPrice;
    }

    @Nullable
    public MonetaryAmount calculatePromotionalDisplayPrice() {
        throw new UnsupportedOperationException();
    }

    @Nullable
    public MonetaryAmount calculatePromotionalCalculatorPrice() {
        throw new UnsupportedOperationException();
    }

    /**
     * Determines if the unit of measure for the current product record. See
     * {@link com.useswiftly.ingestion.product.functions.DeriveUnitOfMeasureFunction}
     * for the relevant business logic.
     *
     * @return null if unknown or enum representing the unit of measure associated with the current record
     */
    @Nullable
    public UnitOfMeasure deriveUnitOfMeasure() {
        if (unitOfMeasureDeciderFunction != null) {
            return unitOfMeasureDeciderFunction.apply(getFlags());
        } else {
            return null;
        }
    }

    /**
     * Retrieves the <em>current</em> associated tax rate for this item. This
     * value may change at any time and caution should be paid if considering
     * to persist it. See
     * {@link com.useswiftly.ingestion.product.functions.CalculateTaxRateFunction}
     * for the relevant business logic.
     *
     * @return null if unknown or tax rate as a decimal value
     */
    @Nullable
    public BigDecimal calculateTaxRate() {
        if (taxRateCalculatorFunction != null) {
            return taxRateCalculatorFunction.apply(getFlags());
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ProductRecord.class.getSimpleName() + "[", "]")
                .add("productId=" + productId)
                .add("productDescription='" + productDescription + "'")
                .add("regularSingularPrice=" + regularSingularPrice)
                .add("promotionalSingularPrice=" + promotionalSingularPrice)
                .add("regularSplitPrice=" + regularSplitPrice)
                .add("promotionalSplitPrice=" + promotionalSplitPrice)
                .add("regularForX=" + regularForX)
                .add("promotionalForX=" + promotionalForX)
                .add("flags=" + flags)
                .add("productSize='" + productSize + "'")
                .add("unitOfMeasure=" + deriveUnitOfMeasure())
                .add("taxRate=" + calculateTaxRate())
                .add("displayPrice=" + regularDisplayPrice())
                .add("regularCalculatorPrice=" + calculateRegularCalculatorPrice())
                .toString();
    }
}
