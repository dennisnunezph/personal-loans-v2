package com.compareglobal.service.loans.personal.service.id;

import com.compareglobal.service.common.loans.personal.domain.LoanRange;
import com.compareglobal.service.common.utils.CompareHelperID;
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

@Service("loanServiceID")
public class LoanServiceID implements LoanService {

   private final PersonalLoanHelper loanHelper;

   @Inject
   public LoanServiceID(@Named("personalLoanHelperID") PersonalLoanHelper loanHelper) {
        this.loanHelper = loanHelper;
   }

   @Override
   public List<PersonalLoanPublic> convert(List<PersonalLoan> personalLoans, Compare compare) {
        List<PersonalLoanPublic> loansList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(personalLoans)) {
            double loanAmount = Double.valueOf(compare.getLoanAmount());
            int tenure = Integer.valueOf(compare.getLoanTenure());
            for (PersonalLoan personalLoan : personalLoans) {
                boolean displayRecord = CompareHelperID.recordIncluded(compare.getFilter(), personalLoan);
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
