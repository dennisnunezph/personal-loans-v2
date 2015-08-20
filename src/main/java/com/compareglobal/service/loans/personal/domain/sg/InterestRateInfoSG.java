package com.compareglobal.service.loans.personal.domain.sg;

import com.compareglobal.service.loans.personal.domain.InterestRateInfo;

/**
 * Created by dennis on 5/12/15.
 */
public class InterestRateInfoSG extends InterestRateInfo {
    private InterestProfileSG interestProfile;
    private String recordKey;
    private String description;

    public InterestProfileSG getInterestProfile() {
        return interestProfile;
    }

    public void setInterestProfile(InterestProfileSG interestProfile) {
        this.interestProfile = interestProfile;
    }

    public double getYears() {
        if (this.getPeriod() > 0) {
            return getPeriod() / 12;
        }
        return 0;
    }

    public String getRecordKey() {
        return recordKey;
    }

    public void setRecordKey(String recordKey) {
        this.recordKey = recordKey;
    }
}
