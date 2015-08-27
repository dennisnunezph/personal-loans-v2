package com.compareglobal.service.loans.personal.transform;

import com.compareglobal.service.loans.personal.domain.Compare;
import com.compareglobal.service.loans.personal.domain.PersonalLoan;
import com.compareglobal.service.loans.personal.domain.PersonalLoanPublic;
import com.compareglobal.service.loans.personal.domain.benefits.Benefit;
import static org.boon.sort.Sorting.sort;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dennis on 8/27/15.
 */
public class PersonalLoanTransformer {

    private static final String TYPE_VALUE = "typeValue";
    private static final String TYPE_KEY = "typeKey";

    public List<PersonalLoanPublic> filteredList(List<PersonalLoan> personalLoans, Compare compare) {
        List<PersonalLoanPublic>  filteredList = new ArrayList<>();
        for (PersonalLoan personalLoan : personalLoans) {
            filteredList.add(sortedValue(personalLoan));
        }
        return filteredList;
    }

    public PersonalLoanPublic sortedValue(PersonalLoan personalLoan) {
        PersonalLoanPublic personalLoanPublic = new PersonalLoanPublic(personalLoan);
        List<Benefit> sortedBenefits = (List<Benefit>) sort(Benefit.class, personalLoan.getBenefitsList(), TYPE_VALUE);
        personalLoanPublic.setSortedBenefits(sortedBenefits);
        return personalLoanPublic;
    }

    /**
     * public CreditCardPublic sortedValue(CreditCard creditCard) {
     CreditCardPublic cardPublic = new CreditCardPublic(creditCard);

     List<Benefit> sortedBenefits = (List<Benefit>) sort(Benefit.class, creditCard.getBenefits(), TYPE_KEY);
     cardPublic.setSortedBenefits(sortedBenefits);

     return cardPublic;
     }
     */
}
