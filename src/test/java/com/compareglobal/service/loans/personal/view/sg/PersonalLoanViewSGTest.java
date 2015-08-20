package com.compareglobal.service.loans.personal.view.sg;

import com.compareglobal.service.common.domain.Filter;
import com.compareglobal.service.loans.personal.domain.PersonalLoan;
import com.compareglobal.service.loans.personal.domain.fees.Fee;
import org.json.simple.JSONObject;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PersonalLoanViewSGTest {

    private PersonalLoanViewSG viewSG;
    private Set<Fee> feeList;
    private Set<Filter> filterList;

    @Before
    public void init() {
        viewSG = new PersonalLoanViewSG();

        feeList = new HashSet<>();
        Fee earlyRepaymentFee = new Fee();
        earlyRepaymentFee.setTypeValue(PersonalLoanViewSG.ViewKeys.EarlyRepaymentFee.getViewKey());
        earlyRepaymentFee.setAmount(BigDecimal.valueOf(8.00));
        feeList.add(earlyRepaymentFee);

        filterList = new HashSet<>();
        Filter applyFilter = new Filter();
        applyFilter.setTypeValue("hasApplyBtn");
        applyFilter.setValue("false");
        filterList.add(applyFilter);
    }

    @Test
    public void testView() throws Exception {

        PersonalLoan personalLoan = mock(PersonalLoan.class);
        when(personalLoan.getFilter()).thenReturn(filterList);
        when(personalLoan.getFeesList()).thenReturn(feeList);

        JSONObject result = viewSG.view(personalLoan);

        JSONObject fees = (JSONObject) result.get("fees");
        Boolean repaymentFee = (Boolean) fees.get(PersonalLoanViewSG.ViewKeys.EarlyRepaymentFee.getViewKey());
        assertTrue(repaymentFee);
    }
}
