package com.compareglobal.service.loans.personal.view.sg;

import org.junit.Before;
import org.junit.Test;
import java.text.DecimalFormat;
import static junit.framework.TestCase.assertEquals;

public class MortgageSGTest {

    MortgageSG mortgageSG;
    final int MONTHS_IN_A_YEAR = 12;
    private DecimalFormat df = new DecimalFormat("######.##");

    @Before
    public void init() {
        mortgageSG = new MortgageSG();
        mortgageSG.setPrincipalLoanAmount(25000);
        mortgageSG.setTenureInMonths(24);
        mortgageSG.setAnnualInterestRate(0.01);
    }

    @Test
    public void testGetTotalInterest() throws Exception {

        double interest =  mortgageSG.getPrincipalLoanAmount() * (mortgageSG.getTenureInMonths()/ MONTHS_IN_A_YEAR) * mortgageSG.getAnnualInterestRate();
        assertEquals("500", df.format(interest));
    }

    @Test
    public void testGetInstalmentMonthlyPayment() throws Exception {
        int loanAmount = 25000;
        int loanTenure = 84;
        double totalInterest = mortgageSG.getPrincipalLoanAmount() * (mortgageSG.getTenureInMonths() / MONTHS_IN_A_YEAR) * mortgageSG.getAnnualInterestRate();
        mortgageSG.setTotalRepayment(totalInterest + loanAmount);

        double installmentMonthly = 0.0;
        if (mortgageSG.getTotalRepayment() > 0) {
            installmentMonthly = mortgageSG.getTotalRepayment() / loanTenure;
        }

        assertEquals("303.57", df.format(installmentMonthly));
    }



}
