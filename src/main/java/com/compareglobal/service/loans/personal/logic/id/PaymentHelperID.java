package com.compareglobal.service.loans.personal.logic.id;

import com.compareglobal.service.loans.personal.domain.Mortgage;
import com.compareglobal.service.loans.personal.logic.PaymentHelper;

public class PaymentHelperID implements PaymentHelper {

    private double maxLoanAmount = 0;
    private double minLoanAmount = 0;
    private double lowestApr = 0;
    private double monthlyInterestRate = 0;
    private double annualHandlingFee = 0;
    private double firstYearPercentageHandlingFee = 0;
    private double firstYearFixedHanlingFee = 0;
    private double minHandlingFee = 0;
    private int handlingFeeType = 0;

    public void setMaxLoanAmount(double maxLoanAmount) {
        this.maxLoanAmount = maxLoanAmount;
    }

    public void setMinLoanAmount(double minLoanAmount) {
        this.minLoanAmount = minLoanAmount;
    }

    public void setLowestApr(double lowestApr) {
        this.lowestApr = lowestApr;
    }

    public void setMonthlyInterestRate(double monthlyInterestRate) {
        this.monthlyInterestRate = monthlyInterestRate;
    }

    public void setAnnualHandlingFee(double annualHandlingFee) {
        this.annualHandlingFee = annualHandlingFee;
    }

    public void setFirstYearFixedHanlingFee(double firstYearFixedHanlingFee) {
        this.firstYearFixedHanlingFee = firstYearFixedHanlingFee;
    }

    public void setFirstYearPercentageHandlingFee(double firstYearPercentageHandlingFee) {
        this.firstYearPercentageHandlingFee = firstYearPercentageHandlingFee;
    }

    public void setMinHandlingFee(double minHandlingFee) {
        this.minHandlingFee = minHandlingFee;
    }

    public void setHandlingFeeType(int handlingFeeType) {
        this.handlingFeeType = handlingFeeType;
    }

    @Override
    public double getLoanPayment(Object loan, double interestRate) {
        return 0;
    }

    @Override
    public double getLoanPayment(Object loan) { // this is actually the montly payment
        Mortgage loanPayment = (Mortgage) loan;
        double loanAmount = loanPayment.getPrincipalLoanAmount();
        return (loanAmount / loanPayment.getTenureInMonths()) + (loanAmount * getMonthlyFlatRate() / 12.0);

    }

    @Override
    public double getAPR(int paymentInMonths, double paymentPerMonth, double presentValue, double interestRate) {
        if (lowestApr > 0)
            return lowestApr / 100.0;
        else return 0;
    }

    public double getMonthlyFlatRate() {
        if (monthlyInterestRate > 0)
            return monthlyInterestRate * 12.0 / 100.0;
        else return 0;
    }

    public double getTotalPayment(Mortgage loan) {
        double tp = loan.getPrincipalLoanAmount() + ((loan.getPrincipalLoanAmount() * (getMonthlyFlatRate() / 12)) * loan.getTenureInMonths());
        tp += Math.max((firstYearPercentageHandlingFee / 100) * loan.getPrincipalLoanAmount(), minHandlingFee);
        tp += firstYearFixedHanlingFee;
        tp += ((loan.getTenureInMonths() > 12) ? ((annualHandlingFee / 12.0) * (loan.getTenureInMonths() - 12)) : 0);
        return tp;
    }

}