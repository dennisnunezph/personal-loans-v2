package com.compareglobal.service.loans.personal.logic.pt;

import com.compareglobal.service.loans.personal.logic.PaymentHelper;
import com.compareglobal.service.loans.personal.logic.RateCalculation;
import com.compareglobal.service.loans.personal.view.pt.MortgagePT;
import org.apache.poi.ss.formula.functions.FinanceLib;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Created by dennis on 4/24/15.
 */
@Component("paymentHelperPT")
public class PaymentHelperPT  implements PaymentHelper {

    private static final double COMMISSION_RATE = 1.04;
    private static final double TAXRATE_LESS_THAN_A_YEAR = 0.0007;
    private static final double TAX_RATE_MORE_THAN_FIVE_YEARS = 0.01;
    private static final double TAX_RATE_FIVE_YEAR_OR_LESS = 0.009;
    private static final int ONE_YEAR = 12;
    private static final int FIVE_YEARS = 60;
    private static final double INSURANCE_RATE = 0.0075;
    private static final double TAN_RATE = 1.04;
    private static final double DEFAULT_PROCESSING_FEE_RATE = 0.02;


    @Override
    public double getLoanPayment(Object loan, double interestRate) {
        return 0;
    }

    @Override
    public double getLoanPayment(Object loan) {
        MortgagePT loanPayment = (MortgagePT) loan;
        double tan = loanPayment.getTAN();
        double rate = loanPayment.getTAN() > 0 ? (tan * TAN_RATE) / ONE_YEAR : 0;
        double periods = loanPayment.getTenureInMonths();
        double futureValue = loanPayment.getBorrowedAmount();
        double monthlyFee = loanPayment.getMonthlyProcessingFee();
        return  Math.abs(FinanceLib.pmt(rate, periods, -futureValue, 0, false) + monthlyFee);
    }

    @Override
    public double getAPR(int paymentInMonths, double paymentPerMonth, double presentValue, double interestRate) {
        return 0;
    }

    public double computeCommissionAmount(MortgagePT mortgage) {
        MortgagePT.InitialCommission initialCommission = mortgage.getInitialCommission();
        double minCommission = initialCommission.getMinimum();
        double maxCommission = initialCommission.getMaximum();
        double fixedAmount = initialCommission.getFixedAmount();
        double varAmount = initialCommission.getRateAmount();
        double loanAmount = mortgage.getPrincipalLoanAmount();
        
		if (maxCommission > 0
                && minCommission > 0){
            double cAmount = Math.min(maxCommission,
                                      Math.max(minCommission,
                                               fixedAmount + varAmount * loanAmount));
			return cAmount * COMMISSION_RATE;
		}else{
	        return (fixedAmount + varAmount * loanAmount) * COMMISSION_RATE;
		}
    }


    public double getTaxOnCredit(double loanAmount, double loanTenure) {
        double taxOnCreditRate;
        if (loanTenure < ONE_YEAR) {
            taxOnCreditRate = TAXRATE_LESS_THAN_A_YEAR;
            return  taxOnCreditRate * loanTenure * loanAmount;
        } else {
            taxOnCreditRate = loanTenure > FIVE_YEARS ? TAX_RATE_MORE_THAN_FIVE_YEARS : TAX_RATE_FIVE_YEAR_OR_LESS;
            return  taxOnCreditRate * loanAmount;
        }
    }

    public double getLifeInsurance(double loanAmount, double loanTenure, boolean required) {
        return required ? (loanAmount * INSURANCE_RATE) / ONE_YEAR * loanTenure : 0;
    }

    public double getBorrowedAmount(MortgagePT mortgage) {
        return mortgage.getBorrowedAmount();
    }

    public double getMonthlyProcessingFee(MortgagePT mortgage) {
		double pFee;
        double processingFee = mortgage.getFixedAmtProcessingFee();
        double processingVar;// = mortgage.getVarRateProcessingFee();
        if (mortgage.isHasMonthlyProcessingFee()
                && 0 == mortgage.getVarRateProcessingFee()
                && 0 == mortgage.getFixedAmtProcessingFee()) {
            processingVar = DEFAULT_PROCESSING_FEE_RATE;
        } else {
            processingVar = mortgage.getVarRateProcessingFee();
        }		
        pFee = processingFee + processingVar * mortgage.getMonthlyPayment();
		return pFee;
    }

    public double getProcessingPercent(MortgagePT mortgage) {
        if (mortgage.isHasMonthlyProcessingFee()
                && 0 == mortgage.getVarRateProcessingFee()
                && 0 == mortgage.getFixedAmtProcessingFee()) {
            return DEFAULT_PROCESSING_FEE_RATE;
        } else {
            return mortgage.getVarRateProcessingFee();
        }
    }

    public double getTotalPayment(MortgagePT mortgage) {
        double monthlyPayment = mortgage.getTotalMonthlyPayment();
        int tenure = mortgage.getTenureInMonths();
        return monthlyPayment * tenure;
    }

    public double getTAEG(MortgagePT mortgage) {
        int tenure = mortgage.getTenureInMonths();
        double monthlyPayment = mortgage.getTotalMonthlyPayment();//mortgage.getMonthlyPayment();
        double loanAmount = mortgage.getPrincipalLoanAmount();
        return RateCalculation.jsRate(tenure, -monthlyPayment, loanAmount, 0, 0, 0) * 12;
    }
}

