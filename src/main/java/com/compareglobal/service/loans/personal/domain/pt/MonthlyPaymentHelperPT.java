package com.compareglobal.service.loans.personal.domain.pt;

import com.compareglobal.service.common.domain.Filter;
import com.compareglobal.service.common.utils.ListHelper;
import com.compareglobal.service.loans.personal.domain.GeneralInfo;
import com.compareglobal.service.loans.personal.domain.MonthlyPaymentHelper;
import com.compareglobal.service.loans.personal.domain.PersonalLoan;
import com.compareglobal.service.loans.personal.domain.fees.Fee;
import com.compareglobal.service.loans.personal.domain.pt.InterestRateHelper;
import com.compareglobal.service.loans.personal.domain.pt.InterestRateInfo;
import com.compareglobal.service.loans.personal.domain.pt.RateTypeConverter;
import com.compareglobal.service.loans.personal.domain.interestrates.Interest;
import com.compareglobal.service.loans.personal.logic.hk.FeeHelper;
import com.compareglobal.service.loans.personal.logic.pt.PaymentHelperPT;
import com.compareglobal.service.loans.personal.view.pt.MortgagePT;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by dennis on 4/22/15.
 */
@Component("monthlyPaymentHelperPT")
public class MonthlyPaymentHelperPT implements MonthlyPaymentHelper {

    public enum MortgageKeys {
        InitialCommissionMin("initialCommissionMin"),
        InitialCommissionMax("initialCommissionMax"),
        InitialCommissionFixed("initialCommissionIfFixedAmount"),
        InitialCommissionVarRate("initialCommissionIfVarRate"),
        LifeInsurance("requiresLifeInsurance"),
        HasMonthlyProcessingFee("hasMonthlyProcessingFee"),
        VarRateProcessingFee("monthlyProcessingFeeIfVarRate"),
        FixedAmountProcessingFee("monthlyProcessingFeeIfFixedAmount"),
        LowestTaxRateTAN("lowestTaxRateTAN");

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

    private final PaymentHelperPT paymentHelper;

    @Inject
    public MonthlyPaymentHelperPT(@Named("paymentHelperPT") PaymentHelperPT paymentHelper) {
        this.paymentHelper = paymentHelper;
    }

    @Override
    public MortgagePT computeMortgage(double loanAmount, int tenure, PersonalLoan personalLoan) {
        MortgagePT mortgage = new MortgagePT();
        mortgage.setPrincipalLoanAmount(loanAmount);
        mortgage.setTenureInMonths(tenure);

        //set fees
        updateMortgageKeys(personalLoan, mortgage);

        FeeHelper feeHelper = new FeeHelper(ListHelper.setToList(personalLoan.getFeesList()));
        mortgage.setAnnualHandlingFee(feeHelper.getAnnualHandlingFee());
        mortgage.setMinHandlingFee(feeHelper.getMinHandlingFee());

        // set Payments
        List<InterestRateInfo> interestRates = RateTypeConverter.convert(personalLoan.getInterestList());
        InterestRateHelper rateHelper = new InterestRateHelper();
        rateHelper.updateMortgageInterest(mortgage, interestRates);

        double commissionAmount = paymentHelper.computeCommissionAmount(mortgage);
        mortgage.setCommissionAmount(commissionAmount);
        mortgage.setTaxOnCredit(paymentHelper.getTaxOnCredit(loanAmount, tenure));
        mortgage.setLifeInsurance(paymentHelper.getLifeInsurance(loanAmount,
                tenure,
                mortgage.isLifeInsuranceRequired()));
        mortgage.setBorrowedAmount(mortgage.getBorrowedAmount());
        mortgage.setMonthlyPayment(paymentHelper.getLoanPayment(mortgage));
        mortgage.setMonthlyProcessingPercent(paymentHelper.getProcessingPercent(mortgage));
        mortgage.setMonthlyProcessingFee(paymentHelper.getMonthlyProcessingFee(mortgage));
        mortgage.setTotalRepayment(paymentHelper.getTotalPayment(mortgage));
        mortgage.setTAEG(paymentHelper.getTAEG(mortgage));
        return mortgage;
    }

