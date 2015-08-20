package com.compareglobal.service.loans.personal.domain.pt;

/*
 * Copyright (c) 2015.
 * Compare Asia Group
 */

import com.compareglobal.service.loans.personal.domain.Mortgage;
import com.compareglobal.service.loans.personal.domain.pt.InterestRateInfo;
import com.compareglobal.service.loans.personal.domain.pt.InterestRateType;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by dennis on 3/3/15.
 */
public class InterestRateHelper {

    public void updateMortgageInterest(Mortgage mortgage, List<InterestRateInfo> interestRates) {

        BigDecimal monIntRate = BigDecimal.valueOf(mortgage.getMonthlyInterestRate());
        double monthlyInterest = 0;

        int profileNo = (mortgage.getInterestProfile() == null) ? 0 : mortgage.getInterestProfile();
        if (0 == profileNo) {
            monthlyInterest = (monIntRate != null) ? monIntRate.doubleValue() : 0 ;
        } else if (2 == profileNo) {
            monthlyInterest = getInterestFromFIRYear(mortgage, interestRates);
        } else if (11 == profileNo) {
            List<InterestRateInfo> interestClone =  getInterestListClone(interestRates);
            monthlyInterest = getFixedInterestByPeriodAndTenure(mortgage, interestClone);
            if (0 == monthlyInterest) {
                monthlyInterest = getRateFromFIRByAmount(mortgage, InterestRateType.FixedRange, interestRates).doubleValue();
            }
        } else if (20 == profileNo
                || 21 == profileNo
                || 22 == profileNo) {
            monthlyInterest = getRateFromFIRByAmount(mortgage,
                    InterestRateType.FixedRange,
                    interestRates).doubleValue();
            if (0 == monthlyInterest) {
                monthlyInterest = (monIntRate != null) ? monIntRate.doubleValue() : 0 ;
            }
        } else {
            monthlyInterest = getRateFromFIRByAmount(mortgage,
                    InterestRateType.FixedRange,
                    interestRates).doubleValue();
        }
        mortgage.setMonthlyInterestRate(monthlyInterest);
    }

    private List<InterestRateInfo> getInterestListClone(List<InterestRateInfo> interestRates) {
        List<InterestRateInfo> interestClone = Collections.emptyList();
        if (CollectionUtils.isNotEmpty(interestRates)) {
            interestClone = new ArrayList<>(interestRates);
        }
        return interestClone;
    }

    public double getRateAPR(Mortgage mortgage,
                             List<InterestRateInfo> interestList,
                             double lowestAPR) {
        double rateFir = getRateFIR(interestList, mortgage.getPrincipalLoanAmount(), mortgage.getTenureInYears());
        if (rateFir == 0) {
            return lowestAPR;
        }
        return  rateFir;
    }


    private double getInterestFromFIRYear(Mortgage mortgage, List<InterestRateInfo> interests) {
        double interestRate = 0;
        if (CollectionUtils.isNotEmpty(interests)) {
            List<InterestRateInfo> filteredByPeriodAndFixedYearType = getSortedInterest(filteredByPeriodAndType(interests,
                    mortgage.getTenureInYears(), InterestRateType.FixedRange));
            Integer startYearRange = 0;
            Integer mortgageYear = mortgage.getTenureInYears();
            for (InterestRateInfo interest : filteredByPeriodAndFixedYearType) {
                if (withinYearRange(mortgageYear, interest.getPeriod(), startYearRange)) {
                    interestRate = interest.getRate().doubleValue();
                    break;
                }
                startYearRange = interest.getPeriod();
            }
        }
        return interestRate;
    }

