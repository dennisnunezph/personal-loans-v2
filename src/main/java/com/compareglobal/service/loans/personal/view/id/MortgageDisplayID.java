package com.compareglobal.service.loans.personal.view.id;

import com.compareglobal.service.common.loans.personal.domain.MortgageDisplay;
import com.compareglobal.service.common.utils.FormatterHelper;

public class MortgageDisplayID extends MortgageDisplay {
    private double totalRepayment;

    public MortgageDisplayID(MortgageID mortgage) {
        this.totalRepayment = mortgage.getTotalRepayment();
        this.setInterestRate(mortgage.getMonthlyInterestRate());
        this.setApr(mortgage.getApr());
        this.setMonthlyPayment(mortgage.getMonthlyPayment());
    }

    public String getTotalRepayment() {
        return FormatterHelper.decimalFormat(totalRepayment);
    }

}
