package com.compareglobal.service.loans.personal.domain.id;

/**
 * Created by dennis on 3/3/15.
 */
public enum InterestRateTypeID {
    Annual("annual"),
    Fixed("fixed"),
    FixedRange("fixedRange");

    private final String rateKey;

    InterestRateTypeID(String rateKey) {
        this.rateKey = rateKey;
    }

    public  String getRateKey() {
        return rateKey;
    }

    public static InterestRateTypeID findByKey(String rateKey) {
        for (InterestRateTypeID key : values()) {
            if (key.getRateKey().equalsIgnoreCase(rateKey)) {
                return  key;
            }
        }
        return  null;
    }
}
