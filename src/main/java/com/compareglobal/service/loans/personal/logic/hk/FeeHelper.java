package com.compareglobal.service.loans.personal.logic.hk;

import com.compareglobal.service.loans.personal.domain.fees.Fee;
import com.compareglobal.service.loans.personal.domain.fees.Types;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nova-pc on 12/2/14.
 */
public class FeeHelper {

    private static final Logger log = Logger.getLogger(FeeHelper.class);

    public enum MortgageKeys {
        AnnualHandlingFee("annualHandlingFee"),
        MinHandlingFee("minHandlingFee");

        private final String mortgageKey;

        MortgageKeys(String mortgageKey) {
            this.mortgageKey = mortgageKey;
        }

        public  String getMortgageKey() {
            return mortgageKey;
        }

        public static MortgageKeys findByKey(String rateKey) {
            for (MortgageKeys key : values()) {
                if (key.getMortgageKey().equalsIgnoreCase(rateKey)) {
                    return  key;
                }
            }
            return  null;
        }
    }

    List<Fee> feeList = new ArrayList<Fee>();
    double feeAmount = 0;

    public FeeHelper(List<Fee> feeList) {
        this.feeList = feeList;
    }

    public double getFee(MortgageKeys mortgageKeys) {
        try {
            for (Fee fee : feeList) {
                if (fee != null
                        && mortgageKeys == MortgageKeys.findByKey(fee.getTypeValue()) ) {
                    feeAmount = fee.getAmount().doubleValue();
                    break;
                }
                else {
                    feeAmount = 0;
                }
            }
        } catch (Exception ex) {
            log.error("Error getFee(): " + ex.getMessage(),ex);
        }

        return feeAmount;
    }

    public double getAnnualHandlingFee() {
        return getFee(MortgageKeys.AnnualHandlingFee);
    }
    public double getMinHandlingFee() {
        return getFee(MortgageKeys.MinHandlingFee);
    }
}
