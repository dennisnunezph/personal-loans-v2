package com.compareglobal.service.loans.personal.domain.fi;

import com.compareglobal.service.loans.personal.domain.PersonalLoan;
import com.compareglobal.service.loans.personal.logic.fi.PaymentHelperFI;
import com.compareglobal.service.loans.personal.view.fi.MortgageDisplayFI;

/**
 * Created by Luis Miguel Osorio.
 */
class MortgageHelperFI {

    public static MortgageDisplayFI createMortgageDisplay(final double loanAmount, final int loanTenure, final PersonalLoan personalLoan) {

        // Mapping all the needed values from data base to make the computations
        final PaymentHelperFI paymentHelper = new PaymentHelperFI(personalLoan);

        final MortgageDisplayFI mortgageDisplay = new MortgageDisplayFI();

        mortgageDisplay.setLoanAmount(loanAmount);
        mortgageDisplay.setLoanTenure(loanTenure);

        mortgageDisplay.setMonthlyInterestRate(paymentHelper.getMonthlyInterestRate());
        mortgageDisplay.setMonthlyRepayment(paymentHelper.getMonthlyRepayment(loanAmount, loanTenure));
        mortgageDisplay.setTotalRepayment(paymentHelper.getTotalPayment(loanAmount, loanTenure));
        mortgageDisplay.setEAR(paymentHelper.getEAR(loanAmount, loanTenure));

        mortgageDisplay.setMaxEAR(paymentHelper.getMaxEAR(loanAmount, loanTenure));
        mortgageDisplay.setMaxTotalRepayment(paymentHelper.getMaxTotalRepayment(loanAmount, loanTenure));
        mortgageDisplay.setMaxMonthlyRepayment(paymentHelper.getMaxMonthlyRepayment(loanAmount, loanTenure));

        return mortgageDisplay;
    }

}
