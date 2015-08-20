package com.compareglobal.service.loans.personal.logic.xx;

import com.compareglobal.service.loans.personal.domain.Mortgage;
import com.compareglobal.service.loans.personal.logic.PaymentHelper;
import org.springframework.stereotype.Component;

@Component("paymentHelperXX")
public class PaymentHelperXX  implements PaymentHelper {

    @Override
    public double getLoanPayment(Object loan, double interestRate) {
        return 0;
    }

    @Override
    public double getLoanPayment(Object loan) {
        Mortgage loanPayment = (Mortgage) loan;
        double loanAmount = loanPayment.getPrincipalLoanAmount();
        double monthlyFlatRateValue = loanPayment.getMonthlyInterestRate();
        int loanTenure = loanPayment.getTenureInMonths();
        int handlingFeeType = loanPayment.getHandlingFeeType();
        double minHandlingFee = loanPayment.getMinHandlingFee();
        double annualHandlingFee = loanPayment.getAnnualHandlingFee();

        return getMonthlyFlatRate(loanAmount,
                monthlyFlatRateValue,
                loanTenure,
                handlingFeeType,
                minHandlingFee,
                annualHandlingFee);
    }

    @Override
    public double getAPR(int paymentInMonths, double paymentPerMonth, double presentValue, double interestRate) {
        return 0;
    }

    public double getMonthlyFlatRate(double loanAmount,
                                     double monthlyFlatRateValue,
                                     int loanTenure,
                                     int handlingFeeType,
                                     double minHandlingFee,
                                     double annualHandlingFee) {
        double monthlyPayment = 0;
        double computedLoanTenure = loanAmount / loanTenure;
        double computedFlatRateValue = 1 + ((monthlyFlatRateValue * loanTenure) / 100);
        double computedFlatRateValueTenure = computedFlatRateValue / loanTenure;
        double basicMonthlyPayment = loanAmount * computedFlatRateValueTenure;
        double computedMinHandlingFee = minHandlingFee / loanTenure;
        double computedAnnualHandlingFee = (annualHandlingFee / 100) * computedLoanTenure;
        double totalAnnualHandlingFee = basicMonthlyPayment + computedAnnualHandlingFee;
        double totalMinHandlingFee = basicMonthlyPayment + computedMinHandlingFee;

        if (handlingFeeType == 0) {
            monthlyPayment = basicMonthlyPayment;
        } else if (handlingFeeType == 1) {
            if (annualHandlingFee > 0) {
                monthlyPayment = basicMonthlyPayment + ((annualHandlingFee / 1200) * loanAmount);
            } else {
                monthlyPayment = basicMonthlyPayment;
            }
        } else if (handlingFeeType == 3) {
            if (annualHandlingFee > 0
                    && minHandlingFee == 0) {
                monthlyPayment = totalAnnualHandlingFee;
            }
            if (annualHandlingFee == 0
                    && minHandlingFee > 0) {
                monthlyPayment = totalMinHandlingFee;
            }

        } else if (handlingFeeType == 4) {
            double computedHandlingFee = annualHandlingFee / 100 * loanAmount;
            if (computedHandlingFee >= minHandlingFee) {
                monthlyPayment = totalAnnualHandlingFee;
            }
            if (computedHandlingFee < minHandlingFee) {
                monthlyPayment = totalMinHandlingFee;
            }
        } else {
            monthlyPayment = basicMonthlyPayment / loanTenure;
        }
        return monthlyPayment;
    }
}
