/*
 * Copyright (c) 2015.
 * Compare Asia Group
 */
package com.compareglobal.service.common.loans.personal.domain;

import com.compareglobal.service.loans.personal.domain.eligibility.Criteria;

import java.util.List;

/**
 * Created by dennis on 2/23/15.
 */
public class LoanRange {

    public enum LoanRangeKeys {
        LoanAmount("loanAmount"),
        LoanTenure("loanTenure"),
        Age("age");

        private final String rangeKey;

        LoanRangeKeys(String rangeKey) {
            this.rangeKey = rangeKey;
        }

        public  String getRangeKey() {
            return rangeKey;
        }

        public static LoanRangeKeys findByKey(String rateKey) {
            for (LoanRangeKeys key : values()) {
                if (key.getRangeKey().equalsIgnoreCase(rateKey)) {
                    return  key;
                }
            }
            return  null;
        }
    }

    private double minLoanAmount;
    private double maxLoanAmount;
    private int minTenure;
    private int maxTenure;

    public LoanRange(List<Criteria> criteriaList) {
        for (Criteria criteria : criteriaList) {
            LoanRangeKeys rangeKey = LoanRangeKeys.findByKey(criteria.getTypeValue());
            if (rangeKey != null) {
                switch (rangeKey) {
                    case LoanAmount:
                        this.minLoanAmount = criteria.getMinimum().doubleValue();
                        this.maxLoanAmount = criteria.getMaximum().doubleValue();
                        break;
                    case LoanTenure:
                        this.minTenure = criteria.getMinimum();
                        this.maxTenure = criteria.getMaximum();
                    default:
                }
            }
        }
    }

    public boolean allowed(double loanAmount, int loanTenure) {
        return allowedAmount(loanAmount)
                && allowedTenure(loanTenure);
    }

    private boolean allowedAmount(double loanAmount) {
       return loanAmount >= minLoanAmount
                && loanAmount <= maxLoanAmount;
    }

    private boolean allowedTenure(int loanTenure) {
        return loanTenure >= minTenure
                && loanTenure <= maxTenure;
    }

}
