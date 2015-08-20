package com.compareglobal.service.loans.personal.service.xx;

import com.compareglobal.service.common.domain.Filter;
import com.compareglobal.service.common.loans.personal.domain.LoanRange;
import com.compareglobal.service.loans.personal.domain.Compare;
import com.compareglobal.service.loans.personal.domain.PersonalLoan;
import com.compareglobal.service.loans.personal.domain.PersonalLoanHelper;
import com.compareglobal.service.loans.personal.domain.PersonalLoanPublic;
import com.compareglobal.service.loans.personal.domain.eligibility.Criteria;
import com.compareglobal.service.loans.personal.service.xx.LoanServiceXX;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;


public class LoanServiceXXTest {
    private LoanServiceXX serviceXX;
    private PersonalLoanHelper mockLoanHelper;

    @Before
    public void init() {
        mockLoanHelper = mock(PersonalLoanHelper.class);
        serviceXX = new LoanServiceXX(mockLoanHelper);
    }

    @Test
    public void convert() throws Exception {
        List<PersonalLoan> plStub = new ArrayList<>();
        PersonalLoan pl1 = new PersonalLoan();
        plStub.add(pl1);

        PersonalLoan pl2 = mock(PersonalLoan.class);
        Set<Criteria> criteriaStub = new HashSet<>();
        Criteria maxAmountCriteria = mock(Criteria.class);
        when(maxAmountCriteria.getMinimum()).thenReturn(1000);
        when(maxAmountCriteria.getMaximum()).thenReturn(100000);
        when(maxAmountCriteria.getTypeValue()).thenReturn(LoanRange.LoanRangeKeys.LoanAmount.getRangeKey());
        criteriaStub.add(maxAmountCriteria);

        Criteria tenureCriteria = mock(Criteria.class);
        when(tenureCriteria.getMinimum()).thenReturn(0);
        when(tenureCriteria.getMaximum()).thenReturn(12);
        when(tenureCriteria.getTypeValue()).thenReturn(LoanRange.LoanRangeKeys.LoanTenure.getRangeKey());
        criteriaStub.add(tenureCriteria);


        when(pl2.getEligibilityList()).thenReturn(criteriaStub);
        plStub.add(pl2);

        Compare compare = new Compare();
        compare.setLoanAmount(100000);
        compare.setLoanTenure(12);
        List<PersonalLoanPublic> result = serviceXX.convert(plStub, compare);
        assertEquals(1, result.size());
    }

    @Test
    public void noRecordIncluded() throws Exception {
        List<PersonalLoan> plStub = new ArrayList<>();
        PersonalLoan pl1 = new PersonalLoan();
        plStub.add(pl1);
        PersonalLoan pl2 = new PersonalLoan();
        plStub.add(pl2);

        Compare compare = new Compare();
        compare.setLoanAmount(100000);
        compare.setLoanTenure(12);
        compare.setFilter(Compare.Filter.CREDITLINE);
        List<PersonalLoanPublic> result = serviceXX.convert(plStub, compare);
        assertEquals(0, result.size());
    }

    @Test
    public void includeRecordViaFilterIncluded() throws Exception {
        List<PersonalLoan> plStub = new ArrayList<>();
        PersonalLoan pl1 = new PersonalLoan();
        plStub.add(pl1);

        PersonalLoan pl2 = mock(PersonalLoan.class);
        Set<Filter> filterStub = new HashSet<>();
        Filter filter1 = new Filter();
        filter1.setTypeValue("hasPersonalCreditLine");
        filter1.setValue("1");
        filterStub.add(filter1);
        when(pl2.getFilter()).thenReturn(filterStub);

        Set<Criteria> criteriaStub = new HashSet<>();
        Criteria maxAmountCriteria = mock(Criteria.class);
        when(maxAmountCriteria.getMinimum()).thenReturn(1000);
        when(maxAmountCriteria.getMaximum()).thenReturn(100000);
        when(maxAmountCriteria.getTypeValue()).thenReturn(LoanRange.LoanRangeKeys.LoanAmount.getRangeKey());
        criteriaStub.add(maxAmountCriteria);

        Criteria tenureCriteria = mock(Criteria.class);
        when(tenureCriteria.getMinimum()).thenReturn(0);
        when(tenureCriteria.getMaximum()).thenReturn(12);
        when(tenureCriteria.getTypeValue()).thenReturn(LoanRange.LoanRangeKeys.LoanTenure.getRangeKey());
        criteriaStub.add(tenureCriteria);


        when(pl2.getEligibilityList()).thenReturn(criteriaStub);
        plStub.add(pl2);

        Compare compare = new Compare();
        compare.setLoanAmount(100000);
        compare.setLoanTenure(12);
        compare.setFilter(Compare.Filter.CREDITLINE);
        List<PersonalLoanPublic> result = serviceXX.convert(plStub, compare);
        assertEquals(1, result.size());
        verify(pl2).getFilter();
    }
}
