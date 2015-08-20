package com.compareglobal.service.loans.personal.view.sg;

import com.compareglobal.service.loans.personal.domain.Mortgage;
import com.compareglobal.service.loans.personal.domain.sg.InterestProfileSG;

/**
 * Created by dennis on 5/12/15.
 */
public class MortgageSG extends Mortgage {

    private static final int MONTHS_IN_YEAR = 12;
    private InterestProfileSG categoryProfile;
    private double lowestApr;
    private double minMonthlyPaymentPercent;
    private double minMonthlyPayment;
    private double minHandlingFeePercent;
    private double minHandlingFeeAmount;
    private double totalHandlingFee;
    private double effectiveInterestRate;
    private double annualInterestRate;
    private long maxTenure;
    private String aprBasis;
    private String annualRateText;
    private String effectiveInterestRateCombined;
    private String maxBalanceTrfTenure;
    private double lowestInterestRate;
    private double fir2Years;

    public InterestProfileSG getCategoryProfile() {
        return categoryProfile;
    }

    public void setCategoryProfile(InterestProfileSG categoryProfile) {
        this.categoryProfile = categoryProfile;
    }

    public void setLowestApr(double lowestApr) {
        this.lowestApr = lowestApr;
    }

    public double getLowestApr() {
        return lowestApr;
    }

    public double getTotalInterest() {
        return this.getPrincipalLoanAmount() * (this.getTenureInMonths() / MONTHS_IN_YEAR) * this.getAnnualInterestRate();
    }

    public double getMinMonthlyPaymentPercent() {
        return minMonthlyPaymentPercent;
    }

    public void setMinMonthlyPaymentPercent(double minMonthlyPaymentPercent) {
        this.minMonthlyPaymentPercent = minMonthlyPaymentPercent;
    }

    public void setMinMonthlyPayment(double minMonthlyPayment) {
        this.minMonthlyPayment = minMonthlyPayment;
    }

    public double getMinMonthlyPayment() {
        return minMonthlyPayment;
    }

    public double getPercentMonthlyPayment() {
        return minMonthlyPaymentPercent * getPrincipalLoanAmount();
    }

    public double getInstalmentMonthlyPayment() {
        double interestRate = this.getAnnualInterestRate();
        double loanAmount = this.getPrincipalLoanAmount();
        double loanTenure = this.getTenureInMonths();
	    double totalInterest = loanAmount * (loanTenure / MONTHS_IN_YEAR) * interestRate;
        double totalRepayment = totalInterest + loanAmount;
        if (totalRepayment > 0) {
            return  totalRepayment / loanTenure;
        }
        return 0;
    }

    public double getCreditLineMonthlyPayment() {
        double percentPayment = this.getPercentMonthlyPayment();
        return percentPayment > this.getMinMonthlyPayment()
                ? percentPayment : this.getMinMonthlyPayment();
    }

    public void setMinHandlingFeePercent(double minHandlingFeePercent) {
        this.minHandlingFeePercent = minHandlingFeePercent;
    }

    public double getMinHandlingFeePercent() {
        return minHandlingFeePercent;
    }

    public double getMinHandlingFeePercentAmount() {
        return minHandlingFeePercent * this.getPrincipalLoanAmount();
    }

    public void setMinHandlingFeeAmount(double minHandlingFeeAmount) {
        this.minHandlingFeeAmount = minHandlingFeeAmount;
    }

    public double getMinHandlingFeeAmount() {
        return minHandlingFeeAmount;
    }

    public double getBalanceTransferMonthlyPayment() {
        return this.getMinHandlingFeePercentAmount() > minHandlingFeeAmount
                ? this.getMinHandlingFeePercentAmount() : minHandlingFeeAmount;
    }


    public void setEffectiveInterestRate(double effectiveInterestRate) {
        this.effectiveInterestRate = effectiveInterestRate;
    }

    public double getEffectiveInterestRate() {
        return effectiveInterestRate;
    }

    public void setAnnualInterestRate(double annualInterestRate) {
        this.annualInterestRate = annualInterestRate;
    }

    public double getAnnualInterestRate() {
        if (annualInterestRate == 0
                && minMonthlyPaymentPercent > 0) {
            return minMonthlyPaymentPercent;
        }
        return annualInterestRate;
    }

    public void setMaxTenure(long maxTenure) {
        this.maxTenure = maxTenure;
    }

    public long getMaxTenure() {
        return maxTenure;
    }

    public void setAprBasis(String aprBasis) {
        this.aprBasis = aprBasis;
    }

    public String getAprBasis() {
        return aprBasis;
    }

    public void setAnnualRateText(String annualRateText) {
        this.annualRateText = annualRateText;
    }

    public String getAnnualRateText() {
        return annualRateText;
    }

    public void setEffectiveInterestRateCombined(String effectiveInterestRateCombined) {
        this.effectiveInterestRateCombined = effectiveInterestRateCombined;
    }

    public String getEffectiveInterestRateCombined() {
        return effectiveInterestRateCombined;
    }

    public void setMaxBalanceTrfTenure(String maxBalanceTrfTenure) {
        this.maxBalanceTrfTenure = maxBalanceTrfTenure;
    }

    public String getMaxBalanceTrfTenure() {
        return maxBalanceTrfTenure;
    }

    public double getLowestInterestRate() {
        return getAnnualInterestRate() * 100;
    }

    /* Add Fir_2 */
    public void setFir2Years(double fir2Years) {
        this.fir2Years = fir2Years;
    }

    public double getFir2Years() {
        return fir2Years;
    }

    /* Add Total Handling Fee */
    public void setTotalHandlingFee(double totalHandlingFee) {
        this.totalHandlingFee = totalHandlingFee;
    }

    public double getTotalHandlingFee() {
      return this.getMinHandlingFeePercentAmount() > minHandlingFeeAmount
              ? this.getMinHandlingFeePercentAmount() : minHandlingFeeAmount;
    }

}
