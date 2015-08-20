/*
* Copyright (c) 2015.
* Compare Asia Group
*/
package com.compareglobal.service.loans.personal.domain;

/**
 * Created by dennis on 4/22/15.
 */
public interface MonthlyPaymentHelper {
    Mortgage computeMortgage(double loanAmount, int tenure, PersonalLoan personalLoan);
}