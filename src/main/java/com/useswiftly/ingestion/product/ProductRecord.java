package com.useswiftly.ingestion.product;

import com.useswiftly.ingestion.records.Record;
import org.jetbrains.annotations.Nullable;

import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.StringJoiner;
import java.util.function.Function;

/**
 *
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
    private final Function<ProductRecordFlags, UnitOfMeasure> unitOfMeasureDeciderFunction;

    public ProductRecord(final Function<ProductRecordFlags, BigDecimal> taxRateCalculator,
                         final Function<ProductRecordFlags, UnitOfMeasure> unitOfMeasureDeciderFunction) {
        this.taxRateCalculatorFunction = taxRateCalculator;
        this.unitOfMeasureDeciderFunction = unitOfMeasureDeciderFunction;
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
        this.regularForX = regularForX;
        return this;
    }

    @Nullable
    public BigInteger getPromotionalForX() {
        return promotionalForX;
    }

    public ProductRecord setPromotionalForX(@Nullable final BigInteger promotionalForX) {
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

    public MonetaryAmount calculateDisplayPrice() {
        throw new UnsupportedOperationException();
    }

    public MonetaryAmount calculateRegularCalculatorPrice() {
        throw new UnsupportedOperationException();
    }

    public MonetaryAmount cacluatePromotionalDisplayPrice() {
        throw new UnsupportedOperationException();
    }

    public MonetaryAmount calculatePromotionalCalculatorPrice() {
        throw new UnsupportedOperationException();
    }

    @Nullable
    public UnitOfMeasure deriveUnitOfMeasure() {
        return unitOfMeasureDeciderFunction.apply(getFlags());
    }

    /**
     * Retrieves the <em>current</em> associated tax rate for this item. This
     * value may change at any time and caution should be paid if considering
     * to persist it.
     */
    @Nullable
    public BigDecimal calculateTaxRate() {
        return taxRateCalculatorFunction.apply(getFlags());
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
                .toString();
    }
}
