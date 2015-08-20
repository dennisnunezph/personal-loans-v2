package com.compareglobal.service.loans.personal.domain.dk;

import com.compareglobal.service.common.loans.personal.domain.MortgageDisplay;
import com.compareglobal.service.common.utils.FormatterHelper;

/**
 * Created by dennis on 2/18/15.
 */
public class MortgageDisplayDK extends MortgageDisplay {
    private double interestRateMax;
    private double monthlyPaymentMax;
    private double aprMax;
    private String legalText;

    public String getAprMax() {
        return FormatterHelper.decimalFormatWithComma(aprMax);
    }

    public void setAprMax(double aprMax) {
        this.aprMax = aprMax;
    }

    public String getInterestRateMax() {
        return FormatterHelper.decimalFormatForPercentWithComma(interestRateMax);
    }

    public void setInterestRateMax(double interestRateMax) {
        this.interestRateMax = interestRateMax;
    }

    public String getMonthlyPaymentMax() {
        return FormatterHelper.decimalFormatWithComma(monthlyPaymentMax);
    }

    public void setMonthlyPaymentMax(double monthlyPaymentMax) {
        this.monthlyPaymentMax = monthlyPaymentMax;
    }

    public String getInterestRate() {
        return FormatterHelper.decimalFormatForPercentWithComma(interestRate);
    }

    public String getApr() {
        return FormatterHelper.decimalFormatWithComma(apr);
    }

    public String getMonthlyPayment() {
        return FormatterHelper.decimalFormatWithComma(monthlyPayment);
    }

    public void setLegalText(String legalText) {
        this.legalText = legalText;
    }

    public String getLegalText() {
        return legalText;
    }
}
