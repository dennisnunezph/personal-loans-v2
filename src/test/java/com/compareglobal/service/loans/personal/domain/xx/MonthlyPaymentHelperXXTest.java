package com.compareglobal.service.loans.personal.domain.xx;


import com.compareglobal.service.common.domain.Filter;
import com.compareglobal.service.loans.personal.domain.PersonalLoan;
import com.compareglobal.service.loans.personal.domain.fees.Fee;
import com.compareglobal.service.loans.personal.domain.xx.MonthlyPaymentHelperXX;
import com.compareglobal.service.loans.personal.logic.PaymentHelper;
import com.compareglobal.service.loans.personal.logic.xx.PaymentHelperXX;
import com.compareglobal.service.loans.personal.view.xx.MortgageXX;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MonthlyPaymentHelperXXTest {
    private MonthlyPaymentHelperXX monthlyPaymentHelper;
    private PaymentHelper paymentHelper;
    private static final DecimalFormat df = new DecimalFormat("#######");
    private DecimalFormat dfRate = new DecimalFormat("#,###,###.##");
    private Set<Fee> feeList;
    private Set<Filter> filterList = new HashSet<>();
    private int tenure;
    private int loanAmount;

    @Before
    public void init() {
        paymentHelper = new PaymentHelperXX();
        monthlyPaymentHelper = new MonthlyPaymentHelperXX(paymentHelper);

        tenure = 24;
        loanAmount = 100000;

        Filter filter = new Filter();
        filter.setTypeValue(MonthlyPaymentHelperXX.MortgageKeys.HasCreditLine.getMortgageKey());
        filter.setValue("1");
        filterList.add(filter);

        feeList = new HashSet<>();
        Fee fee = new Fee();
        fee.setTypeValue(MonthlyPaymentHelperXX.MortgageKeys.MonthlyInterestRate.getMortgageKey());
        fee.setAmount(BigDecimal.valueOf(0.2));
        feeList.add(fee);

        Fee fee2 = new Fee();
        fee2.setTypeValue(MonthlyPaymentHelperXX.MortgageKeys.MinMonthlyPayment.getMortgageKey());
        fee2.setAmount(BigDecimal.valueOf(0.2));
        feeList.add(fee2);

        Fee fee3 = new Fee();
        fee3.setTypeValue(MonthlyPaymentHelperXX.MortgageKeys.MinMonthlyPaymentPercent.getMortgageKey());
        fee3.setAmount(BigDecimal.valueOf(0.2));
        feeList.add(fee3);

    }

    @Test
    public void populatePayment() {
        PersonalLoan personalLoan = mock(PersonalLoan.class);
        when(personalLoan.getFeesList()).thenReturn(feeList);

        MortgageXX loanPayment = monthlyPaymentHelper.populateMortgage(loanAmount, tenure, personalLoan);
        assertEquals("4367", df.format(loanPayment.getMonthlyPayment()));
    }

    @Test
    public void testCreditLineMonthlyRepayment(){

        PersonalLoan personalLoan = mock(PersonalLoan.class);
        when(personalLoan.getFilter()).thenReturn(filterList);
        when(personalLoan.getFeesList()).thenReturn(feeList);

        MortgageXX mortgage = monthlyPaymentHelper.populateMortgage(loanAmount, tenure, personalLoan);
        assertEquals("200",df.format(mortgage.getCreditLineMonthlyRepayment()));
    }

    @Test
    public void testCreditLineDailyInterest(){

        PersonalLoan personalLoan = mock(PersonalLoan.class);
        when(personalLoan.getFilter()).thenReturn(filterList);
        when(personalLoan.getFeesList()).thenReturn(feeList);

        MortgageXX mortgage = monthlyPaymentHelper.populateMortgage(loanAmount, tenure, personalLoan);
        assertEquals("0.55",dfRate.format(mortgage.getCreditLineDailyInterest()));
    }
}
