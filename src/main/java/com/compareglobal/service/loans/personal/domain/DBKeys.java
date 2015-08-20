package com.compareglobal.service.loans.personal.domain;

/**
 * Created by Luis Miguel Osorio.
 *
 * Unify every single database key in a single common enum class
 */
public enum DBKeys {

    ProductImage("product_icon"),
    ProductLink("product_link"),
    HandlingFeeType("handling_fee_type"),
    InterestProfile("interestProfile"),
    InterestRateMonthly("interestRateMonthly"),
    MinHandlingFee("minHandlingFee"),
    MaxLoanAmount("maxLoanAmount"),
    MaxLoanTenure("maxLoanTenure"),
    MinLoanAmount("minLoanAmount"),
    MinLoanTenure("minLoanTenure"),
    LowestApr("lowestApr"),
    FirstYearPercentageHandlingFee("1stYearPercentageHandlingFee"),
    FirstYearFixedHandlingFee("1stYearFixedHanlingFee"),
    AnnualHandlingFee("annualHandlingFee"),
    Link("link"),
    TypeValue("typeValue"),
    OnlineOffer("hasExclusiveOnlineOffer"),
    OnlineOfferText("exclusiveOnlineOfferText"),
    ApplyButtonFlag("hasApplyBtn"),
    MobileLink("product_mobile_link"),
    HandlingFeeWaiverLogo("handlingfeewaiverlogo"),
    PromoFlag("hasPromo"),
    PromoTag("promoTag"),
    TotalTag("totalTag"),
    AprBasis("aprBasis"),
    ProductInfo("productInfo"),
    MonthlyRepaymentTag("monhtlyRepaymentTag"),
    KeyFeature("keyFeature"),
    InterestRate("interestRate"),
    Exclusive("exclusive"),
    BeAware("beAware"),
    Benefit("keyBenefit"),
    OpeningHandlingFee("openingHandlingFee"),
    OpeningHandlingFeePercentage("openingHandlingFeePercentage"),
    MonthlyHandlingFee("monthlyHandlingFee"),
    AnnualHandlingFeePercentage("annualHandlingFeePercentage"),
    InterestRateMonthlyMax("interestRateMonthlyMax"),
    MaxOpeningHandlingFee("maxOpeningHandlingFee"),
    MaxAnnualHandlingFee("maxAnnualHandlingFee"),
    MinMonthlyPayment("minMonthlyPayment"),
    MaxMonthlyPayment("maxMontlyPayment"),
    MinMonthlyPaymentPercent("minMonthlyPaymentPercent"),
    ComputationLogic("computationLogic"),
    EarlyRepaymentFee("earlyRepaymentfee");

    private final String key;

    DBKeys(final String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String toString() {
        return key;
    }

    public boolean equals(final String key) {
        return this.key.equals(key);
    }

    public boolean isPrefixOf(final String key) {
        return key.startsWith(this.key);
    }
}
