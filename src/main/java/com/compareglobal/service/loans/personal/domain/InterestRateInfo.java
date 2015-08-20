package com.compareglobal.service.loans.personal.domain;

import java.math.BigDecimal;

/**
 * Created by dennis on 5/12/15.
 */
public class InterestRateInfo {
    private int period;
    private double amount;
    private BigDecimal rate;

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }
}
