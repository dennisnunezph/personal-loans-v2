/*
 * Copyright (c) 2015.
 * Compare Asia Group
 */
package com.compareglobal.service.loans.personal.service.hk;

import com.compareglobal.service.common.loans.personal.domain.LoanRange;
import com.compareglobal.service.common.utils.CompareHelperHK;
import com.compareglobal.service.common.utils.ListHelper;
import com.compareglobal.service.loans.personal.domain.*;
import com.compareglobal.service.loans.personal.service.LoanService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dennis on 2/17/15.
 */
@Service("loanServiceHK")
public class LoanServiceHK implements LoanService {

    private final PersonalLoanHelper loanHelper;

    @Inject
    public LoanServiceHK(@Named("personalLoanHelperHK") PersonalLoanHelper loanHelper) {
        this.loanHelper = loanHelper;
    }

    @Override
    public List<PersonalLoanPublic> convert(List<PersonalLoan> personalLoans, Compare compare) {
        List<PersonalLoanPublic> loansList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(personalLoans)) {
            double loanAmount = Double.valueOf(compare.getLoanAmount());
            int tenure = Integer.valueOf(compare.getLoanTenure());
            for (PersonalLoan personalLoan : personalLoans) {
                boolean displayRecord = CompareHelperHK.recordIncluded(compare.getFilter(), personalLoan);
                LoanRange loanRange = new LoanRange(ListHelper.setToList(personalLoan.getEligibilityList()));
                if (displayRecord
                        && loanRange.allowed(loanAmount, tenure)) {
                    loansList.add(loanHelper.build(personalLoan, loanAmount, tenure));
                }
            }
        }
        return loansList;
    }

}
