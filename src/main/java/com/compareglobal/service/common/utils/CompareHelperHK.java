/*
 * Copyright (c) 2015.
 * Compare Asia Group
 */

package com.compareglobal.service.common.utils;

import com.compareglobal.service.loans.personal.domain.Compare;
import com.compareglobal.service.loans.personal.domain.PersonalLoan;
import com.compareglobal.service.common.domain.Filter;

import java.util.*;

public class CompareHelperHK {

    private CompareHelperHK(){}

    public static boolean recordIncluded(Compare.Filter filter, PersonalLoan pl) {

        if (filter == null) {
            return true;
        }
        for (Filter baseFilter : pl.getFilter()) {
            if (Compare.Filter.INSTALMENT.equals(filter)
                    && baseFilter.getTypeValue().equals("hasPersonalInstalmentLoan")
                    && baseFilter.getValue().equals("1")) {
                return true;
            }

            if (Compare.Filter.TAXLOAN.equals(filter)
                    && baseFilter.getTypeValue().equals("hasTaxLoan")
                    && baseFilter.getValue().equals("1")) {
                return true;
            }

            if (Compare.Filter.DEBTCONSOLIDATION.equals(filter)
                    && baseFilter.getTypeValue().equals("hasDebtConsolidation")
                    && baseFilter.getValue().equals("1")) {
                return true;
            }

            if (Compare.Filter.CREDITLINE.equals(filter)
                    && baseFilter.getTypeValue().equals("hasPersonalCreditLine")
                    && baseFilter.getValue().equals("1")) {
                return true;
            }

            if (Compare.Filter.LENDINGCOMPANIES.equals(filter)
                    && baseFilter.getTypeValue().equals("hasFinancialfirm")
                    && baseFilter.getValue().equals("1")) {
                return true;
            }
        }
        return false;
    }
}