/*
 * Copyright (c) 2015.
 * Compare Asia Group
 */
package com.compareglobal.service.loans.personal.domain.id;

import com.compareglobal.service.loans.personal.domain.Mortgage;
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
public class InterestRateHelperID {

    public void updateMortgageInterest(Mortgage mortgage, List<InterestRateInfoID> interestRates) {

        BigDecimal monIntRate = BigDecimal.valueOf(mortgage.getMonthlyInterestRate());
        double monthlyInterest = 0;

        int profileNo = (mortgage.getInterestProfile() == null) ? 0 : mortgage.getInterestProfile();
        if (0 == profileNo) {
            monthlyInterest = (monIntRate != null) ? monIntRate.doubleValue() : 0 ;
        } else if (2 == profileNo) {
            monthlyInterest = getInterestFromFIRYear(mortgage, interestRates);
        } else if (11 == profileNo) {
            List<InterestRateInfoID> interestClone =  getInterestListClone(interestRates);
            monthlyInterest = getFixedInterestByPeriodAndTenure(mortgage, interestClone);
            if (0 == monthlyInterest) {
                monthlyInterest = getRateFromFIRByAmount(mortgage, InterestRateTypeID.FixedRange, interestRates).doubleValue();
            }
        } else if (20 == profileNo
                || 21 == profileNo
                || 22 == profileNo) {
            monthlyInterest = getRateFromFIRByAmount(mortgage,
                    InterestRateTypeID.FixedRange,
                    interestRates).doubleValue();
            if (0 == monthlyInterest) {
                monthlyInterest = (monIntRate != null) ? monIntRate.doubleValue() : 0 ;
            }
        } else {
            monthlyInterest = getRateFromFIRByAmount(mortgage,
                    InterestRateTypeID.FixedRange,
                    interestRates).doubleValue();
        }
        mortgage.setMonthlyInterestRate(monthlyInterest);
    }

    private List<InterestRateInfoID> getInterestListClone(List<InterestRateInfoID> interestRates) {
        List<InterestRateInfoID> interestClone = Collections.emptyList();
        if (CollectionUtils.isNotEmpty(interestRates)) {
            interestClone = new ArrayList<>(interestRates);
        }
        return interestClone;
    }

    public double getRateAPR(Mortgage mortgage,
                             List<InterestRateInfoID> interestList,
                             double lowestAPR) {
        double rateFir = getRateFIR(interestList, mortgage.getPrincipalLoanAmount(), mortgage.getTenureInYears());
        if (rateFir == 0) {
            return lowestAPR;
        }
        return  rateFir;
    }


    private double getInterestFromFIRYear(Mortgage mortgage, List<InterestRateInfoID> interests) {
        double interestRate = 0;
        if (CollectionUtils.isNotEmpty(interests)) {
            List<InterestRateInfoID> filteredByPeriodAndFixedYearType = getSortedInterest(filteredByPeriodAndType(interests,
                    mortgage.getTenureInYears(), InterestRateTypeID.FixedRange));
            Integer startYearRange = 0;
            Integer mortgageYear = mortgage.getTenureInYears();
            for (InterestRateInfoID interest : filteredByPeriodAndFixedYearType) {
                if (withinYearRange(mortgageYear, interest.getPeriod(), startYearRange)) {
                    interestRate = interest.getRate().doubleValue();
                    break;
                }
                startYearRange = interest.getPeriod();
            }
        }
        return interestRate;
    }

    private List<InterestRateInfoID> getSortedInterest(List<InterestRateInfoID> interestList) {
        Collections.sort(interestList, new Comparator<InterestRateInfoID>() {
            @Override
            public int compare(InterestRateInfoID interest1, InterestRateInfoID interest2) {
                return new CompareToBuilder()
                        .append(interest1.getPeriod(), interest2.getPeriod())
                        .append(interest1.getAmount(), interest2.getAmount()).toComparison();
            }
        });

        return interestList;

    }

