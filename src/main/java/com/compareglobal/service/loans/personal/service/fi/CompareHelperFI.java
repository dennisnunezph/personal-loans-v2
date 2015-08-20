package com.compareglobal.service.loans.personal.service.fi;

import com.compareglobal.service.common.domain.Filter;
import com.compareglobal.service.common.domain.Item;
import com.compareglobal.service.common.domain.converter.TypeValueHelper;
import com.compareglobal.service.loans.personal.domain.Compare;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Luis Miguel Osorio.
 *
 * Mapping between application filters and data base filters
 */
class CompareHelperFI {

    private final Map<Compare.Filter, Boolean> personalLoanCompareFilters;

    CompareHelperFI(final Set<Filter> personalLoanFilters) {

        personalLoanCompareFilters = new HashMap<>();

        final List<Item> personalLoanItemFilters = TypeValueHelper.getParsedItem(personalLoanFilters);
        for (Item personalLoanItemFilter : personalLoanItemFilters) {
            final boolean filterValue = isTrueValue(personalLoanItemFilter.getValue());
            final String filterType = personalLoanItemFilter.getType();

            // We can only use the existing values in Compare.Filter enum
            switch (filterType) {
                case "hasTermLoan":
                    personalLoanCompareFilters.put(Compare.Filter.TERMLOAN, filterValue);
                    personalLoanCompareFilters.put(Compare.Filter.BALANCETRANSFER, filterValue);
                    break;
                case "hasRevolvingLoan":
                    personalLoanCompareFilters.put(Compare.Filter.REVOLVINGLOAN, filterValue);
                    personalLoanCompareFilters.put(Compare.Filter.CONSOLIDATEDCREDIT, filterValue);
                    break;
                case "hasQuickLoan":
                    personalLoanCompareFilters.put(Compare.Filter.QUICKLOAN, filterValue);
                    personalLoanCompareFilters.put(Compare.Filter.CREDITLINE, filterValue);
                    break;
                default:
            }
        }
    }

    boolean hasFilter(final Compare.Filter filter) {
        return personalLoanCompareFilters.containsKey(filter) && personalLoanCompareFilters.get(filter);
    }

    private boolean isTrueValue(final String value) {
        return "true".equalsIgnoreCase(value) || "1".equals(value);
    }
}
