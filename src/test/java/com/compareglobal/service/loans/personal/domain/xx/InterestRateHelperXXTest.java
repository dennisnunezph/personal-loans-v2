package com.compareglobal.service.loans.personal.domain.xx;


import com.compareglobal.service.loans.personal.logic.xx.PaymentHelperXX;
import com.compareglobal.service.loans.personal.view.xx.MortgageXX;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class InterestRateHelperXXTest {
    private PaymentHelperXX paymentHelper;
    private InterestRateHelperXX interestRateHelper;
    private static final DecimalFormat df = new DecimalFormat("#######");
    private MortgageXX mortgage;
    private List<InterestRateInfoXX> interestList;

    @Before
    public void init() {
        paymentHelper = new PaymentHelperXX();
        interestRateHelper = new InterestRateHelperXX();
        mortgage = new MortgageXX();
        mortgage.setPrincipalLoanAmount(100000);
        mortgage.setTenureInMonths(24);
        mortgage.setHandlingFeeType(1);
        mortgage.setInterestProfile(0);
        mortgage.setMonthlyInterestRate(0.11);

        mortgage.setMinMonthlyPayment(0.00);
        mortgage.setMinMonthlyPaymentPercent(0.50);


        InterestRateInfoXX interest = new InterestRateInfoXX();
        interest.setInterestRateType(InterestRateTypeXX.Fixed);
        interest.setAmount(179999L);
        interest.setPeriod(2);
        interest.setRate(BigDecimal.valueOf(9.8));

        InterestRateInfoXX interest2 = new InterestRateInfoXX();

        InterestRateInfoXX interest3 = new InterestRateInfoXX();
        interest3.setInterestRateType(InterestRateTypeXX.Annual);
        interest3.setAmount(179999L);
        interest3.setPeriod(1);
        interest3.setRate(BigDecimal.valueOf(9.8));

        interestList = new ArrayList<>();
        interestList.add(interest);
        interestList.add(interest2);
        interestList.add(interest3);

    }

    @Test
    public void monthlyPaymentCitibankSpeedyCash() throws Exception {

        mortgage.setHandlingFeeType(0);
        interestRateHelper.updateMortgageInterest(mortgage, interestList);
        double monthlyPayment = paymentHelper.getLoanPayment(mortgage);
        assertEquals("4277", df.format(monthlyPayment));
    }

    @Test
    public void dreamCashPersonalLoanNoHandlingFee() throws Exception {

        mortgage.setInterestProfile(21);
        mortgage.setMonthlyInterestRate(0.125);
        interestRateHelper.updateMortgageInterest(mortgage, interestList);
        double monthlyPayment = paymentHelper.getLoanPayment(mortgage);
        assertEquals("4292", df.format(monthlyPayment));
    }

    @Test
    public void dreamCashPersonalLoanWithHandlingFeeAsActual() throws Exception {

        mortgage.setInterestProfile(11);
        mortgage.setAnnualHandlingFee(1);
        mortgage.setMonthlyInterestRate(0.125);
        interestRateHelper.updateMortgageInterest(mortgage, interestList);
        double monthlyPayment = paymentHelper.getLoanPayment(mortgage);
        assertNotEquals("4292", df.format(monthlyPayment));
    }

    @Test
    public void testGetMonthlyPaymentIfHandlingFee1() throws Exception {

        mortgage.setHandlingFeeType(1);
        mortgage.setAnnualHandlingFee(1);
        interestRateHelper.updateMortgageInterest(mortgage, interestList);
        double monthlyPayment = paymentHelper.getLoanPayment(mortgage);
        assertEquals("4360", df.format(monthlyPayment));
    }

    @Test
    public void testGetMonthlyPaymentIfHandlingFee3() throws Exception {

        mortgage.setHandlingFeeType(3);
        mortgage.setAnnualHandlingFee(1);
        mortgage.setMinHandlingFee(0);

        interestRateHelper.updateMortgageInterest(mortgage, interestList);
        double monthlyPayment = paymentHelper.getLoanPayment(mortgage);
        assertEquals("4318", df.format(monthlyPayment));

        mortgage.setAnnualHandlingFee(0);
        mortgage.setMinHandlingFee(1);

        interestRateHelper.updateMortgageInterest(mortgage, interestList);
        monthlyPayment = paymentHelper.getLoanPayment(mortgage);
        assertEquals("4277", df.format(monthlyPayment));
    }

    @Test
    public void testGetMonthlyPaymentIfHandlingFee4() throws Exception {

        mortgage.setHandlingFeeType(4);
        mortgage.setAnnualHandlingFee(1);
        mortgage.setMinHandlingFee(0);

        interestRateHelper.updateMortgageInterest(mortgage, interestList);
        double monthlyPayment = paymentHelper.getLoanPayment(mortgage);
        assertEquals("4318", df.format(monthlyPayment));

        mortgage.setAnnualHandlingFee(0);
        mortgage.setMinHandlingFee(1);

        interestRateHelper.updateMortgageInterest(mortgage, interestList);
        monthlyPayment = paymentHelper.getLoanPayment(mortgage);
        assertEquals("4277", df.format(monthlyPayment));
    }

    @Test
    public void testGetMonthlyPaymentIfHandlingFee5() throws Exception {

        mortgage.setHandlingFeeType(5);

        interestRateHelper.updateMortgageInterest(mortgage, interestList);
        double monthlyPayment = paymentHelper.getLoanPayment(mortgage);
        assertEquals("178", df.format(monthlyPayment));
    }


    @Test
    public void testGetCreditLineDailyInterest() throws Exception {
        MonthlyPaymentHelperXX creditLineHelper = new MonthlyPaymentHelperXX(paymentHelper);
        double loanAmount = 100000;
        double annualRate = 9.80;

        double dailyInterest = creditLineHelper.getCreditLineDailyInterest(loanAmount,annualRate);
        assertEquals("27", df.format(dailyInterest));

        double dailyInterest2 = creditLineHelper.getCreditLineDailyInterest(loanAmount,0);
        assertEquals("0", df.format(dailyInterest2));
    }

    @Test
    public void testGetCreditLineMonthlyRepayment() throws Exception {
        MonthlyPaymentHelperXX creditLineHelper = new MonthlyPaymentHelperXX(paymentHelper);

        double loanAmount = 100000;

        double dailyInterest = creditLineHelper.getCreditLineMonthlyRepayment(loanAmount, 0, 0.50);
        assertEquals("500", df.format(dailyInterest));


        double dailyInterest2 = creditLineHelper.getCreditLineMonthlyRepayment(loanAmount, 0, 0);
        assertEquals("0", df.format(dailyInterest2));

        double dailyInterest3 = creditLineHelper.getCreditLineMonthlyRepayment(loanAmount, 50, 0);
        assertEquals("50", df.format(dailyInterest3));

        double dailyInterest4 = creditLineHelper.getCreditLineMonthlyRepayment(loanAmount, 50, 0.50);
        assertEquals("500", df.format(dailyInterest4));
    }
}