    private List<InterestRateInfoID> filteredByPeriodAndType(List<InterestRateInfoID> rawSortedList,
                                                   final int period,
                                                   final InterestRateTypeID type) {
        if (CollectionUtils.isEmpty(rawSortedList)) {
            return Collections.EMPTY_LIST;
        }

        Iterables.removeIf(rawSortedList, new Predicate<InterestRateInfoID>() {
            @Override
            public boolean apply(InterestRateInfoID input) {
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
                                                     List<InterestRateInfoID> interests) {
        BigDecimal interestRate = BigDecimal.ZERO;
        BigDecimal mortgageAmount = new BigDecimal(mortgage.getPrincipalLoanAmount());
        BigDecimal startRange = BigDecimal.ZERO;
        if (CollectionUtils.isNotEmpty(interests)) {
            List<InterestRateInfoID> filteredByPeriod = getSortedInterest(filteredByPeriodAndType(interests,
                    mortgage.getTenureInYears(),InterestRateTypeID.Fixed));
            for (InterestRateInfoID interest : filteredByPeriod) {
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
                                              InterestRateTypeID type,
                                              List<InterestRateInfoID> interests) {
        BigDecimal startRange = getAmountRange(mortgage, interests, type);
        return  getRateFIRByAmount(interests, startRange, type);
    }

    private BigDecimal getRateFIRByAmount(List<InterestRateInfoID> interestSet,
                                          BigDecimal filteredAmount,
                                          InterestRateTypeID type) {
        double rateFIR = 0;

        if (CollectionUtils.isNotEmpty(interestSet)) {
            List<InterestRateInfoID> filteredByPeriod = getSortedInterest(filteredByAmountAndType(interestSet,
                    filteredAmount, type));
            BigDecimal startRange = BigDecimal.ZERO;
            for (InterestRateInfoID interest :filteredByPeriod) {
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

    private List<InterestRateInfoID> filteredByAmountAndType(List<InterestRateInfoID> rawSortedList,
                                                   final BigDecimal mortgageAmount,
                                                   final InterestRateTypeID type) {
        if (CollectionUtils.isEmpty(rawSortedList)) {
            return Collections.EMPTY_LIST;
        }

        Iterables.removeIf(rawSortedList, new Predicate<InterestRateInfoID>() {
            @Override
            public boolean apply(InterestRateInfoID input) {
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

    private double getRateFIR(List<InterestRateInfoID> interestList,
                              double principalAmount,
                              int tenureInYears) {
        double rateFIR = 0;
        BigDecimal mortgageAmount = new BigDecimal(principalAmount);

        if (CollectionUtils.isNotEmpty(interestList)) {
            List<InterestRateInfoID> filteredByPeriod = getSortedInterest(filteredByPeriodAndType(interestList,
                    tenureInYears, InterestRateTypeID.Annual));
            BigDecimal startRange = BigDecimal.ZERO;
            for (InterestRateInfoID interest :filteredByPeriod) {
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
                                      List<InterestRateInfoID> interests,
                                      InterestRateTypeID type) {
        BigDecimal mortgageAmount = new BigDecimal(mortgage.getPrincipalLoanAmount());
        BigDecimal startRange = BigDecimal.ZERO;
        if (CollectionUtils.isNotEmpty(interests)) {
            List<InterestRateInfoID> filteredByPeriod = getSortedInterest(filteredByType(interests, type));
            for (InterestRateInfoID interest : filteredByPeriod) {
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

    private List<InterestRateInfoID> filteredByType(List<InterestRateInfoID> rawSortedList,
                                          final InterestRateTypeID type) {
        if (CollectionUtils.isEmpty(rawSortedList)) {
            return Collections.EMPTY_LIST;
        }

        Iterables.removeIf(rawSortedList, new Predicate<InterestRateInfoID>() {
            @Override
            public boolean apply(InterestRateInfoID input) {
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
