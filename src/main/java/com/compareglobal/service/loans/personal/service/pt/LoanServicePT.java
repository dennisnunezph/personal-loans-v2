/*
 * Copyright (c) 2015.
 * Compare Asia Group
 */
package com.compareglobal.service.loans.personal.service.pt;

import com.compareglobal.service.common.loans.personal.domain.LoanRange;
import com.compareglobal.service.common.utils.CompareHelper;
import com.compareglobal.service.common.utils.CompareHelperHK;
import com.compareglobal.service.common.utils.CompareHelperPT;
import com.compareglobal.service.common.utils.ListHelper;
import com.compareglobal.service.loans.personal.domain.Compare;
import com.compareglobal.service.loans.personal.domain.PersonalLoan;
import com.compareglobal.service.loans.personal.domain.PersonalLoanHelper;
import com.compareglobal.service.loans.personal.domain.PersonalLoanPublic;
import com.compareglobal.service.loans.personal.service.LoanService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dennis on 4/22/15.
 */
@Service("loanServicePT")
public class LoanServicePT implements LoanService {

    private final PersonalLoanHelper loanHelper;
    private CompareHelper compareHelper;

    @Inject
    public LoanServicePT(@Named("personalLoanHelperPT") PersonalLoanHelper loanHelper) {
        this.loanHelper = loanHelper;
        compareHelper = new CompareHelperPT();
    }

    @Override
    public List<PersonalLoanPublic> convert(List<PersonalLoan> personalLoans, Compare compare) {
        List<PersonalLoanPublic> loansList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(personalLoans)) {
            double loanAmount = Double.valueOf(compare.getLoanAmount());
            int tenure = Integer.valueOf(compare.getLoanTenure());
            for (PersonalLoan personalLoan : personalLoans) {
                boolean displayRecord = compareHelper.recordIncluded(compare.getFilter(), personalLoan);
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
