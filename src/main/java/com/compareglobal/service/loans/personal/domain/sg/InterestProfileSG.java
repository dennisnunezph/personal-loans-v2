package com.compareglobal.service.loans.personal.domain.sg;

/**
 * Created by dennis on 5/12/15.
 */
public enum InterestProfileSG {
    PersonalInstalment("hasPersonalInstalmentLoan"),
    CreditLine("hasPersonalCreditLine"),
    BalanceTransfer("hasDebtConsolidation");

    private final String interestProfile;

    InterestProfileSG(String interestProfile) {
        this.interestProfile = interestProfile;
    }

    public  String getInterestProfile() {
        return interestProfile;
    }

    public static InterestProfileSG findByKey(String rateKey) {
        for (InterestProfileSG key : values()) {
            if (key.getInterestProfile().equalsIgnoreCase(rateKey)) {
                return  key;
            }
        }
        return  null;
    }
}
