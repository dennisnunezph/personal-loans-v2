package com.compareglobal.service.loans.personal.logic.fi;

import com.compareglobal.service.loans.personal.domain.DBKeys;
import com.compareglobal.service.loans.personal.domain.GeneralInfo;
import com.compareglobal.service.loans.personal.domain.PersonalLoan;
import com.compareglobal.service.loans.personal.domain.fees.Fee;
import com.compareglobal.service.loans.personal.domain.interestrates.Interest;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.ss.formula.functions.FinanceLib;

import java.math.BigDecimal;

/**
 * Created by Luis Miguel Osorio.
 *
 * Mapping all the needed values from data base and making the computations
 */
public class PaymentHelperFI {

    public final static double NOT_AVAILABLE = -1;

    private double interestRateMonthly;
    private double openingHandlingFee;
    private double openingHandlingFeePercentage;
    private double monthlyHandlingFee;
    private double annualHandlingFeePercentage;
    private double interestRateMonthlyMax;
    private double annualHandlingFee;
    private double maxOpeningHandlingFee;
    private double minMonthlyPayment;
    private double minMonthlyPaymentPercent;
    private double maxMonthlyPayment;
    private int computationLogic = 1;


    public PaymentHelperFI(final PersonalLoan personalLoan) {
        // Mapping every value from data base
        loadPropertiesFromPersonalLoanFees(personalLoan);
        loadPropertiesFromPersonalLoanInterest(personalLoan);
        loadPropertiesFromPersonalLoanGeneralInfo(personalLoan);
    }

    public double getMaxEAR(final double loanAmount, final int loanTenure) {
        if (computationLogic == 1)
            return 0;

        final double maxMonthlyPayment = getMaxMonthlyRepayment(loanAmount, loanTenure);

        if (maxMonthlyPayment == NOT_AVAILABLE)
            return NOT_AVAILABLE;

        return PLMath.rate(loanTenure, maxMonthlyPayment, loanAmount) * 12;
    }

    public double getMaxTotalRepayment (final double loanAmount, final int loanTenure) {
        if (computationLogic == 1)
            return 0;

        return getMaxMonthlyRepayment(loanAmount, loanTenure) * loanTenure;
    }


    public double getEAR(final double loanAmount, final int loanTenure) {
        final double monthlyPayment = getMonthlyRepayment(loanAmount, loanTenure);

        if (monthlyPayment == NOT_AVAILABLE)
                return NOT_AVAILABLE;

        return PLMath.rate(loanTenure, monthlyPayment, loanAmount) * 12;
    }
 
    public double getTotalPayment(final double loanAmount, final int loanTenure) {
        return getMonthlyRepayment(loanAmount, loanTenure) * loanTenure;
    }


    public double getMaxMonthlyInterestRate() {
        return interestRateMonthlyMax / 100;
    }

    public double getMonthlyInterestRate() {
        return interestRateMonthly / 100;
    }


    public double getMaxMonthlyRepayment(final double loanAmount, final int loanTenure ) {
        if (computationLogic == 1)
            return 0;

        return getMonthlyRepayment(loanAmount, loanTenure, getMaxMonthlyInterestRate()) ;
    }

    public double getMonthlyRepayment(final double loanAmount, final int loanTenure) {
        return getMonthlyRepayment(loanAmount, loanTenure, getMonthlyInterestRate() ) ;
    }


    private double getMinMonthlyPaymentApplicable (final double amount){
        return Math.max(minMonthlyPayment, minMonthlyPaymentPercent * amount / 100);
    }

    private double getMonthlyRepayment(final double loanAmount, final int loanTenure, final double monthlyInterestRate ) {
        double monthlyPayment = FinanceLib.pmt(
                monthlyInterestRate ,
                loanTenure,
                loanAmount + getOpeningHandlingFee(loanAmount)
                ,0 , false)
                + monthlyHandlingFee + getAnnualHandlingFee(loanAmount) / 12;

        monthlyPayment *= -1;

        if(monthlyPayment<getMinMonthlyPaymentApplicable(loanAmount) || (monthlyPayment > maxMonthlyPayment && maxMonthlyPayment>0) )
            return NOT_AVAILABLE;

        return monthlyPayment;
    }

    private double getAnnualHandlingFee(double loanAmount) {
        if (annualHandlingFee > 0)
            return annualHandlingFee;

        return (annualHandlingFeePercentage/100) * loanAmount;
    }

    private double getOpeningHandlingFee(final double loanAmount) {
        if(openingHandlingFeePercentage > 0) {
            return Math.min(maxOpeningHandlingFee, ((openingHandlingFeePercentage / 100) * loanAmount));
        }
        else {
            return  openingHandlingFee;
        }

    }

    private void loadPropertiesFromPersonalLoanInterest(final PersonalLoan personalLoan) {

        for (Interest interest : personalLoan.getInterestList()) {
            final String type = interest.getTypeValue();
            final BigDecimal amount = interest.getAmount();

            if (amount != null) {
                final double doubleValue = amount.doubleValue();
                if (DBKeys.InterestRateMonthly.equals(type)) {
                    interestRateMonthly = doubleValue;
                }

                if (DBKeys.InterestRateMonthlyMax.equals(type)) {
                    interestRateMonthlyMax = doubleValue;
                }
            }
        }
    }

    private void loadPropertiesFromPersonalLoanFees(final PersonalLoan personalLoan) {

        for (Fee fee : personalLoan.getFeesList()) {
            final String type = fee.getTypeValue();
            final BigDecimal amount = (fee.getAmount() == null)? BigDecimal.ZERO : fee.getAmount();

            if (DBKeys.MonthlyHandlingFee.equals(type)) {
                monthlyHandlingFee = amount.doubleValue();
            }

            if (DBKeys.OpeningHandlingFee.equals(type)) {
                openingHandlingFee = amount.doubleValue();
                openingHandlingFeePercentage = NumberUtils.toDouble(fee.getDescription(), 0.0);
            }

            if (DBKeys.AnnualHandlingFee.equals(type)) {
                annualHandlingFee = amount.doubleValue();
            }

            if (DBKeys.AnnualHandlingFeePercentage.equals(type)) {
                annualHandlingFeePercentage = amount.doubleValue();
            }

            if (DBKeys.MaxOpeningHandlingFee.equals(type)) {
                maxOpeningHandlingFee = amount.doubleValue();
            }

            if (DBKeys.MinMonthlyPayment.equals(type)) {
                minMonthlyPayment = amount.doubleValue();
            }

            if (DBKeys.MaxMonthlyPayment.equals(type)) {
                maxMonthlyPayment = amount.doubleValue();
            }

            if (DBKeys.MinMonthlyPaymentPercent.equals(type)) {
                minMonthlyPaymentPercent = amount.doubleValue();
            }
        }
    }

    private void loadPropertiesFromPersonalLoanGeneralInfo(final PersonalLoan personalLoan) {

        for (GeneralInfo generalInfo : personalLoan.getGeneralInfo()) {
            final String type = generalInfo.getTypeValue();
            final String description = generalInfo.getDescription();

            if (DBKeys.ComputationLogic.equals(type)) {
                computationLogic = Integer.parseInt(description);
            }
        }
    }
}
