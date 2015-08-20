/*
 * Copyright (c) 2015.
 * Compare Asia Group
 */
package com.compareglobal.service.loans.personal.logic;

import com.compareglobal.service.loans.personal.domain.interestrates.Interest;

import java.util.List;

/**
 * Created by dennis on 2/13/15.
 */
public interface PaymentHelper {
    double getLoanPayment(Object loan);
    double getLoanPayment(Object loan, double interestRate);
    double getAPR(int paymentInMonths, double paymentPerMonth, double presentValue, double interestRate);
}
