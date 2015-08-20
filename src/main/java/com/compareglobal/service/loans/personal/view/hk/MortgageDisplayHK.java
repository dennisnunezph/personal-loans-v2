/*
 * Copyright (c) 2015.
 * Compare Asia Group
 */
package com.compareglobal.service.loans.personal.view.hk;

import com.compareglobal.service.common.loans.personal.domain.MortgageDisplay;
import com.compareglobal.service.common.utils.FormatterHelper;
import com.compareglobal.service.loans.personal.domain.Mortgage;

/**
 * Created by dennis on 2/18/15.
 */
public class MortgageDisplayHK extends MortgageDisplay {
    private double monthlyPaymentForTaxLoan;
    private double totalRepayment;
    private double creditLineAnnualInterestRate;
    private double creditLineMonthlyRepayment;
    private double creditLineDailyInterest;
    private String aprBasis;
    private Integer interestProfile;

    public MortgageDisplayHK(MortgageHK mortgage) {
        if (mortgage != null) {
            this.monthlyPaymentForTaxLoan = mortgage.getMonthlyPaymentForTaxLoan();
            this.totalRepayment = mortgage.getTotalRepayment();
            this.creditLineAnnualInterestRate = mortgage.getCreditLineAnnualInterestRate();
            this.creditLineMonthlyRepayment = mortgage.getCreditLineMonthlyRepayment();
            this.creditLineDailyInterest = mortgage.getCreditLineDailyInterest();
            this.setInterestRate(mortgage.getMonthlyInterestRateRaw());
            this.setApr(mortgage.getApr());
            this.setMonthlyPayment(mortgage.getMonthlyPayment());
            this.aprBasis = mortgage.getAprBasis();
            this.interestProfile = mortgage.getInterestProfile();
        }
    }

    public String getMonthlyPaymentForTaxLoan() {
        return FormatterHelper.decimalFormat(monthlyPaymentForTaxLoan);
    }

    public String getTotalRepayment() {
        return FormatterHelper.decimalFormat(totalRepayment);
    }

    public String getCreditLineAnnualInterestRate() {
        return FormatterHelper.decimalFormatForPercent(creditLineAnnualInterestRate);
    }

    public String getCreditLineMonthlyRepayment() {
        return FormatterHelper.decimalFormatForPercent(creditLineMonthlyRepayment);
    }

    public String getCreditLineDailyInterest() {
        return FormatterHelper.decimalFormatForPercent(creditLineDailyInterest);
    }

    public String getAprBasis() {
        return aprBasis;
    }

    public void setAprBasis(String aprBasis) {
        this.aprBasis = aprBasis;
    }

    public Integer getInterestProfile() {
        return interestProfile;
    }

    public void setInterestProfile(Integer interestProfile) {
        this.interestProfile = interestProfile;
    }

}
