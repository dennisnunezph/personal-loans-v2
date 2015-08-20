package com.compareglobal.service.loans.personal.domain.xx;

import com.compareglobal.service.loans.personal.domain.interestrates.Interest;
import com.compareglobal.service.loans.personal.domain.pt.InterestRateInfo;
import com.compareglobal.service.loans.personal.domain.pt.InterestRateType;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by dennis on 3/3/15.
 */
public class RateTypeConverterXX {

    private static final String ANNUAL_TEXT_BASIS = "yApr";
    private static final String BASIS = "basis";
    private static final String FIR_UP_TO = "fir_up_to_";
    private static final String YEAR = "year";
    private static final String FIR_ = "fir_";

    private RateTypeConverterXX(){}

    public static List<InterestRateInfoXX> convert(Set<Interest> interestList) {
        List<InterestRateInfoXX> rateInfoList = new ArrayList<>();
        for (Interest interest : interestList) {
            InterestRateInfoXX rateInfo = toRateInfo(interest);
            if (rateInfo != null
                    && rateInfo.getInterestRateType() != null
                    && rateInfo.getAmount() > 0) {
                rateInfoList.add(rateInfo);
            }
        }
        return rateInfoList;
    }

    protected static InterestRateInfoXX toRateInfo(Interest interest) {
        InterestRateInfoXX aprInfo = new InterestRateInfoXX();

        String aprType = interest.getTypeValue();
        InterestRateTypeXX interestRateType = getRateType(aprType);
        if (interestRateType == null) {
            return null;
        }
        aprInfo.setInterestRateType(interestRateType);

        int period = 0;
        long amountLong = 0;
        String[] aprInfoData;
        switch (interestRateType) {
            case Annual:
                aprInfoData = aprType.split(ANNUAL_TEXT_BASIS);
                String periodData = cleanUpNumbers(aprInfoData.length > 0 ? String.valueOf(aprInfoData[0]): "");
                period = aprInfoData.length > 0 ? Integer.valueOf(periodData) : 0;
                amountLong = aprInfoData.length > 1 ? Long.valueOf(aprInfoData[1]) : 0;
                break;
            case FixedRange:
                aprInfoData = aprType.split(FIR_UP_TO);
                if (aprInfoData.length > 1) {
                    amountLong = Long.valueOf(aprInfoData[1]);
                }
                break;
            default:
        }
        aprInfo.setPeriod(period);
        aprInfo.setAmount(amountLong);
        aprInfo.setRate(interest.getAmount());
        return aprInfo;
    }

    private static InterestRateTypeXX getRateType(String aprType) {
        InterestRateTypeXX interestRateType = null;

        if (aprType.contains(ANNUAL_TEXT_BASIS)
                && !aprType.contains(BASIS)) {
            interestRateType = InterestRateTypeXX.Annual;
        } else if (aprType.startsWith(FIR_UP_TO)
                && !aprType.contains(YEAR)) {
            interestRateType = InterestRateTypeXX.FixedRange;
        } else if (aprType.startsWith(FIR_)) {
            interestRateType = InterestRateTypeXX.Fixed;
        }
        return interestRateType;
    }

    private static String cleanUpNumbers(String rawData) {
        if (StringUtils.isBlank(rawData)) {
            return "";
        }
        return rawData.replace("0_", "");
    }
}
