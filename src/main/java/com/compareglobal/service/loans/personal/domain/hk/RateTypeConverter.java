package com.compareglobal.service.loans.personal.domain.hk;

import com.compareglobal.service.loans.personal.domain.interestrates.Interest;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by dennis on 3/3/15.
 */
public class RateTypeConverter {

    private static final String ANNUAL_TEXT_BASIS = "yApr";
    private static final String BASIS = "basis";
    private static final String FIR_UP_TO = "fir_up_to_";
    private static final String YEAR = "year";
    private static final String FIR_ = "fir_";

    private RateTypeConverter(){}

    public static List<InterestRateInfo> convert(Set<Interest> interestList) {
        List<InterestRateInfo> rateInfoList = new ArrayList<>();
        for (Interest interest : interestList) {
            InterestRateInfo rateInfo = toRateInfo(interest);
            if (rateInfo != null
                    && rateInfo.getInterestRateType() != null) {
                if (rateInfo.getInterestRateType().equals(InterestRateType.Basis)
                        && StringUtils.isNotBlank(rateInfo.getBasis())) {
                    rateInfoList.add(rateInfo);
                } else if (rateInfo.getAmount() > 0) {
                    rateInfoList.add(rateInfo);
                }
            }
        }
        return rateInfoList;
    }

    protected static InterestRateInfo toRateInfo(Interest interest) {
        InterestRateInfo aprInfo = new InterestRateInfo();

        String aprType = interest.getTypeValue();
        InterestRateType interestRateType = getRateType(aprType);
        if (interestRateType == null) {
            return null;
        }
        aprInfo.setInterestRateType(interestRateType);

        int period = 0;
        long amountLong = 0;
        BigDecimal rate = interest.getAmount();
        String[] aprInfoData;
        switch (interestRateType) {
            case Annual:
                aprInfoData = aprType.split(ANNUAL_TEXT_BASIS);
                String periodData = cleanUpNumbers(aprInfoData.length > 0 ? String.valueOf(aprInfoData[0]): "");
                period = aprInfoData.length > 0 ? NumberUtils.toInt(periodData) : 0;
                amountLong = aprInfoData.length > 1 ? NumberUtils.toLong(aprInfoData[1]) : 0;
                break;
            case FixedRange:
                aprInfoData = aprType.split(FIR_UP_TO);
                if (aprInfoData.length > 1) {
                    amountLong = NumberUtils.toLong(aprInfoData[1]);
                }
                break;
            case FIRRange:
                RateInfoParam rateInfo = getRateInfo(aprType);
                if (rateInfo != null) {
                    period = rateInfo.getPeriod();
                    amountLong = rateInfo.getAmount();
                }
                if (rate != null
                    && rate.compareTo(BigDecimal.ZERO) == 0
                    && StringUtils.isNotBlank(interest.getDescription())) {
                    rate = NumberUtils.createBigDecimal(interest.getDescription());
                }
                break;
            case Basis:
                aprInfoData = aprType.split(ANNUAL_TEXT_BASIS);
                period = NumberUtils.toInt(aprInfoData[0]);
                amountLong = NumberUtils.toLong(aprInfoData[1].replace(BASIS, ""));
                aprInfo.setBasis(interest.getDescription());
                break;
            default:
        }
        aprInfo.setPeriod(period);
        aprInfo.setAmount(amountLong);
        aprInfo.setRate(rate);
        return aprInfo;
    }

    private static InterestRateType getRateType(String aprType) {
        InterestRateType interestRateType = null;

        if (aprType.contains(ANNUAL_TEXT_BASIS)) {
            if (aprType.contains(BASIS)) {
                interestRateType = InterestRateType.Basis;
            } else {
                interestRateType = InterestRateType.Annual;
            }
        } else if (aprType.startsWith(FIR_UP_TO)) {
            if (aprType.contains(YEAR)) {
                interestRateType = InterestRateType.FIRRange;
            } else {
                interestRateType = InterestRateType.FixedRange;
            }
        } else if (aprType.startsWith(FIR_)) {
            interestRateType = InterestRateType.Fixed;
        }
        return interestRateType;
    }

    private static RateInfoParam getRateInfo(String aprType) {
        RateInfoParam infoParam = new RateInfoParam();
        if (StringUtils.isBlank(aprType)) {
            return infoParam;
        }

        String[] aprInfoData = aprType.replaceAll(FIR_UP_TO, "").split("_");
        int period = 0;
        if (aprInfoData.length > 0) {
            infoParam.setAmount(getSafeLong(aprInfoData[0]));
            String yearString = "";
            if (aprInfoData.length == 2
                    && StringUtils.isNotBlank(aprInfoData[1])) {
                yearString = aprInfoData[1].replace(YEAR, "");
                period = getSafeInteger(yearString);
            }

            if (aprInfoData.length == 3
                    && StringUtils.isNotBlank(aprInfoData[1])) {
                yearString = aprInfoData[1] + aprInfoData[2].replace(YEAR, "");
                period = getSafeInteger(yearString);
            }

            infoParam.setPeriod(period);
        }
        return infoParam;
    }

    private static String cleanUpNumbers(String rawData) {
        if (StringUtils.isBlank(rawData)) {
            return "";
        }
        return rawData.replace("0_", "");
    }

    private static Long getSafeLong(String longValue) {
        long safeVal;
        try {
            safeVal = Long.valueOf(longValue);
        } catch (Exception ex) {
            safeVal = 0;
        }
        return safeVal;
    }

    private static Integer getSafeInteger(String intValue) {
        int safeVal;
        try {
            safeVal = Integer.valueOf(intValue);
        } catch (Exception ex) {
            safeVal = 0;
        }
        return safeVal;
    }

    private static class RateInfoParam {
        private int period;
        private long amount;

        public int getPeriod() {
            return period;
        }

        public void setPeriod(int period) {
            this.period = period;
        }

        public long getAmount() {
            return amount;
        }

        public void setAmount(long amount) {
            this.amount = amount;
        }

    }
}
