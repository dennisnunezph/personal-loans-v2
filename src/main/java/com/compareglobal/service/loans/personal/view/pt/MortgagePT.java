package com.compareglobal.service.loans.personal.view.pt;

import com.compareglobal.service.loans.personal.domain.Mortgage;

import java.math.BigDecimal;

/**
 * Created by dennis on 4/24/15.
 */
public class MortgagePT extends Mortgage {
    private InitialCommission initialCommission;
    private double commissionAmount;
    private double taxOnCredit;
    private double lifeInsurance;
    private double borrowedAmount;
    private double tan;
    private double fixedAmtProcessingFee;
    private double monthlyProcessingPercent;
    private double varRateProcessingFee;
    private boolean lifeInsuranceRequired;
    private boolean hasMonthlyProcessingFee;
    private double monthlyProcessingFee;
    private double TAEG;
    private double lowestTaxRateTAN;


    public void setBorrowedAmount(double borrowedAmount) {
        this.borrowedAmount = borrowedAmount;
    }

    public double getBorrowedAmount() {
        if (borrowedAmount == 0) {
            computedBorrowedAmount();
        }
        return borrowedAmount;
    }

    public InitialCommission getInitialCommission() {
        return initialCommission;
    }

    public void setInitialCommission(InitialCommission initialCommission) {
        this.initialCommission = initialCommission;
    }

    public double getCommissionAmount() {
        return commissionAmount;
    }

    public void setCommissionAmount(double commissionAmount) {
        this.commissionAmount = commissionAmount;
    }

    public double getTaxOnCredit() {
        return taxOnCredit;
    }

    public void setTaxOnCredit(double taxOnCredit) {
        this.taxOnCredit = taxOnCredit;
    }

    public double getLifeInsurance() {
        return lifeInsurance;
    }

    public void setLifeInsurance(double lifeInsurance) {
        this.lifeInsurance = lifeInsurance;
    }

    private void computedBorrowedAmount() {
        borrowedAmount = getPrincipalLoanAmount() + commissionAmount + taxOnCredit + lifeInsurance;
    }

    public void setTAN(double TAN) {
        this.tan = TAN;
    }

    public double getTAN() {
        return tan;
    }

    public double getFixedAmtProcessingFee() {
        return fixedAmtProcessingFee;
    }

    public void setFixedAmtProcessingFee(double fixedAmtProcessingFee) {
        this.fixedAmtProcessingFee = fixedAmtProcessingFee;
    }

    public double getVarRateProcessingFee() {
        return varRateProcessingFee;
    }

    public void setVarRateProcessingFee(double varRateProcessingFee) {
        this.varRateProcessingFee = varRateProcessingFee;
    }

    public double getMonthlyProcessingPercent() {
        return monthlyProcessingPercent;
    }

    public void setMonthlyProcessingPercent(double monthlyProcessingPercent) {
        this.monthlyProcessingPercent = monthlyProcessingPercent;
    }

    public void setLifeInsuranceRequired(boolean lifeInsuranceRequired) {
        this.lifeInsuranceRequired = lifeInsuranceRequired;
    }

    public boolean isLifeInsuranceRequired() {
        return lifeInsuranceRequired;
    }

    public void setHasMonthlyProcessingFee(boolean hasMonthlyProcessingFee) {
        this.hasMonthlyProcessingFee = hasMonthlyProcessingFee;
    }

    public boolean isHasMonthlyProcessingFee() {
        return hasMonthlyProcessingFee;
    }

    public void setMonthlyProcessingFee(double monthlyProcessingFee) {
        this.monthlyProcessingFee = monthlyProcessingFee *1.04;
    }

    public double getMonthlyProcessingFee() {
        return monthlyProcessingFee;
    }

    public double getTotalInterest() {
        if (getTotalRepayment() == 0) {
            return 0;
        }
        return getTotalRepayment() - getPrincipalLoanAmount() ;
    }

    public double getTAEG() {
        return TAEG;
    }

    public void setTAEG(double TAEG) {
        this.TAEG = TAEG;
    }

    public double getLowestTaxRateTAN() {
        return lowestTaxRateTAN;
    }

    public void setLowestTaxRateTAN(double lowestTaxRateTAN) {
        this.lowestTaxRateTAN = lowestTaxRateTAN;
    }

    public double getTotalMonthlyPayment() {
        return getMonthlyPayment() + getMonthlyProcessingFee();
    }

    public static class InitialCommission {
        private double minimum;
        private double maximum;
        private boolean varRate;
        private double fixedAmount;
        private double rateAmount;

        public double getMinimum() {
            return minimum;
        }

        public void setMinimum(double minimum) {
            this.minimum = minimum;
        }

        public double getMaximum() {
            return maximum;
        }

        public void setMaximum(double maximum) {
            this.maximum = maximum;
        }

        public boolean isVarRate() {
            return varRate;
        }

        public void setVarRate(boolean varRate) {
            this.varRate = varRate;
        }

        public double getFixedAmount() {
            return fixedAmount;
        }

        public void setFixedAmount(double fixedAmount) {
            this.fixedAmount = fixedAmount;
        }

        public double getRateAmount() {
            return rateAmount;
        }

        public void setRateAmount(double rateAmount) {
            this.rateAmount = rateAmount;
        }

    }
}
