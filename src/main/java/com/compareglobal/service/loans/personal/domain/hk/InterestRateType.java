package com.compareglobal.service.loans.personal.domain.hk;

/**
 * Created by dennis on 3/3/15.
 */
public enum  InterestRateType {
    Annual("annual"),
    Fixed("fixed"),
    FixedRange("fixedRange"),
    FIRRange("firRange"),
    Basis("basis");

    private final String rateKey;

    InterestRateType(String rateKey) {
        this.rateKey = rateKey;
    }

    public  String getRateKey() {
        return rateKey;
    }

    public static InterestRateType findByKey(String rateKey) {
        for (InterestRateType key : values()) {
            if (key.getRateKey().equalsIgnoreCase(rateKey)) {
                return  key;
            }
        }
        return  null;
    }
}
