package com.compareglobal.service.loans.personal.view.xx;


import com.compareglobal.service.loans.personal.domain.Mortgage;

public class MortgageXX extends Mortgage {
    private double lowestApr;
    private double minMonthlyPayment;
    private double minMonthlyPaymentPercent;
    private boolean hasCreditLine;

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
}
