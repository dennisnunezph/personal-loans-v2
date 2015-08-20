package com.compareglobal.service.common.utils;

import com.compareglobal.service.common.domain.Filter;
import com.compareglobal.service.loans.personal.domain.Compare;
import com.compareglobal.service.loans.personal.domain.PersonalLoan;

/**
 * Created by dennis on 4/22/15.
 */
public class CompareHelperPT implements CompareHelper {

    @Override
    public boolean recordIncluded(Compare.Filter filter, PersonalLoan pl) {
        if (filter == null) {
            return true;
        }
        for (Filter baseFilter : pl.getFilter()) {
            if (Compare.Filter.QUICKCASH.equals(filter)
                    && baseFilter.getTypeValue().equals("hasQuickCash")
                    && baseFilter.getValue().equals("1")) {
                return true;
            }

            if (Compare.Filter.CONSOLIDATEDCREDIT.equals(filter)
                    && baseFilter.getTypeValue().equals("hasConsolidatedCredit")
                    && baseFilter.getValue().equals("1")) {
                return true;
            }

            if (Compare.Filter.SPECIALIST.equals(filter)
                    && baseFilter.getTypeValue().equals("hasSpecialist")
                    && baseFilter.getValue().equals("1")) {
                return true;
            }

            if ((Compare.Filter.PERSONALLOAN.equals(filter) || Compare.Filter.PERSONALINST.equals(filter))
                    && baseFilter.getTypeValue().equals("hasPersonalLoan")
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
