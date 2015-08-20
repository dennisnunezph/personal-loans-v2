package com.compareglobal.service.loans.personal.domain.sg;

import com.compareglobal.service.loans.personal.domain.hk.InterestRateType;
import com.compareglobal.service.loans.personal.domain.interestrates.Interest;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dennis on 5/12/15.
 */
public class RateTypeConverterSG {

    private static final String ANNUAL_TEXT_BASIS = "yApr";
    private static final String BASIS = "basis";
    private static final String FIR_UP_TO = "fir_up_to_";
    private static final String YEAR = "year";
    private static final String FIR_ = "fir_";

    public static List<InterestRateInfoSG> convertToRateList(List<Interest> interestList, InterestProfileSG profile) {
        List<InterestRateInfoSG> rateInfoList = new ArrayList<>();
        for (Interest interest : interestList) {
            InterestRateInfoSG rateInfo = toRateInfo(interest, profile);
            if (rateInfo != null) {
                rateInfoList.add(rateInfo);
            }
        }
        return rateInfoList;
    }


    protected static InterestRateInfoSG toRateInfo(Interest interest, InterestProfileSG profile) {
        InterestRateInfoSG aprInfo = new InterestRateInfoSG();

        String aprType = interest.getTypeValue();
        InterestRateType interestRateType = getRateType(aprType);
        if (interestRateType == null) {
            return null;
        }

        int period = 0;
        double interestAmount = 0;
        String[] aprInfoData;
        String amountString = "";
        String tenureData = "";
        switch (profile) {
            case PersonalInstalment:
                aprInfoData = aprType.split(FIR_UP_TO);
                if (aprInfoData.length > 1) {
                    String[] yearData = aprInfoData[1].split(YEAR);
                    if (yearData.length > 0
                            && yearData[0].contains("_")) {
                        yearData = yearData[0].split("_");
                    }
                    if (yearData.length > 0) {
                        String[] extraInterestInfo = yearData[0].split("_");
                        amountString = extraInterestInfo[0];

                        if (yearData.length > 1) {
                            extraInterestInfo = yearData[0].split("_");
                            amountString = extraInterestInfo[0];
                            tenureData = yearData[1];
                            period = StringUtils.isNotBlank(tenureData)
                                    && StringUtils.isNumeric(tenureData) ? Integer.valueOf(tenureData) : 0;
                            period = period * 12;
                        }
                        interestAmount = NumberUtils.toDouble(amountString);
                        if (yearData.length > 2) {
                            if ("0".equals(yearData[1])) {
                               String monthPart = yearData[2];
                               if ("25".equals(monthPart)) {
                                   period = 3;
                               } else if ("5".equals(monthPart)) {
                                   period = 6;
                               }
                            } else {
                               String periodString = yearData[1];
                               period = StringUtils.isNotBlank(periodString)
                                        && StringUtils.isNumeric(periodString) ? Integer.valueOf(periodString) : 0;
                               period = period * 12;
                            }
                        }
                    }
                }
                interestAmount =  interestAmount > 0 ? interestAmount : NumberUtils.toDouble(interest.getDescription());
                break;
            default:
        }
        aprInfo.setPeriod(period);
        aprInfo.setAmount(interestAmount);
        BigDecimal interestRate = interest.getAmount().compareTo(BigDecimal.ZERO) > 0 ?
                interest.getAmount() : BigDecimal.valueOf(NumberUtils.toDouble(interest.getDescription()));
        aprInfo.setRate(interestRate);
        aprInfo.setRecordKey(aprType);
        return aprInfo;
    }

    private static InterestRateType getRateType(String aprType) {
        InterestRateType interestRateType = null;

        if (aprType.contains(ANNUAL_TEXT_BASIS)
                && !aprType.contains(BASIS)) {
            interestRateType = InterestRateType.Annual;
        } else if (aprType.startsWith(FIR_UP_TO)
                && !aprType.contains(YEAR)) {
            interestRateType = InterestRateType.FixedRange;
        } else if (aprType.startsWith(FIR_)) {
            interestRateType = InterestRateType.Fixed;
        }
        return interestRateType;
    }
}
