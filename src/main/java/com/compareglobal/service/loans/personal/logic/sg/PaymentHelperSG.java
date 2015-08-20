package com.compareglobal.service.loans.personal.logic.sg;

import com.compareglobal.service.loans.personal.domain.Mortgage;
import com.compareglobal.service.loans.personal.logic.PaymentHelper;
import com.compareglobal.service.loans.personal.view.sg.MortgageSG;
import org.springframework.stereotype.Component;

/**
 * Created by dennis on 2/17/15.
 */
@Component("paymentHelperSG")
public class PaymentHelperSG implements PaymentHelper {

    @Override
    public double getLoanPayment(Object loan, double interestRate) {
        return 0;
    }

    @Override
    public double getLoanPayment(Object loan) {
        MortgageSG mortgage = (MortgageSG) loan;
        double monthlyPayment = 0;

        switch(mortgage.getCategoryProfile()) {
            case PersonalInstalment:
                monthlyPayment = mortgage.getInstalmentMonthlyPayment();
                break;
            case CreditLine:
                monthlyPayment = mortgage.getCreditLineMonthlyPayment();
                break;
            case BalanceTransfer:
                monthlyPayment = mortgage.getBalanceTransferMonthlyPayment();
                break;
            default:
        }
        return monthlyPayment;
    }

    @Override
    public double getAPR(int paymentInMonths, double paymentPerMonth, double presentValue, double interestRate) {
        return 0;
    }


}

