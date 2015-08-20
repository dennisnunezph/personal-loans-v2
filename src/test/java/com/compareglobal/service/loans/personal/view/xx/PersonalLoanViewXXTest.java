package com.compareglobal.service.loans.personal.view.xx;


import com.compareglobal.service.common.domain.Filter;
import com.compareglobal.service.loans.personal.domain.PersonalLoan;
import com.compareglobal.service.loans.personal.domain.fees.Fee;
import com.compareglobal.service.loans.personal.view.xx.PersonalLoanViewXX;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PersonalLoanViewXXTest {
    private PersonalLoanViewXX viewXX;

    @Before
    public void init() {
        viewXX = new PersonalLoanViewXX();
    }

    @Test
    public void earlyRepaymentFee() throws Exception {
        PersonalLoan personalLoan = mock(PersonalLoan.class);

        Set<Filter> filterList = new HashSet<>();
        Filter applyFilter = new Filter();
        applyFilter.setTypeValue("hasApplyBtn");
        applyFilter.setValue("false");
        filterList.add(applyFilter);
        when(personalLoan.getFilter()).thenReturn(filterList);

        Set<Fee> feeList = new HashSet<>();
        Fee earlyRepaymentFee = new Fee();
        earlyRepaymentFee.setTypeValue(PersonalLoanViewXX.ViewKeys.EarlyRepaymentFee.getViewKey());
        earlyRepaymentFee.setAmount(BigDecimal.valueOf(5.00));
        feeList.add(earlyRepaymentFee);

        when(personalLoan.getFeesList()).thenReturn(feeList);

        JSONObject result = viewXX.view(personalLoan);

        JSONObject fees = (JSONObject) result.get("fees");
        Boolean repaymentFee = (Boolean) fees.get(PersonalLoanViewXX.ViewKeys.EarlyRepaymentFee.getViewKey());
        assertTrue(repaymentFee);
    }
}