    private List<InterestRateInfo> getSortedInterest(List<InterestRateInfo> interestList) {
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

    private List<InterestRateInfo> filteredByPeriodAndType(List<InterestRateInfo> rawSortedList,
                                                           final int period,
                                                           final InterestRateType type) {
        if (CollectionUtils.isEmpty(rawSortedList)) {
            return Collections.EMPTY_LIST;
        }

        Iterables.removeIf(rawSortedList, new Predicate<InterestRateInfo>() {
            @Override
            public boolean apply(InterestRateInfo input) {
                if (input.getInterestRateType() == null
                        || input.getPeriod() == 0
                        || input.getInterestRateType() == null) {
                    return true;
                }

                return input.getPeriod() != period
                        || input.getInterestRateType() != type;
            }
        });
        return rawSortedList;
    }

    private double getFixedInterestByPeriodAndTenure(Mortgage mortgage,
                                                     List<InterestRateInfo> interests) {
        BigDecimal interestRate = BigDecimal.ZERO;
        BigDecimal mortgageAmount = new BigDecimal(mortgage.getPrincipalLoanAmount());
        BigDecimal startRange = BigDecimal.ZERO;
        if (CollectionUtils.isNotEmpty(interests)) {
            List<InterestRateInfo> filteredByPeriod = getSortedInterest(filteredByPeriodAndType(interests,
                    mortgage.getTenureInYears(),InterestRateType.Fixed));
            for (InterestRateInfo interest : filteredByPeriod) {
                if (withinAmountRange(mortgageAmount, BigDecimal.valueOf(interest.getAmount()), startRange)) {
                    interestRate = interest.getRate();
                    break;
                }
                startRange = BigDecimal.valueOf(interest.getAmount());
            }
        }
        return interestRate.doubleValue();
    }

    private BigDecimal getRateFromFIRByAmount(Mortgage mortgage,
                                              InterestRateType type,
                                              List<InterestRateInfo> interests) {
        BigDecimal startRange = getAmountRange(mortgage, interests, type);
        return  getRateFIRByAmount(interests, startRange, type);
    }

    private BigDecimal getRateFIRByAmount(List<InterestRateInfo> interestSet,
                                          BigDecimal filteredAmount,
                                          InterestRateType type) {
        double rateFIR = 0;

        if (CollectionUtils.isNotEmpty(interestSet)) {
            List<InterestRateInfo> filteredByPeriod = getSortedInterest(filteredByAmountAndType(interestSet,
                    filteredAmount, type));
            BigDecimal startRange = BigDecimal.ZERO;
            for (InterestRateInfo interest :filteredByPeriod) {
                BigDecimal interestAmount = BigDecimal.valueOf(interest.getAmount());
                if (interest.getRate().compareTo(BigDecimal.ZERO) > 0
                        && withinAmountRange(filteredAmount, interestAmount, startRange)) {
                    rateFIR = interest.getRate().doubleValue();
                    break;
                }
                startRange = interestAmount;
            }
        }
        return  new BigDecimal(rateFIR);
    }

    private List<InterestRateInfo> filteredByAmountAndType(List<InterestRateInfo> rawSortedList,
                                                           final BigDecimal mortgageAmount,
                                                           final InterestRateType type) {
        if (CollectionUtils.isEmpty(rawSortedList)) {
            return Collections.EMPTY_LIST;
        }

        Iterables.removeIf(rawSortedList, new Predicate<InterestRateInfo>() {
            @Override
            public boolean apply(InterestRateInfo input) {
                if (input.getInterestRateType() == null
                        || input.getAmount() == 0
                        || input.getInterestRateType() == null) {
                    return true;
                }
                BigDecimal amount = BigDecimal.valueOf(input.getAmount());

                return amount.compareTo(mortgageAmount) != 0
                        || input.getInterestRateType() != type;
            }
        });
        return rawSortedList;
    }

    private boolean withinYearRange(Integer loanTenure, Integer currentYear, Integer startRange) {
        return (currentYear.compareTo(loanTenure) >= 0)
                && currentYear.compareTo(startRange) >= 0;
    }

    private double getRateFIR(List<InterestRateInfo> interestList,
                              double principalAmount,
                              int tenureInYears) {
        double rateFIR = 0;
        BigDecimal mortgageAmount = new BigDecimal(principalAmount);

        if (CollectionUtils.isNotEmpty(interestList)) {
            List<InterestRateInfo> filteredByPeriod = getSortedInterest(filteredByPeriodAndType(interestList,
                    tenureInYears, InterestRateType.Annual));
            BigDecimal startRange = BigDecimal.ZERO;
            for (InterestRateInfo interest :filteredByPeriod) {
                BigDecimal interestAmount = BigDecimal.valueOf(interest.getAmount());
                if (interest.getPeriod() == tenureInYears
                        && withinAmountRange(mortgageAmount, interestAmount, startRange)) {
                    rateFIR = interest.getRate().doubleValue();
                    break;
                }
                startRange = interestAmount;
            }
        }
        return rateFIR;
    }

    private BigDecimal getAmountRange(Mortgage mortgage,
                                      List<InterestRateInfo> interests,
                                      InterestRateType type) {
        BigDecimal mortgageAmount = new BigDecimal(mortgage.getPrincipalLoanAmount());
        BigDecimal startRange = BigDecimal.ZERO;
        if (CollectionUtils.isNotEmpty(interests)) {
            List<InterestRateInfo> filteredByPeriod = getSortedInterest(filteredByType(interests, type));
            for (InterestRateInfo interest : filteredByPeriod) {
                BigDecimal interestAmount = BigDecimal.valueOf(interest.getAmount());
                if (withinAmountRange(mortgageAmount,  interestAmount, startRange)) {
                    startRange = BigDecimal.valueOf(interest.getAmount());
                    break;
                }
                startRange = interestAmount;
            }
        }
        return startRange;
    }

    private List<InterestRateInfo> filteredByType(List<InterestRateInfo> rawSortedList,
                                                  final InterestRateType type) {
        if (CollectionUtils.isEmpty(rawSortedList)) {
            return Collections.EMPTY_LIST;
        }

        Iterables.removeIf(rawSortedList, new Predicate<InterestRateInfo>() {
            @Override
            public boolean apply(InterestRateInfo input) {
                if (input.getInterestRateType() == null
                        || input.getAmount() == 0
                        || input.getInterestRateType() == null) {
                    return true;
                }

                if (input.getInterestRateType() != type) {
                    return true;
                }
                return false;
            }
        });
        return rawSortedList;
    }


    private boolean withinAmountRange(BigDecimal loanAmount, BigDecimal currentAmount, BigDecimal startRange) {
        return (currentAmount.compareTo(loanAmount) >= 0)
                && currentAmount.compareTo(startRange) >= 0;
    }


}