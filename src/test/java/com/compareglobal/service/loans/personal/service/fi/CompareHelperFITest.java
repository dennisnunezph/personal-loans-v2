package com.compareglobal.service.loans.personal.service.fi;

import com.compareglobal.service.common.domain.Filter;
import com.compareglobal.service.loans.personal.domain.Compare;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Luis Miguel Osorio.
 */
@RunWith(MockitoJUnitRunner.class)
public class CompareHelperFITest {

    @Test
    public void shouldHasFilterTermLoanFromFinland() {
        final Set<Filter> personalLoanFilters = getPersonalLoanFiltersFor("hasTermLoan");

        final CompareHelperFI compareHelperFI = new CompareHelperFI(personalLoanFilters);

        assertTrue("should has filter", compareHelperFI.hasFilter(Compare.Filter.TERMLOAN));
    }

    @Test
    public void shouldHasFilterRevolvingLoanLoanFromFinland() {
        final Set<Filter> personalLoanFilters = getPersonalLoanFiltersFor("hasRevolvingLoan");

        final CompareHelperFI compareHelperFI = new CompareHelperFI(personalLoanFilters);

        assertTrue("should has filter", compareHelperFI.hasFilter(Compare.Filter.REVOLVINGLOAN));
    }

    @Test
    public void shouldHasFilterQuickLoanFromFinland() {
        final Set<Filter> personalLoanFilters = getPersonalLoanFiltersFor("hasQuickLoan");

        final CompareHelperFI compareHelperFI = new CompareHelperFI(personalLoanFilters);

        assertTrue("should has filter", compareHelperFI.hasFilter(Compare.Filter.QUICKLOAN));
    }

    @Test
    public void shouldNotHasContactLessFilterFromFinland() {
        final Set<Filter> personalLoanFilters = getPersonalLoanFiltersFor("hasPayDay");

        final CompareHelperFI compareHelperFI = new CompareHelperFI(personalLoanFilters);

        assertFalse("should not has filter", compareHelperFI.hasFilter(Compare.Filter.PAYDAY));
    }

    private Set<Filter> getPersonalLoanFiltersFor(final String... filterTypeValues) {
        final Set<Filter> personalLoanFilters = new HashSet<>();
        for (String filterTypeValue: filterTypeValues) {
            final Filter filter = new Filter();
            filter.setTypeValue(filterTypeValue);
            filter.setValue("1");
            personalLoanFilters.add(filter);
        }
        return personalLoanFilters;
    }
}