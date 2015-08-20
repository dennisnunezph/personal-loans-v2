package com.compareglobal.service.loans.personal.view.id;


import com.compareglobal.service.loans.personal.domain.Mortgage;

public class MortgageID extends Mortgage {
    private double lowestApr;
    private double minMonthlyPayment;
    private double minMonthlyPaymentPercent;
    private boolean hasCreditLine;
    private double monthlyInterestRate;

    public double getLowestApr() {
        return lowestApr;
    }

    public void setLowestApr(double lowestApr) {
        this.lowestApr = lowestApr;
    }

    public double getMinMonthlyPayment() {
        return minMonthlyPayment;
    }

    public void setMinMonthlyPayment(double minMonthlyPayment) {
        this.minMonthlyPayment = minMonthlyPayment;
    }

    public double getMinMonthlyPaymentPercent() {
        return minMonthlyPaymentPercent;
    }

    public void setMinMonthlyPaymentPercent(double minMonthlyPaymentPercent) {
        this.minMonthlyPaymentPercent = minMonthlyPaymentPercent;
    }

    public boolean isHasCreditLine() {
        return hasCreditLine;
    }

    public void setHasCreditLine(boolean hasCreditLine) {
        this.hasCreditLine = hasCreditLine;
    }


    @Override
    public double getMonthlyInterestRate() {
        return this.monthlyInterestRate;
    }

    @Override
    public void setMonthlyInterestRate(double defaultMonthlyInterestRate) {
        this.monthlyInterestRate = defaultMonthlyInterestRate;
    }
}
