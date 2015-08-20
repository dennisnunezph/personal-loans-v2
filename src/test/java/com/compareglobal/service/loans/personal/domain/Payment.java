package com.compareglobal.service.loans.personal.domain;

/**
 * Created by Luis Miguel Osorio.
 */
public class Payment {

    private final Integer loanTenure, loanAmount;

    private final Double interestRate, monthlyPayment, totalRepayment;

    public Payment(final Integer loanTenure, final Integer loanAmount, final Double interestRate, final Double monthlyPayment, final Double totalRepayment) {
        this.loanTenure = loanTenure;
        this.loanAmount = loanAmount;
        this.interestRate = interestRate;
        this.monthlyPayment = monthlyPayment;
        this.totalRepayment = totalRepayment;
    }

    public Integer getLoanTenure() {
        return loanTenure;
    }

    public Integer getLoanAmount() {
        return loanAmount;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public Double getMonthlyPayment() {
        return monthlyPayment;
    }

    public Double getTotalRepayment() {
        return totalRepayment;
    }
}
