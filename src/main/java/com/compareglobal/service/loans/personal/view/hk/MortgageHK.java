/*
 * Copyright (c) 2015.
 * Compare Asia Group
 */
package com.compareglobal.service.loans.personal.view.hk;

import com.compareglobal.service.loans.personal.domain.Mortgage;

/**
 * Created by dennis on 2/16/15.
 */
public class MortgageHK extends Mortgage {
    private double lowestApr;
    private double minMonthlyPayment;
    private double minMonthlyPaymentPercent;
    private boolean hasCreditLine;
    private String aprBasis;
    private double prime;

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

    public void setAprBasis(String aprBasis) {
        this.aprBasis = aprBasis;
    }

    public String getAprBasis() {
        return aprBasis;
    }

    public void setPrime(double prime) {
        this.prime = prime;
    }

    public double getPrime() {
        return prime;
    }
}
