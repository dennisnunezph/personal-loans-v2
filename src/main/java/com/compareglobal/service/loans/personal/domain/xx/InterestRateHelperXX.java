package com.compareglobal.service.loans.personal.domain.xx;


import com.compareglobal.service.loans.personal.domain.Mortgage;
import com.compareglobal.service.loans.personal.domain.xx.InterestRateInfoXX;
import com.compareglobal.service.loans.personal.domain.xx.InterestRateTypeXX;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class InterestRateHelperXX {

    public void updateMortgageInterest(Mortgage mortgage, List<InterestRateInfoXX> interestRates) {

        BigDecimal monIntRate = BigDecimal.valueOf(mortgage.getMonthlyInterestRate());
        double monthlyInterest = 0;

        int profileNo = (mortgage.getInterestProfile() == null) ? 0 : mortgage.getInterestProfile();
        if (0 == profileNo) {
            monthlyInterest = (monIntRate != null) ? monIntRate.doubleValue() : 0 ;
        } else if (2 == profileNo) {
            monthlyInterest = getInterestFromFIRYear(mortgage, interestRates);
        } else if (11 == profileNo) {
            List<InterestRateInfoXX> interestClone =  getInterestListClone(interestRates);
            monthlyInterest = getFixedInterestByPeriodAndTenure(mortgage, interestClone);
            if (0 == monthlyInterest) {
                monthlyInterest = getRateFromFIRByAmount(mortgage, InterestRateTypeXX.FixedRange, interestRates).doubleValue();
            }
        } else if (20 == profileNo
                || 21 == profileNo
                || 22 == profileNo) {
            monthlyInterest = getRateFromFIRByAmount(mortgage,
                    InterestRateTypeXX.FixedRange,
                    interestRates).doubleValue();
            if (0 == monthlyInterest) {
                monthlyInterest = (monIntRate != null) ? monIntRate.doubleValue() : 0 ;
            }
        } else {
            monthlyInterest = getRateFromFIRByAmount(mortgage,
                    InterestRateTypeXX.FixedRange,
                    interestRates).doubleValue();
        }
        mortgage.setMonthlyInterestRate(monthlyInterest);
    }

    private List<InterestRateInfoXX> getInterestListClone(List<InterestRateInfoXX> interestRates) {
        List<InterestRateInfoXX> interestClone = Collections.emptyList();
        if (CollectionUtils.isNotEmpty(interestRates)) {
            interestClone = new ArrayList<>(interestRates);
        }
        return interestClone;
    }

    public double getRateAPR(Mortgage mortgage,
                             List<InterestRateInfoXX> interestList,
                             double lowestAPR) {
        double rateFir = getRateFIR(interestList, mortgage.getPrincipalLoanAmount(), mortgage.getTenureInYears());
        if (rateFir == 0) {
            return lowestAPR;
        }
        return  rateFir;
    }


    private double getInterestFromFIRYear(Mortgage mortgage, List<InterestRateInfoXX> interests) {
        double interestRate = 0;
        if (CollectionUtils.isNotEmpty(interests)) {
            List<InterestRateInfoXX> filteredByPeriodAndFixedYearType = getSortedInterest(filteredByPeriodAndType(interests,
                    mortgage.getTenureInYears(), InterestRateTypeXX.FixedRange));
            Integer startYearRange = 0;
            Integer mortgageYear = mortgage.getTenureInYears();
            for (InterestRateInfoXX interest : filteredByPeriodAndFixedYearType) {
                if (withinYearRange(mortgageYear, interest.getPeriod(), startYearRange)) {
                    interestRate = interest.getRate().doubleValue();
                    break;
                }
                startYearRange = interest.getPeriod();
            }
        }
        return interestRate;
    }

    private List<InterestRateInfoXX> getSortedInterest(List<InterestRateInfoXX> interestList) {
        Collections.sort(interestList, new Comparator<InterestRateInfoXX>() {
            @Override
            public int compare(InterestRateInfoXX interest1, InterestRateInfoXX interest2) {
                return new CompareToBuilder()
                        .append(interest1.getPeriod(), interest2.getPeriod())
                        .append(interest1.getAmount(), interest2.getAmount()).toComparison();
            }
        });

        return interestList;

    }

    private List<InterestRateInfoXX> filteredByPeriodAndType(List<InterestRateInfoXX> rawSortedList,
                                                           final int period,
                                                           final InterestRateTypeXX type) {
        if (CollectionUtils.isEmpty(rawSortedList)) {
            return Collections.EMPTY_LIST;
        }

        Iterables.removeIf(rawSortedList, new Predicate<InterestRateInfoXX>() {
            @Override
            public boolean apply(InterestRateInfoXX input) {
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
                                                     List<InterestRateInfoXX> interests) {
        BigDecimal interestRate = BigDecimal.ZERO;
        BigDecimal mortgageAmount = new BigDecimal(mortgage.getPrincipalLoanAmount());
        BigDecimal startRange = BigDecimal.ZERO;
        if (CollectionUtils.isNotEmpty(interests)) {
            List<InterestRateInfoXX> filteredByPeriod = getSortedInterest(filteredByPeriodAndType(interests,
                    mortgage.getTenureInYears(),InterestRateTypeXX.Fixed));
            for (InterestRateInfoXX interest : filteredByPeriod) {
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
                                              InterestRateTypeXX type,
                                              List<InterestRateInfoXX> interests) {
        BigDecimal startRange = getAmountRange(mortgage, interests, type);
        return  getRateFIRByAmount(interests, startRange, type);
    }

    private BigDecimal getRateFIRByAmount(List<InterestRateInfoXX> interestSet,
                                          BigDecimal filteredAmount,
                                          InterestRateTypeXX type) {
        double rateFIR = 0;

        if (CollectionUtils.isNotEmpty(interestSet)) {
            List<InterestRateInfoXX> filteredByPeriod = getSortedInterest(filteredByAmountAndType(interestSet,
                    filteredAmount, type));
            BigDecimal startRange = BigDecimal.ZERO;
            for (InterestRateInfoXX interest :filteredByPeriod) {
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

    private List<InterestRateInfoXX> filteredByAmountAndType(List<InterestRateInfoXX> rawSortedList,
                                                           final BigDecimal mortgageAmount,
                                                           final InterestRateTypeXX type) {
        if (CollectionUtils.isEmpty(rawSortedList)) {
            return Collections.EMPTY_LIST;
        }

        Iterables.removeIf(rawSortedList, new Predicate<InterestRateInfoXX>() {
            @Override
            public boolean apply(InterestRateInfoXX input) {
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

    private double getRateFIR(List<InterestRateInfoXX> interestList,
                              double principalAmount,
                              int tenureInYears) {
        double rateFIR = 0;
        BigDecimal mortgageAmount = new BigDecimal(principalAmount);

        if (CollectionUtils.isNotEmpty(interestList)) {
            List<InterestRateInfoXX> filteredByPeriod = getSortedInterest(filteredByPeriodAndType(interestList,
                    tenureInYears, InterestRateTypeXX.Annual));
            BigDecimal startRange = BigDecimal.ZERO;
            for (InterestRateInfoXX interest :filteredByPeriod) {
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
                                      List<InterestRateInfoXX> interests,
                                      InterestRateTypeXX type) {
        BigDecimal mortgageAmount = new BigDecimal(mortgage.getPrincipalLoanAmount());
        BigDecimal startRange = BigDecimal.ZERO;
        if (CollectionUtils.isNotEmpty(interests)) {
            List<InterestRateInfoXX> filteredByPeriod = getSortedInterest(filteredByType(interests, type));
            for (InterestRateInfoXX interest : filteredByPeriod) {
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

    private List<InterestRateInfoXX> filteredByType(List<InterestRateInfoXX> rawSortedList,
                                                  final InterestRateTypeXX type) {
        if (CollectionUtils.isEmpty(rawSortedList)) {
            return Collections.EMPTY_LIST;
        }

        Iterables.removeIf(rawSortedList, new Predicate<InterestRateInfoXX>() {
            @Override
            public boolean apply(InterestRateInfoXX input) {
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
