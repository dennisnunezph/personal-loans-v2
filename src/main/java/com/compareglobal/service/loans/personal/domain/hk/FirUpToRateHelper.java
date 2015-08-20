package com.compareglobal.service.loans.personal.domain.hk;

import com.compareglobal.service.common.utils.ListHelper;
import com.google.common.base.Predicate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;

import java.util.*;

/**
 * Created by dennis on 6/11/15.
 */
public class FirUpToRateHelper {

    private static final int ONE_YEAR_SIX_MONTHS = 18;
    private static final int ONE_YEAR_SIX_MONTHS_CUSTOM = 15;

    public double getTargetInterestForFIRUpTo(double loanAmount,
                                              int loanTenure,
                                              List<InterestRateInfo> interestRates) {
        if (CollectionUtils.isNotEmpty(interestRates)) {
            List<InterestRateInfo> rateInfoList = new ArrayList<>(ListHelper.filterList(getSortedFIRUpToInterest(interestRates),
                    getFIRUpToFilter()));
            int tenureYears = convertTenureToYears(loanTenure);
            if (CollectionUtils.isNotEmpty(rateInfoList)) {
                InterestRateInfo interestRateInfo = ListHelper.findItemFromListByKey(rateInfoList,
                        getTargetTenureFilter(tenureYears, loanAmount));
                if (interestRateInfo != null) {
                    return interestRateInfo.getRate().doubleValue();
                }
            }
        }
        return 0;
    }

    private int convertTenureToYears(int loanTenure) {
        if (loanTenure == 0) {
            return 0;
        }

        if (ONE_YEAR_SIX_MONTHS == loanTenure) {
            return ONE_YEAR_SIX_MONTHS_CUSTOM;
        }

        return loanTenure >= 12 ? loanTenure / 12 : loanTenure;
    }

    private Predicate<InterestRateInfo> getFIRUpToFilter() {
        Predicate<InterestRateInfo> promotionsPredicateFir = new Predicate<InterestRateInfo>() {
            @Override
            public boolean apply(InterestRateInfo input) {
                return  input.getInterestRateType() != null
                        && input.getInterestRateType().equals(InterestRateType.FIRRange);
            }
        };

        return promotionsPredicateFir;
    }

    private List<InterestRateInfo> getSortedFIRUpToInterest(List<InterestRateInfo> interestList) {
        Collections.sort(interestList, new Comparator<InterestRateInfo>() {
            @Override
            public int compare(InterestRateInfo interest1, InterestRateInfo interest2) {
                return new CompareToBuilder()
                        .append(interest1.getPeriod(), interest2.getPeriod())
                        .append(interest1.getAmount(), interest2.getAmount()).toComparison();
            }
        });

        return interestList;

    }

    private Predicate<InterestRateInfo> getTargetTenureFilter(final int tenure, final double loanAmount) {
        return new Predicate<InterestRateInfo>() {
            @Override
            public boolean apply(InterestRateInfo input) {
                return (input.getPeriod() >= tenure)
                        && input.getAmount() >= loanAmount
                        && input.getInterestRateType().equals(InterestRateType.FIRRange);
            }
        };
    }
}
