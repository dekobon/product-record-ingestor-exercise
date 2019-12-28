package com.useswiftly.ingestion.product;

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
public class ProductRecordImpl implements ProductRecord {
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
    public ProductRecordImpl(@Named("TaxRateCalculator")
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

    @Override
    @Nullable
    public BigInteger getProductId() {
        return productId;
    }

    @Override
    public ProductRecordImpl setProductId(@Nullable final BigInteger productId) {
        this.productId = productId;
        return this;
    }

    @Override
    @Nullable
    public String getProductDescription() {
        return productDescription;
    }

    @Override
    public ProductRecordImpl setProductDescription(@Nullable final String productDescription) {
        this.productDescription = productDescription;
        return this;
    }

    @Override
    @Nullable
    public MonetaryAmount getRegularSingularPrice() {
        return regularSingularPrice;
    }

    @Override
    public ProductRecordImpl setRegularSingularPrice(@Nullable final MonetaryAmount regularSingularPrice) {
        this.regularSingularPrice = regularSingularPrice;
        return this;
    }

    @Override
    @Nullable
    public MonetaryAmount getPromotionalSingularPrice() {
        return promotionalSingularPrice;
    }

    @Override
    public ProductRecordImpl setPromotionalSingularPrice(@Nullable final MonetaryAmount promotionalSingularPrice) {
        this.promotionalSingularPrice = promotionalSingularPrice;
        return this;
    }

    @Override
    @Nullable
    public MonetaryAmount getRegularSplitPrice() {
        return regularSplitPrice;
    }

    @Override
    public ProductRecordImpl setRegularSplitPrice(@Nullable final MonetaryAmount regularSplitPrice) {
        this.regularSplitPrice = regularSplitPrice;
        return this;
    }

    @Override
    @Nullable
    public MonetaryAmount getPromotionalSplitPrice() {
        return promotionalSplitPrice;
    }

    @Override
    public ProductRecordImpl setPromotionalSplitPrice(@Nullable final MonetaryAmount promotionalSplitPrice) {
        this.promotionalSplitPrice = promotionalSplitPrice;
        return this;
    }

    @Override
    @Nullable
    public BigInteger getRegularForX() {
        return regularForX;
    }

    @Override
    public ProductRecordImpl setRegularForX(@Nullable final BigInteger regularForX) {
        this.regularForX = regularForX;
        return this;
    }

    @Override
    @Nullable
    public BigInteger getPromotionalForX() {
        return promotionalForX;
    }

    @Override
    public ProductRecordImpl setPromotionalForX(@Nullable final BigInteger promotionalForX) {
        this.promotionalForX = promotionalForX;
        return this;
    }

    @Override
    @Nullable
    public ProductRecordFlags getFlags() {
        return flags;
    }

    @Override
    public ProductRecordImpl setFlags(@Nullable final ProductRecordFlags flags) {
        this.flags = flags;
        return this;
    }

    @Override
    @Nullable
    public String getProductSize() {
        return productSize;
    }

    @Override
    public ProductRecordImpl setProductSize(@Nullable final String productSize) {
        this.productSize = productSize;
        return this;
    }

    /* ====================================================================== *\
     * The below methods calculate their values based on data already stored in
     * this object and/or providers and are not persisted within this object.
     * ====================================================================== */

    @Override
    @NotNull
    public String regularDisplayPrice() {
        return computeDisplayPrice(getRegularSingularPrice(),
                getRegularSplitPrice(), getRegularForX());
    }

    @Override
    @Nullable
    public MonetaryAmount calculateRegularCalculatorPrice() {
        return computeCalculatorPrice(getRegularSingularPrice(),
                getRegularSplitPrice(), getRegularForX());
    }

    @Override
    @Nullable
    public String promotionalDisplayPrice() {
        return computeDisplayPrice(getPromotionalSingularPrice(),
                getPromotionalSplitPrice(), getPromotionalForX());
    }

    @Override
    @Nullable
    public MonetaryAmount calculatePromotionalCalculatorPrice() {
        return computeCalculatorPrice(getPromotionalSingularPrice(),
                getPromotionalSplitPrice(), getPromotionalForX());
    }

    @Override
    @Nullable
    public UnitOfMeasure deriveUnitOfMeasure() {
        if (unitOfMeasureDeciderFunction != null) {
            return unitOfMeasureDeciderFunction.apply(getFlags());
        } else {
            return null;
        }
    }

    @Override
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
        return new StringJoiner(", ", ProductRecordImpl.class.getSimpleName() + "[", "]")
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
                .add("regularDisplayPrice=" + regularDisplayPrice())
                .add("regularCalculatorPrice=" + calculateRegularCalculatorPrice())
                .add("promotionalDisplayPrice=" + promotionalDisplayPrice())
                .add("promotionalCalculatorPrice=" + calculatePromotionalCalculatorPrice())
                .toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ProductRecordImpl record = (ProductRecordImpl) o;
        return Objects.equals(productId, record.productId) &&
                Objects.equals(productDescription, record.productDescription) &&
                Objects.equals(regularSingularPrice, record.regularSingularPrice) &&
                Objects.equals(promotionalSingularPrice, record.promotionalSingularPrice) &&
                Objects.equals(regularSplitPrice, record.regularSplitPrice) &&
                Objects.equals(promotionalSplitPrice, record.promotionalSplitPrice) &&
                Objects.equals(regularForX, record.regularForX) &&
                Objects.equals(promotionalForX, record.promotionalForX) &&
                Objects.equals(flags, record.flags) &&
                Objects.equals(productSize, record.productSize);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, productDescription, regularSingularPrice,
                promotionalSingularPrice, regularSplitPrice,
                promotionalSplitPrice, regularForX, promotionalForX, flags,
                productSize);
    }

    /**
     * Chooses the appropriate price between the specified singular price
     * and split price.
     */
    @NotNull
    protected MonetaryAmount selectApplicablePrice(@NotNull final MonetaryAmount singularPrice,
                                                   @NotNull final MonetaryAmount splitPrice) {
        final MonetaryAmount amount;

        if (singularPrice.isZero() && splitPrice.isZero()) {
            // since both prices are zero, we just return one of them
            amount = singularPrice;
        } else if (!singularPrice.isZero()) {
            amount = singularPrice.with(rounding);
        } else if (!splitPrice.isZero()) {
            amount = splitPrice.with(rounding);
        } else {
            String msg = String.format("Either singular price [%s] or split " +
                    "price [%s] must be zero", singularPrice, splitPrice);
            throw new IllegalStateException(msg);
        }

        return amount;
    }

    /**
     * Renders a price to display based on the supplied singular price,
     * split price and forX parameters.
     */
    @NotNull
    protected String computeDisplayPrice(@Nullable final MonetaryAmount singularPrice,
                                         @Nullable final MonetaryAmount splitPrice,
                                         @Nullable final BigInteger forX) {
        /* If either price is null, it indicates that we don't have a properly
         * populated ProductRecord object and it is likely a test instance.
         * In these cases, we just return a "unknown" string because we aren't
         * following the expected contract. */
        if (singularPrice == null || splitPrice == null) {
            return "unknown";
        }

        final MonetaryAmount amount = selectApplicablePrice(singularPrice, splitPrice);

        // Price formatted to a friendly string with a currency symbol
        final String formattedPrice = displayPriceFormat.format(amount);

        // End price to display to users
        final String displayPrice;

        // If forX is greater than zero
        if (forX != null && BigInteger.ZERO.compareTo(forX) < 0) {
            // Perhaps, this format should be externalized for localization
            displayPrice = String.format("%d for %s", forX, formattedPrice);
        } else {
            displayPrice = formattedPrice;
        }

        return displayPrice;
    }

    /**
     * Calculates the appropriate price for a product based on the supplied
     * singular price, split price and forX parameters. The calculated price
     * is rounded to 4 decimals (opposed to the default MonetaryAmount of 5).
     */
    @Nullable
    protected MonetaryAmount computeCalculatorPrice(@Nullable final MonetaryAmount singularPrice,
                                                    @Nullable final MonetaryAmount splitPrice,
                                                    @Nullable final BigInteger forX) {
        /* If either price is null, it indicates that we don't have a properly
         * populated ProductRecord object and it is likely a test instance.
         * In these cases, we just return a null because we aren't
         * following the expected contract. */
        if (singularPrice == null || splitPrice == null) {
            return null;
        }

        final MonetaryAmount amount = selectApplicablePrice(singularPrice, splitPrice);
        final MonetaryAmount calculatorPrice;

        // Don't do any additional computation if the value is already zero
        if (amount.isZero()) {
            calculatorPrice = amount;
        // If forX is greater than zero
        } else if (forX != null && BigInteger.ZERO.compareTo(forX) < 0) {
            /* Assume that amount is the proper split price because we assume
             * the data file isn't corrupt - I really don't like this assumption
             * but it is fine for this exercise. */
            calculatorPrice = amount.divide(forX).with(rounding).stripTrailingZeros();
        } else {
            calculatorPrice = amount;
        }

        return calculatorPrice;
    }
}
