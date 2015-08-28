package com.compareglobal.service.loans.personal.domain;

import com.compareglobal.service.loans.personal.domain.benefits.Benefit;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * Created by dennis on 8/28/15.
 */
public class PersonalLoanTemplate {
    private final PersonalLoan personalLoan;
    private List<Benefit> sortedBenefits;
    private List<Pair<String, String>> ranking;
    private Mortgage loan;

    public PersonalLoanTemplate(PersonalLoan personalLoan) {
        this.personalLoan = personalLoan;
    }

    public void setSortedBenefits(List<Benefit> sortedBenefits) {
        this.sortedBenefits = sortedBenefits;
    }

    public List<Benefit> getSortedBenefits() {
        return sortedBenefits;
    }

    public void setRanking(List<Pair<String, String>> ranking) {
        this.ranking = ranking;
    }

    public List<Pair<String, String>> getRanking() {
        return ranking;
    }

    public PersonalLoan getPersonalLoan() {
        return personalLoan;
    }

    public void setLoan(Mortgage loan) {
        this.loan = loan;
    }

    public Mortgage getLoan() {
        return loan;
    }
}
