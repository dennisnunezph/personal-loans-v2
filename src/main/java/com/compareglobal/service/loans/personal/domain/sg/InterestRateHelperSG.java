package com.compareglobal.service.loans.personal.domain.sg;

import com.compareglobal.service.common.utils.JSONToMap;
import com.compareglobal.service.common.utils.ListHelper;
import com.compareglobal.service.loans.personal.domain.Mortgage;
import com.compareglobal.service.loans.personal.domain.interestrates.Interest;
import com.compareglobal.service.loans.personal.view.sg.MortgageSG;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by dennis on 5/12/15.
 */
public class InterestRateHelperSG {

    private static final String FIR_UP_TO = "fir_up_to_";
    private static final String YEAR = "year";
    private static final String LOWEST_APR = "lowestAPR";

    public void updateMortgageInterest(Mortgage mortgage,
                                       Set<Interest> interestRates) {
        MortgageSG mortgageSG = (MortgageSG) mortgage;
        double annualInterestRate = 0;
        APRParam aprParam = new APRParam(mortgageSG);
        aprParam.setProfile(mortgageSG.getCategoryProfile());
        aprParam.setInterestRateList(interestRates);

        switch(mortgageSG.getCategoryProfile()) {
            case PersonalInstalment:
                annualInterestRate = getPersonalInstalmentRate(aprParam);
                break;
            case CreditLine:
                annualInterestRate = getLowestAPRRate(interestRates);
                break;
            case BalanceTransfer:
                annualInterestRate = getLowestAPRRate(interestRates);
                break;
            default:
        }
        mortgageSG.setAnnualInterestRate(annualInterestRate);
    }

    private double getLowestAPRRate(Set<Interest> interests) {
        double interestRate = 0;
        Predicate<Interest> predicate = new Predicate<Interest>() {
            @Override
            public boolean apply(Interest input) {
                return input.getTypeValue().equalsIgnoreCase(LOWEST_APR);
            }
        };
        Interest lowestAPR = ListHelper.findItemByKey(interests, predicate);
        if (lowestAPR != null) {
            interestRate = convertToPercent(lowestAPR.getAmount().doubleValue());
        }
        return interestRate;
    }

    private double convertToPercent(double rate) {
        double interestRate = rate;
        if (interestRate >= 1) {
            interestRate = interestRate / 100;
        }
        return interestRate;
    }

    private double getPersonalInstalmentRate(APRParam aprParam) {
        double interestRate = 0;
        InterestRateInfoSG aprInterest = getTargetAmountFilter(aprParam);
        if (aprInterest != null) {
            interestRate = aprInterest.getRate().doubleValue();
        }
        return interestRate;
    }

    private InterestRateInfoSG getTargetAmountFilter(APRParam aprParam) {
        InterestRateInfoSG rateInfoSG = aprParam.getInterestRateList().stream()
                .filter(interest -> interest.getTypeValue().startsWith(FIR_UP_TO)
                               && interest.getTypeValue().endsWith(YEAR))
                .map(interest -> RateTypeConverterSG.toRateInfo(interest, aprParam.getProfile()))
                .sorted(Comparator.comparing(InterestRateInfoSG::getPeriod)
                        .thenComparing(InterestRateInfoSG::getAmount))
                .filter(rateInfo -> rateInfo.getPeriod() >= aprParam.getTenure()
                        && rateInfo.getAmount() >= aprParam.getLoanAmount())
                .findFirst()
                .orElse(null);
        return rateInfoSG;
    }

    private static class APRParam {
        private InterestProfileSG profile;
        private int tenure;
        private double loanAmount;
        private Set<Interest> interestRateList;

        public APRParam(MortgageSG mortgageSG) {
            this.tenure = mortgageSG.getTenureInMonths();
            this.loanAmount = mortgageSG.getPrincipalLoanAmount();
        }

        public InterestProfileSG getProfile() {
            return profile;
        }

        public void setProfile(InterestProfileSG profile) {
            this.profile = profile;
        }

        public int getTenure() {
            return tenure;
        }

        public void setTenure(int tenure) {
            this.tenure = tenure;
        }

        public double getLoanAmount() {
            return loanAmount;
        }

        public void setLoanAmount(double loanAmount) {
            this.loanAmount = loanAmount;
        }

        public Set<Interest> getInterestRateList() {
            return interestRateList;
        }

        public void setInterestRateList(Set<Interest> interestRateList) {
            this.interestRateList = interestRateList;
        }
    }

}
