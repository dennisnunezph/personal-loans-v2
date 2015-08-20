package com.compareglobal.service.loans.personal.domain.xx;


public enum InterestRateTypeXX {

    Annual("annual"),
    Fixed("fixed"),
    FixedRange("fixedRange");

    private final String rateKey;

    InterestRateTypeXX(String rateKey) {
        this.rateKey = rateKey;
    }

    public  String getRateKey() {
        return rateKey;
    }

    public static InterestRateTypeXX findByKey(String rateKey) {
        for (InterestRateTypeXX key : values()) {
            if (key.getRateKey().equalsIgnoreCase(rateKey)) {
                return  key;
            }
        }
        return  null;
    }
}
