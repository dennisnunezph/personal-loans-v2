package com.compareglobal.service.loans.personal.view.fi;

import com.compareglobal.service.common.loans.personal.domain.MortgageDisplay;
import com.compareglobal.service.common.utils.FormatterHelper;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Luis Miguel Osorio.
 */
public class MortgageDisplayFI extends MortgageDisplay {


    private Double loanAmount;
    private Integer loanTenure;
    private Double monthlyInterestRate;
    private Double totalRepayment;
    private Double monthlyRepayment;
    private Double maxEAR;
    private Double maxTotalRepayment;
    private Double EAR;
    private Double maxMonthlyRepayment;

    Double doubleMaxMonthlyRepayment() {
        return maxMonthlyRepayment;
    }

    Double doubleTotalRepayment() {
        return totalRepayment;
    }

    Double doubleMaxEAR() {
        return maxEAR;
    }

    Double doubleMaxTotalRepayment() {
        return maxTotalRepayment;
    }

    public String getLoanAmount() {
        return FormatterHelper.decimalFormat(loanAmount);
    }

    public void setLoanAmount(final Double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public String getLoanTenure() {
        return String.valueOf(loanTenure);
    }

    public void setLoanTenure(final Integer loanTenure) {
        this.loanTenure = loanTenure;
    }

    public void setMonthlyPayment(final Double monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }

    public String getMonthlyInterestRate() {
        return FormatterHelper.decimalFormat(monthlyInterestRate);
    }

    public void setMonthlyInterestRate(final Double monthlyInterestRate) {
        this.monthlyInterestRate = monthlyInterestRate;
    }

    public String getTotalRepayment() {
        return FormatterHelper.decimalFormat(totalRepayment);
    }

    public void setTotalRepayment(final Double totalRepayment) {
        this.totalRepayment = totalRepayment;
    }

    public void setMaxEAR(final double maxEAR) {
        this.maxEAR = maxEAR;
    }

    public void setMaxTotalRepayment(final double maxTotalRepayment) {
        this.maxTotalRepayment = maxTotalRepayment;
    }

    public void setEAR(final double EAR) {
        this.EAR = EAR;
    }

    public String getMaxEAR() {
        return FormatterHelper.decimalFormat(maxEAR * 100);
    }

    public String getMaxTotalRepayment() {
        return FormatterHelper.decimalFormat(maxTotalRepayment);
    }

    @JsonProperty("EAR")
    public String getEAR() {
        return FormatterHelper.decimalFormat(EAR * 100);
    }

    public String getMonthlyRepayment() {
        return FormatterHelper.decimalFormat(monthlyRepayment);
    }

    public void setMonthlyRepayment(final Double monthlyRepayment) {
        this.monthlyRepayment = monthlyRepayment;
    }

    public String getMaxMonthlyRepayment() {
        return FormatterHelper.decimalFormat(maxMonthlyRepayment);
    }

    public void setMaxMonthlyRepayment(final double maxMonthlyRepayment) {
        this.maxMonthlyRepayment = maxMonthlyRepayment;
    }

}
