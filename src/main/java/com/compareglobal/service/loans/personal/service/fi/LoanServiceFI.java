package com.compareglobal.service.loans.personal.service.fi;

import com.compareglobal.service.common.loans.personal.domain.LoanRange;
import com.compareglobal.service.common.utils.ListHelper;
import com.compareglobal.service.loans.personal.domain.Compare;
import com.compareglobal.service.loans.personal.domain.PersonalLoan;
import com.compareglobal.service.loans.personal.domain.PersonalLoanHelper;
import com.compareglobal.service.loans.personal.domain.PersonalLoanPublic;
import com.compareglobal.service.loans.personal.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luis Miguel Osorio.
 */
@Component("loanServiceFI")
public class LoanServiceFI implements LoanService {

    private final PersonalLoanHelper personalLoanHelper;

    @Autowired
    public LoanServiceFI(@Qualifier("personalLoanHelperFI") final PersonalLoanHelper personalLoanHelper) {
        this.personalLoanHelper = personalLoanHelper;
    }

    @Override
    public List<PersonalLoanPublic> convert(final List<PersonalLoan> personalLoans, final Compare compare) {
        final List<PersonalLoanPublic> personalLoansResult = new ArrayList<>();

        // Filter passed from the request
        final Compare.Filter requestFilter = compare.getFilter();
        final Double loanAmount = Double.valueOf(compare.getLoanAmount());
        final Integer loanTenure = compare.getLoanTenure();

        if (requestFilter != null) {
            // Apply filter on each personal loan
            for (PersonalLoan personalLoan : personalLoans) {
                if (hasFilter(personalLoan, requestFilter) && isInRange(personalLoan, loanAmount, loanTenure)){
                    personalLoansResult.add(personalLoanHelper.build(personalLoan, loanAmount, loanTenure));
                }
            }
        } else {
            // There is no filter to apply
            for (PersonalLoan personalLoan : personalLoans) {
                if (isInRange(personalLoan, loanAmount, loanTenure)) {
                    personalLoansResult.add(personalLoanHelper.build(personalLoan, loanAmount, loanTenure));
                }
            }
        }
        return personalLoansResult;
    }

    private boolean isInRange(final PersonalLoan personalLoan, final Double loanAmount, final Integer loanTenure) {
        final LoanRange loanRange = new LoanRange(ListHelper.setToList(personalLoan.getEligibilityList()));
        return loanRange.allowed(loanAmount, loanTenure);
    }

    private boolean hasFilter(final PersonalLoan personalLoan, final Compare.Filter filter) {
        // Mapping personal loan filters and verify that it matches with the request filter
        if (personalLoan.getFilter() != null) {
            final CompareHelperFI compareHelperFI = new CompareHelperFI(personalLoan.getFilter());
            return compareHelperFI.hasFilter(filter);
        }
        return false;
    }
}
