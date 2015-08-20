/*
 * Copyright (c) 2015.
 * Compare Asia Group
 */
package com.compareglobal.service.loans.personal.domain.dk;

import com.compareglobal.service.loans.personal.domain.Mortgage;

/**
 * Created by dennis on 2/16/15.
 */
public class MortgageDK extends Mortgage {
    private double floatingHandlingFee;
    private double minimumFloatingHandlingFee;
    private double monthlyInterestRateMax;
    private double monthlyPaymentMax;

    private boolean onlineLender;
    private String legalText;

    public double getFloatingHandlingFee() {
        return floatingHandlingFee;
    }

    public void setFloatingHandlingFee(double floatingHandlingFee) {
        this.floatingHandlingFee = floatingHandlingFee;
    }

    public double getMonthlyInterestRateMax() {
        return monthlyInterestRateMax;
    }

    public void setMonthlyInterestRateMax(double monthlyInterestRateMax) {
        this.monthlyInterestRateMax = monthlyInterestRateMax;
    }

    public double getMonthlyPaymentMax() {
        return monthlyPaymentMax;
    }

    public void setMonthlyPaymentMax(double monthlyPaymentMax) {
        this.monthlyPaymentMax = monthlyPaymentMax;
    }

    public double getMinimumFloatingHandlingFee() {
        return minimumFloatingHandlingFee;
    }

    public void setMinimumFloatingHandlingFee(double minimumFloatingHandlingFee) {
        this.minimumFloatingHandlingFee = minimumFloatingHandlingFee;
    }

    public boolean isOnlineLender() {
        return onlineLender;
    }

    public void setOnlineLender(boolean onlineLender) {
        this.onlineLender = onlineLender;
    }

    public void setLegalText(String legalText) {
        this.legalText = legalText;
    }

    public String getLegalText() {
        return legalText;
    }
}
