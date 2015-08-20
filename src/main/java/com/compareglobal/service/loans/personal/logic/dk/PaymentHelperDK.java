/*
 * Copyright (c) 2015.
 * Compare Asia Group
 */
package com.compareglobal.service.loans.personal.logic.dk;

import com.compareglobal.service.loans.personal.domain.dk.MortgageDK;
import com.compareglobal.service.loans.personal.logic.PaymentHelper;
import org.apache.poi.ss.formula.functions.FinanceLib;
import org.springframework.stereotype.Component;

/**
 * Created by dennis on 2/13/15.
 */
@Component("paymentHelperDK")
public class PaymentHelperDK implements PaymentHelper {

    private static final double MONTH_OVER_YEAR = 1.0 / 12.0;

    @Override
    public double getLoanPayment(Object loan) {
        return 0;
    }

    /**
    // -PMT( Math.pow( (1 + lowestMonthlyFlatRate), (1/12)) - 1 , tenureInMonths, loanAmnt
        + (floatingHandlingFee/100 * loanAmnt) + (monthlyFee * tenureInMonths) + fixedHandlingFee);
    */
    @Override
    public double getLoanPayment(Object loan, double interestRate) {
        MortgageDK mortgageLocal = (MortgageDK) loan;
        double rate = interestRate > 0 ? Math.pow(1 + interestRate, MONTH_OVER_YEAR) - 1 : 0;

        double periods = mortgageLocal.getTenureInMonths();
        double futureValue = getFutureValue(mortgageLocal);
        double defaultPayment =  Math.abs(FinanceLib.pmt(rate, periods, futureValue, 0, false));
        if (mortgageLocal.getMinimumFloatingHandlingFee() > defaultPayment
                && !mortgageLocal.isOnlineLender()) {
            futureValue = getFutureValueExceedHandlingFee(mortgageLocal);
            defaultPayment = Math.abs(FinanceLib.pmt(rate, periods, futureValue, 0, false));
        }
        return defaultPayment;
    }

    public double getAPR(int paymentInMonths,
                         double paymentPerMonth,
                         double presentValue,
                         double interestRate) {
        double rate = jsRate(paymentInMonths, paymentPerMonth, -presentValue, 0, 0, interestRate);
        return  (Math.pow(1 + rate, 12) - 1) * 100;
    }

    private double getFutureValue(MortgageDK loan) {
        double loanAmount = loan.getPrincipalLoanAmount();

        double floatingHandlingFee = loan.getFloatingHandlingFee() > 0 ? (loan.getFloatingHandlingFee() / 100) * loanAmount : 0;

        double fixedHandlingFee = loan.getFixedHandlingFee();

        return loanAmount + floatingHandlingFee + fixedHandlingFee;
    }

    private double getFutureValueExceedHandlingFee(MortgageDK loan) {
        double loanAmount = loan.getPrincipalLoanAmount();

        double floatingHandlingFee = loan.getMinimumFloatingHandlingFee() > 0 ? loan.getMinimumFloatingHandlingFee() : 0;

        double fixedHandlingFee = loan.getFixedHandlingFee();

        return loanAmount + floatingHandlingFee + fixedHandlingFee;
    }

    private double jsRate(int paymentsPerYear,
                        double paymentAmount,
                        double presentValue,
                        double futureValue,
                        int dueEndOrBeginning,
                        double interest) {

        if (interest == 0){
            interest = 0.01;
        }

        if (futureValue == 0){
            futureValue = 0;
        }

        if (dueEndOrBeginning == 0) {
            dueEndOrBeginning = 0;
        }

        int FINANCIAL_MAX_ITERATIONS = 128;
        double FINANCIAL_PRECISION = 0.0000001;
        double y, y0, y1, x0, x1 = 0, f = 0, i = 0;
        double rate = interest;
        if (Math.abs(rate) < FINANCIAL_PRECISION) {
            y= (presentValue * ( 1+ paymentsPerYear * rate) + paymentAmount * (1 + rate*dueEndOrBeginning) *paymentsPerYear + futureValue);
        } else {
            f = Math.exp((paymentsPerYear * Math.log( 1 + rate)));
            y = presentValue * f + paymentAmount *( 1 / rate + dueEndOrBeginning) * ( f - 1 ) + futureValue;
        }
        y0 = presentValue + paymentAmount * paymentsPerYear + futureValue;
        y1 = presentValue * f + paymentAmount * ( 1 / rate + dueEndOrBeginning ) * ( f - 1) + futureValue;
        i = x0 = 0.0;
        x1 = rate;
        while((Math.abs( y0 - y1 ) > FINANCIAL_PRECISION)
                && (i < FINANCIAL_MAX_ITERATIONS)) {
            rate=( ( y1 * x0 ) - ( y0 * x1 ) ) / ( y1 - y0 );
            x0 = x1;
            x1 = rate;
            if (Math.abs(rate) < FINANCIAL_PRECISION) {
                y = (presentValue * (1 + paymentsPerYear * rate) + paymentAmount * (1 + rate * dueEndOrBeginning) * paymentsPerYear + futureValue);
            } else {
                f = Math.exp((paymentsPerYear * Math.log( 1 + rate)));
                y = presentValue * f + paymentAmount * ( 1 / rate + dueEndOrBeginning) * (f-1) + futureValue;
            }
            y0 = y1;
            y1 = y;
            ++i;
        }
        return rate;
    }
}
