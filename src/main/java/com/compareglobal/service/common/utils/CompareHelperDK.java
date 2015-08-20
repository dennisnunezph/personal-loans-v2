/*
 * Copyright (c) 2015.
 * Compare Asia Group
 */

package com.compareglobal.service.common.utils;

import com.compareglobal.service.common.domain.Filter;
import com.compareglobal.service.loans.personal.domain.Compare;
import com.compareglobal.service.loans.personal.domain.PersonalLoan;

public class CompareHelperDK {

    private CompareHelperDK(){}

    public static boolean recordIncluded(Compare.Filter filter, PersonalLoan pl) {

        if (filter == null) {
            return true;
        }

        for (Filter baseFilter : pl.getFilter()) {
            if (Compare.Filter.ONLINELAAN.equals(filter)
                    && baseFilter.getTypeValue().equals("onlineLaanTap")
                    && baseFilter.getValue().equals("1")) {
                return true;
            }

            if (Compare.Filter.KVIKLAAN.equals(filter)
                    && baseFilter.getTypeValue().equals("kviklaanTap")
                    && baseFilter.getValue().equals("1")) {
                return true;
            }

            if (Compare.Filter.DINBANK.equals(filter)
                    && baseFilter.getTypeValue().equals("dinBankTap")
                    && baseFilter.getValue().equals("1")) {
                return true;
            }

            if (filter == null
                    && baseFilter.getTypeValue().equals("allTab")
                    && baseFilter.getValue().equals("1")) {
                return true;
            }
        }
        return false;
    }
}