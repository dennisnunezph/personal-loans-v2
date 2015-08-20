package com.compareglobal.service.loans.personal.view.xx;

import com.compareglobal.service.common.loans.personal.domain.MortgageDisplay;
import com.compareglobal.service.common.utils.FormatterHelper;
import com.compareglobal.service.loans.personal.domain.Mortgage;

public class MortgageDisplayXX extends MortgageDisplay {
    private double monthlyPaymentForTaxLoan;
    private double totalRepayment;
    private double creditLineAnnualInterestRate;
    private double creditLineMonthlyRepayment;
    private double creditLineDailyInterest;

    public MortgageDisplayXX(Mortgage mortgage) {
        if (mortgage != null) {
            this.monthlyPaymentForTaxLoan = mortgage.getMonthlyPaymentForTaxLoan();
            this.totalRepayment = mortgage.getTotalRepayment();
            this.creditLineAnnualInterestRate = mortgage.getCreditLineAnnualInterestRate();
            this.creditLineMonthlyRepayment = mortgage.getCreditLineMonthlyRepayment();
            this.creditLineDailyInterest = mortgage.getCreditLineDailyInterest();
            this.setInterestRate(mortgage.getMonthlyInterestRate());
            this.setApr(mortgage.getApr());
            this.setMonthlyPayment(mortgage.getMonthlyPayment());
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
}
