package com.compareglobal.service.loans.personal.service.dk;

import com.compareglobal.service.common.utils.CompareHelperDK;
import com.compareglobal.service.common.utils.ListHelper;
import com.compareglobal.service.loans.personal.domain.*;
import com.compareglobal.service.loans.personal.service.LoanService;
import com.compareglobal.service.common.loans.personal.domain.LoanRange;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dennis on 2/17/15.
 */
@Component("loanServiceDK")
public class LoanServiceDK implements LoanService {

    private final PersonalLoanHelper personalLoanHelper;


    @Inject
    public LoanServiceDK(@Named("personalLoanHelperDK") PersonalLoanHelper personalLoanHelper) {
        this.personalLoanHelper = personalLoanHelper;
    }

    @Override
    public List<PersonalLoanPublic> convert(List<PersonalLoan> personalLoans, Compare compare) {
        List<PersonalLoanPublic> loansList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(personalLoans)) {
            double loanAmount = Double.valueOf(compare.getLoanAmount());
            int tenure = Integer.valueOf(compare.getLoanTenure());
            for (PersonalLoan personalLoan : personalLoans) {
                boolean displayRecord = CompareHelperDK.recordIncluded(compare.getFilter(), personalLoan);
                LoanRange loanRange = new LoanRange(ListHelper.setToList(personalLoan.getEligibilityList()));
                if (displayRecord
                        && loanRange.allowed(loanAmount, tenure)) {
                    loansList.add(personalLoanHelper.build(personalLoan, loanAmount, tenure));
                }
            }
        }
        return loansList;
    }
}