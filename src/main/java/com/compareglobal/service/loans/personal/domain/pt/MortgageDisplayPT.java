package com.compareglobal.service.loans.personal.domain.pt;

import com.compareglobal.service.common.loans.personal.domain.MortgageDisplay;
import com.compareglobal.service.common.utils.FormatterHelper;

/**
 * Created by dennis on 4/27/15.
 */
public class MortgageDisplayPT extends MortgageDisplay {
    private double commissionAmount;
    private double taxOnCredit;
    private double lifeInsurance;
    private double borrowedAmount;
    private double processingPercent;
    private double monthlyProcessingFee;
    private double totalPayment;
    private double totalInterest;
    private double TAEG;
    private double lowestTaxRateTAN;
    private double totalMonthlyPayment;

    public double getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(double commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public String getTaxOnCredit() {
        return FormatterHelper.decimalFormat(taxOnCredit);
    }

    public void setTaxOnCredit(double taxOnCredit) {
        this.taxOnCredit = taxOnCredit;
    }

    public void setLifeInsurance(double lifeInsurance) {
        this.lifeInsurance = lifeInsurance;
    }

    public String getLifeInsurance() {
        return FormatterHelper.decimalFormat(lifeInsurance);
    }

    public void setBorrowedAmount(double borrowedAmount) {
        this.borrowedAmount = borrowedAmount;
    }

    public String getBorrowedAmount() {
        return FormatterHelper.decimalFormat(borrowedAmount);
    }

    public void setProcessingPercent(double processingPercent) {
        this.processingPercent = processingPercent;
    }

    public String getProcessingPercent() {
        return FormatterHelper.decimalFormat(processingPercent);
    }

    public void setMonthlyProcessingFee(double monthlyProcessingFee) {
        this.monthlyProcessingFee = monthlyProcessingFee;
    }

    public double getMonthlyProcessingFee() {
        return monthlyProcessingFee;
    }

    public void setTotalPayment(double totalPayment) {
        this.totalPayment = totalPayment;
    }

    public String getTotalPayment() {
        return FormatterHelper.decimalFormat(totalPayment);
    }

    public void setTotalInterest(double totalInterest) {
        this.totalInterest = totalInterest;
    }

    public String getTotalInterest() {
        return FormatterHelper.decimalFormat(totalInterest);
    }

    public void setTAEG(double TAEG) {
        this.TAEG = TAEG;
    }

    public String getTAEG() {
        return FormatterHelper.decimalFormatForPercent(TAEG * 100);
    }

    public String getLowestTaxRateTAN() {
        return FormatterHelper.decimalFormatForPercent(lowestTaxRateTAN);
    }

    public void setLowestTaxRateTAN(double lowestTaxRateTAN) {
        this.lowestTaxRateTAN = lowestTaxRateTAN;
    }

    public void setTotalMonthlyPayment(double totalMonthlyPayment) {
        this.totalMonthlyPayment = totalMonthlyPayment;
    }

    public double getTotalMonthlyPayment() {
        return  totalMonthlyPayment;
    }
}
