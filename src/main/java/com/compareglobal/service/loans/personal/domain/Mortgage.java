package com.compareglobal.service.loans.personal.domain;


/**
 * Created by dennis on 11/27/14.
 */
public class Mortgage {

    public static final int MONTHS_IN_A_YEAR = 12;
    private int handlingFeeType;
    private double principalLoanAmount;
    private int tenureInMonths;
    private double monthlyPayment;
    private double monthlyPaymentForTaxLoan;
    private Integer interestProfile;
    private double annualHandlingFee;
    private double minHandlingFee;
    private double monthlyInterestRate;
    private double apr;
    private double totalRepayment;
    private double creditLineAnnualInterestRate;
    private double creditLineMonthlyRepayment;
    private double creditLineDailyInterest;
    private boolean hasTaxLoan;
    private boolean hasPersonalInstallment;
    private double monthlyFee;
    private double fixedHandlingFee;
    private double monthlyInterestRateRaw;

    public boolean isHasPersonalInstallment() {
        return hasPersonalInstallment;
    }

    public void setHasPersonalInstallment(boolean hasPersonalInstallment) {
        this.hasPersonalInstallment = hasPersonalInstallment;
    }

    public double getMonthlyInterestRate() {
        if (monthlyInterestRate > 1) {
            return monthlyInterestRate / 100;
        }
        return monthlyInterestRate;
    }

    public void setMonthlyInterestRate(double defaultMonthlyInterestRate) {
        this.monthlyInterestRate = defaultMonthlyInterestRate;
        this.monthlyInterestRateRaw = defaultMonthlyInterestRate;
    }

    public double getMonthlyInterestRateRaw() {
        return monthlyInterestRateRaw;
    }

    public double getPrincipalLoanAmount() {
        return principalLoanAmount;
    }

    public void setPrincipalLoanAmount(double principalLoanAmount) {
        this.principalLoanAmount = principalLoanAmount;
    }

    public int getTenureInMonths() {
       return tenureInMonths;
    }

    public int getTenureInYears() {
        return tenureInMonths / MONTHS_IN_A_YEAR;
    }

    public void setTenureInMonths(int tenureInMonths) {
        this.tenureInMonths = tenureInMonths;
    }

    public double getMonthlyPayment() {
        return monthlyPayment;
    }

    public void setMonthlyPayment(double monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }

    public void setHandlingFeeType(int handlingFeeType) {
        this.handlingFeeType = handlingFeeType;
    }

    public int getHandlingFeeType() {
        return handlingFeeType;
    }

    public void setInterestProfile(Integer interestProfile) {
        this.interestProfile = interestProfile;
    }

    public Integer getInterestProfile() {
        return interestProfile;
    }

    public double getAnnualHandlingFee() { return annualHandlingFee; }

    public void setAnnualHandlingFee(double annualHandlingFee) { this.annualHandlingFee = annualHandlingFee; }

    public double getMinHandlingFee() { return minHandlingFee;  }

    public void setMinHandlingFee(double minHandlingFee) { this.minHandlingFee = minHandlingFee; }

    public double getTotalRepayment() {
        return totalRepayment;
    }

    public void setTotalRepayment(double totalRepayment) {
        this.totalRepayment = totalRepayment;
    }

    public double getApr() {
        return apr;
    }

    public void setApr(double apr) {
        this.apr = apr;
    }

    public double getCreditLineAnnualInterestRate() { return creditLineAnnualInterestRate; }

    public void setCreditLineAnnualInterestRate(double creditLineAnnualInterestRate) { this.creditLineAnnualInterestRate = creditLineAnnualInterestRate; }

    public double getCreditLineMonthlyRepayment() { return creditLineMonthlyRepayment; }

    public void setCreditLineMonthlyRepayment(double creditLineMonthlyRepayment) { this.creditLineMonthlyRepayment = creditLineMonthlyRepayment; }

    public double getCreditLineDailyInterest() { return creditLineDailyInterest; }

    public void setCreditLineDailyInterest(double creditLineDailyInterest) { this.creditLineDailyInterest = creditLineDailyInterest; }

    public boolean getHasTaxLoan() {
        return hasTaxLoan;
    }

    public void setHasTaxLoan(boolean hasTaxLoan) {
        this.hasTaxLoan = hasTaxLoan;
    }

    public double getMonthlyPaymentForTaxLoan() {
        return monthlyPaymentForTaxLoan;
    }

    public void setMonthlyPaymentForTaxLoan(double monthlyPaymentForTaxLoan) {
        this.monthlyPaymentForTaxLoan = monthlyPaymentForTaxLoan;
    }

    public boolean isTaxLoanApplicable() {
        if (hasPersonalInstallment) {
           return false;
        }
        return hasTaxLoan;
    }

    public boolean needsSeparateTaxLoanComputation() {
        return hasTaxLoan
                && hasPersonalInstallment;
    }

    public double getMonthlyFee() {
        return monthlyFee;
    }

    public void setMonthlyFee(double monthlyFee) {
        this.monthlyFee = monthlyFee;
    }

    public double getFixedHandlingFee() {
        return fixedHandlingFee;
    }

    public void setFixedHandlingFee(double fixedHandlingFee) {
        this.fixedHandlingFee = fixedHandlingFee;
    }
}
