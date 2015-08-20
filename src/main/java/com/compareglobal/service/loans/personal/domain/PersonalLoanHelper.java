package com.compareglobal.service.loans.personal.domain;

/**
 * Created by dennis on 2/16/15.
 */
public interface PersonalLoanHelper {
    PersonalLoanPublic build(PersonalLoan personalLoan, double loanAmount, int tenure);
    String getLocale();
}
