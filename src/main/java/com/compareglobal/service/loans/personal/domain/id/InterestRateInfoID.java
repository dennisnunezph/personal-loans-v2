package com.compareglobal.service.loans.personal.domain.id;

import java.math.BigDecimal;

/**
 * Created by dennis on 3/3/15.
 */
public class InterestRateInfoID {
    private InterestRateTypeID interestRateType;
    private int period;
    private Long amount;
    private BigDecimal rate;

    public InterestRateTypeID getInterestRateType() {
        return interestRateType;
    }

    public void setInterestRateType(InterestRateTypeID interestRateType) {
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
