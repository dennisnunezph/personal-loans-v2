/*
 * Copyright (c) 2015.
 * Compare Asia Group
 */
package com.compareglobal.service.common.loans.personal.domain;

import com.compareglobal.service.common.utils.FormatterHelper;

/**
 * Created by dennis on 2/18/15.
 */
public class MortgageDisplay {
    protected double interestRate;
    protected double apr;
    protected double monthlyPayment;

    public String getInterestRate() {
        return FormatterHelper.decimalFormatForPercent(interestRate);
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public String getApr() {
        return FormatterHelper.decimalFormat(apr);
    }

    public void setApr(double apr) {
        this.apr = apr;
    }

    public String getMonthlyPayment() {
        return FormatterHelper.decimalFormat(monthlyPayment);
    }

    public void setMonthlyPayment(double monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }
}