    private void updateMortgageKeys(PersonalLoan personalLoan, MortgagePT mortgage) {
        generalInfoUpdates(personalLoan, mortgage);

        for (Fee fee : personalLoan.getFeesList()) {
            MortgageKeys mortgageKey = MortgageKeys.findByKey(fee.getTypeValue());
            if (mortgageKey != null) {
                BigDecimal value = (fee.getAmount() == null) ? BigDecimal.valueOf(0) : fee.getAmount();
                switch (mortgageKey) {
                    case VarRateProcessingFee:
                        double doubleRateVal = value.doubleValue();
                        if (doubleRateVal > 0) {
                            doubleRateVal = doubleRateVal / 100;
                        }
                        mortgage.setVarRateProcessingFee(doubleRateVal);
                        break;
                    case FixedAmountProcessingFee:
                        mortgage.setFixedAmtProcessingFee(value.doubleValue());
                        break;
                    default:
                }
            }
        }
        for (Interest interest : personalLoan.getInterestList()) {
            MortgageKeys mortgageKey = MortgageKeys.findByKey(interest.getTypeValue());
            if (mortgageKey != null) {
                BigDecimal value = (interest.getAmount() == null) ? BigDecimal.valueOf(0) : interest.getAmount();
                switch (mortgageKey) {
                    case LowestTaxRateTAN:
                        if (value != null
                            && value.compareTo(BigDecimal.ZERO) > 0) {
                            mortgage.setTAN(value.doubleValue() / 100);
                            mortgage.setLowestTaxRateTAN(value.doubleValue());
                        }
                        break;
                    default:
                }
            }
        }
        for (Filter filter : personalLoan.getFilter()) {
            MortgageKeys mortgageKey = MortgageKeys.findByKey(filter.getTypeValue());
            if (mortgageKey != null) {
                boolean value = "1".equals(filter.getValue()) ? true : false;
                switch (mortgageKey) {
                    case HasMonthlyProcessingFee:
                        mortgage.setHasMonthlyProcessingFee(value);
                        break;
                    default:
                }
            }
        }
    }

    private void generalInfoUpdates(PersonalLoan personalLoan, MortgagePT mortgage) {
        MortgagePT.InitialCommission initialCommission = new MortgagePT.InitialCommission();
        for (GeneralInfo generalInfo : personalLoan.getGeneralInfo()) {
            MortgageKeys mortgageKey = MortgageKeys.findByKey(generalInfo.getTypeValue());
            if (mortgageKey != null) {
                String value = generalInfo.getDescription();
                switch (mortgageKey) {
                    case InitialCommissionMin:
                        initialCommission.setMinimum(StringUtils.isNotBlank(value)? Double.valueOf(generalInfo.getDescription()) : 0);
                        break;
                    case InitialCommissionMax:
                        initialCommission.setMaximum(StringUtils.isNotBlank(value)? Double.valueOf(generalInfo.getDescription()) : 0);
                        break;
                    case InitialCommissionFixed:
                        initialCommission.setFixedAmount(StringUtils.isNotBlank(value)? Double.valueOf(generalInfo.getDescription()) : 0);
                        break;
                    case InitialCommissionVarRate:
                        double varRate = StringUtils.isNotBlank(value)? Double.valueOf(generalInfo.getDescription()) : 0;
                            varRate = varRate / 100;
                        initialCommission.setRateAmount(varRate);
                        break;
                    case LifeInsurance:
                        mortgage.setLifeInsuranceRequired(StringUtils.isNotBlank(value)
                                && "1".equals(value)
                                ? true : false);
                        break;
                    default:
                }
            }
        }
        mortgage.setInitialCommission(initialCommission);
    }
}
