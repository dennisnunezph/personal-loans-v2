package com.compareglobal.service.loans.personal.domain.xx;


import com.compareglobal.service.loans.personal.domain.hk.InterestRateType;

import java.math.BigDecimal;

public class InterestRateInfoXX {

    private InterestRateTypeXX interestRateType;
    private int period;
    private Long amount;
    private BigDecimal rate;

    public InterestRateTypeXX getInterestRateType() {
        return interestRateType;
    }

    public void setInterestRateType(InterestRateTypeXX interestRateType) {
        this.interestRateType = interestRateType;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public BigDecimal getRate() {
        return rate;
    }

}
