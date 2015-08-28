package com.compareglobal.service.loans.personal.transform;

import com.compareglobal.service.loans.personal.domain.Compare;
import com.compareglobal.service.loans.personal.domain.PersonalLoan;
import com.compareglobal.service.loans.personal.domain.PersonalLoanTemplate;
import com.compareglobal.service.loans.personal.domain.benefits.Benefit;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dennis on 8/27/15.
 */
public class PersonalLoanTransformer {

    public List<PersonalLoanTemplate> filteredList(List<PersonalLoan> personalLoans, Compare compare) {
        List<PersonalLoanTemplate>  filteredList = new ArrayList<>();
        for (PersonalLoan personalLoan : personalLoans) {
            filteredList.add(sortedValue(personalLoan));
        }
        return filteredList;
    }

    public PersonalLoanTemplate sortedValue(PersonalLoan personalLoan) {
        PersonalLoanTemplate loanTemplate = new PersonalLoanTemplate(personalLoan);

        loanTemplate.setSortedBenefits(personalLoan.getBenefitsList().stream()
                               .sorted(Comparator.comparing(Benefit::getTypeValue))
                               .collect(Collectors.toList()));
        loanTemplate.setRanking(personalLoan.getRankings().stream()
                               .map(ranking -> Pair.of(ranking.getTypeValue(), ranking.getRankValue()))
                               .collect(Collectors.toList()));

        return loanTemplate;
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
