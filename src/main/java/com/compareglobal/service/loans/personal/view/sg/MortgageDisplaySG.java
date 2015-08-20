/*
 * Copyright (c) 2015.
 * Compare Asia Group
 */
package com.compareglobal.service.loans.personal.view.sg;

import com.compareglobal.service.common.loans.personal.domain.MortgageDisplay;
import com.compareglobal.service.common.utils.FormatterHelper;
import com.compareglobal.service.loans.personal.domain.Mortgage;
import com.compareglobal.service.loans.personal.domain.sg.InterestProfileSG;

/**
 * Created by dennis on 2/18/15.
 */
public class MortgageDisplaySG extends MortgageDisplay {
    private String aprBasis;
    private long maxTenure;
    private double totalRepayment;
    private double totalInterest;
    private double totalHandlingFee;
    private double annualHandlingFee;
    private double effectiveInterestRate;
    private double monthlyInterestRate;
    private String annualRateText;
    private double minMonthlyPayment;
    private double minMonthlyPaymentPercent;
    private double minHandlingFeePercent;
    private double minHandlingFeeAmount;
    private InterestProfileSG categoryProfile;
    private String effectiveInterestRateCombined;
    private String maxBalanceTrfTenure;
    private double lowestInterestRate;
    private double fir2Years;

    public MortgageDisplaySG(Mortgage mortgage) {
        MortgageSG mortgageSG = (MortgageSG) mortgage;
        if (mortgage != null) {
            this.totalRepayment = mortgage.getTotalRepayment();
            this.interestRate = mortgageSG.getAnnualInterestRate();
            this.monthlyPayment = mortgage.getMonthlyPayment();
            this.setMonthlyPayment(mortgage.getMonthlyPayment());
            this.totalInterest = mortgageSG.getTotalInterest();
            this.annualHandlingFee = mortgageSG.getAnnualHandlingFee();
            this.effectiveInterestRate = mortgageSG.getEffectiveInterestRate();
            this.effectiveInterestRateCombined = mortgageSG.getEffectiveInterestRateCombined();
            this.monthlyInterestRate = mortgageSG.getMonthlyInterestRate();
            this.maxTenure = mortgageSG.getMaxTenure();
            this.aprBasis = mortgageSG.getAprBasis();
            this.annualRateText = mortgageSG.getAnnualRateText();
            this.minMonthlyPayment = mortgageSG.getMinMonthlyPayment();
            this.minMonthlyPaymentPercent = mortgageSG.getMinMonthlyPaymentPercent();
            this.minHandlingFeePercent = mortgageSG.getMinHandlingFeePercent();
            this.minHandlingFeeAmount = mortgageSG.getMinHandlingFeeAmount();
            this.categoryProfile = mortgageSG.getCategoryProfile();
            this.apr = mortgageSG.getLowestApr();
            this.maxBalanceTrfTenure = mortgageSG.getMaxBalanceTrfTenure();
            this.lowestInterestRate = mortgageSG.getLowestInterestRate();
            this.fir2Years = mortgageSG.getFir2Years();
            this.totalHandlingFee = mortgageSG.getTotalHandlingFee();
        }
    }

    public String getTotalRepayment() {
        return FormatterHelper.decimalFormat(totalRepayment);
    }

    public String getTotalInterest() {
        return FormatterHelper.decimalFormat(totalInterest);
    }

    public double getTotalHandlingFee() {
        return totalHandlingFee;
    }


    public String getAnnualHandlingFee() {
        return FormatterHelper.decimalFormat(annualHandlingFee);
    }
    public String  getEffectiveInterestRate() {
        return FormatterHelper.decimalFormatForPercent(effectiveInterestRate);
    }

    public String getMonthlyInterestRate() {
        return FormatterHelper.decimalFormatForPercent(monthlyInterestRate);
    }

    public long getMaxTenure() {
        return maxTenure;
    }

    public String getAprBasis() {
        return aprBasis;
    }

    public String getAnnualRateText() {
        return annualRateText;
    }

    public String getMinMonthlyPayment() {
        return FormatterHelper.decimalFormat(minMonthlyPayment);
    }

    public String getMinMonthlyPaymentPercent() {
        return FormatterHelper.decimalFormatForPercent(minMonthlyPaymentPercent);
    }

    public double getMinHandlingFeePercent() {
        return minHandlingFeePercent;
    }

    public InterestProfileSG getCategoryProfile() {
        return categoryProfile;
    }

    public double getMinHandlingFeeAmount() {
        return  minHandlingFeeAmount;
    }

    public String getEffectiveInterestRateCombined() {
        return effectiveInterestRateCombined;
    }


    public double getLowestInterestRate() {
        return lowestInterestRate;
    }

    public String getMaxBalanceTrfTenure() {
        return maxBalanceTrfTenure;
    }

    public double getFir2Years() {
        return fir2Years;
    }

}
