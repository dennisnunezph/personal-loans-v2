package com.compareglobal.service.loans.personal.domain.sg;

import com.compareglobal.service.common.domain.Filter;
import com.compareglobal.service.common.utils.ListHelper;
import com.compareglobal.service.loans.personal.domain.GeneralInfo;
import com.compareglobal.service.loans.personal.domain.MonthlyPaymentHelper;
import com.compareglobal.service.loans.personal.domain.Mortgage;
import com.compareglobal.service.loans.personal.domain.PersonalLoan;
import com.compareglobal.service.loans.personal.domain.eligibility.Criteria;
import com.compareglobal.service.loans.personal.domain.fees.Fee;
import com.compareglobal.service.loans.personal.domain.interestrates.Interest;
import com.compareglobal.service.loans.personal.logic.PaymentHelper;
import com.compareglobal.service.loans.personal.logic.hk.FeeHelper;
import com.compareglobal.service.loans.personal.logic.hk.PaymentHelperHK;
import com.compareglobal.service.loans.personal.view.sg.MortgageSG;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by dennis on 4/22/15.
 */
@Component("monthlyPaymentHelperSG")
public class MonthlyPaymentHelperSG implements MonthlyPaymentHelper {

    public enum MortgageKeys {
        LowestApr("lowestApr"),
        MonthlyInterestRate("monthly_interest_rate"),
        EffectiveInterestRate("effective_interest_rate"),
        EffectiveInterestRateCombined("effective_interest_rate_combined"),
        MinMonthlyPayment("minMonthlyPaymentSGD"),
        MinMonthlyPaymentPercent("minMonthlyPaymentPercent"),
        MinHandlingFeePercent("minHandlingFeePercent"),
        MinHandlingFeeAmount("minHandlingFeeAmount"),
        InterestProfile("interestProfile"),
        HandlingFeeType("handling_fee_type"),
        AnnualHandlingFee("annualHandlingFee"),
        HasInstallmentLoan("hasPersonalInstalmentLoan"),
        HasTaxLoan("hasTaxLoan"),
        HasCreditLine("hasPersonalCreditLine"),
        TypeValue("typeValue"),
        Tenure("tenure"),
        AprBasis("aprBasis"),
        AnnualRateText("annualRateText"),
        MaxBalanceTrfTenure("maxbalancetrftenure"),
		Fir2Years("fir_2_Years");

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

    private final PaymentHelper paymentHelper;

    @Inject
    public MonthlyPaymentHelperSG(@Named("paymentHelperSG") PaymentHelper paymentHelper) {
        this.paymentHelper = paymentHelper;
    }

    @Override
    public Mortgage computeMortgage(double loanAmount, int tenure, PersonalLoan personalLoan) {
        MortgageSG mortgage = new MortgageSG();
        mortgage.setPrincipalLoanAmount(loanAmount);
        mortgage.setTenureInMonths(tenure);

        //set fees
        updateMortgageKeys(personalLoan,mortgage);

        FeeHelper feeHelper = new FeeHelper(ListHelper.setToList(personalLoan.getFeesList()));
        mortgage.setAnnualHandlingFee(feeHelper.getAnnualHandlingFee());
        mortgage.setMinHandlingFee(feeHelper.getMinHandlingFee());

        // set Payments
        InterestRateHelperSG rateHelper = new InterestRateHelperSG();
        rateHelper.updateMortgageInterest(mortgage, personalLoan.getInterestList());

        mortgage.setMonthlyPayment(paymentHelper.getLoanPayment(mortgage));
        mortgage.setTotalRepayment(mortgage.getMonthlyPayment() * mortgage.getTenureInMonths());


        if (mortgage.needsSeparateTaxLoanComputation()) {
            mortgage.setMonthlyPaymentForTaxLoan(getMonthlyPaymentForTaxLoan(mortgage));
        }

        // APR
        //mortgage.setApr(rateHelper.getRateAPR(mortgage, interestRates, mortgage.getLowestApr()));


        if (mortgage.getCategoryProfile().equals(InterestProfileSG.CreditLine)) {
            mortgage.setCreditLineAnnualInterestRate(mortgage.getMonthlyInterestRate());

            double monthlyPayment = 0;
            double monthlyPaymentPercent = 0;
            mortgage.setCreditLineMonthlyRepayment(getCreditLineMonthlyRepayment(mortgage.getPrincipalLoanAmount(), monthlyPayment, monthlyPaymentPercent));
            mortgage.setCreditLineDailyInterest(getCreditLineDailyInterest(mortgage.getPrincipalLoanAmount(), mortgage.getCreditLineAnnualInterestRate()));
        }
        return mortgage;
    }

    private void updateMortgageKeys(PersonalLoan personalLoan, MortgageSG mortgage) {
        for (Fee fee : personalLoan.getFeesList()) {
            MortgageKeys mortgageKey = MortgageKeys.findByKey(fee.getTypeValue());
            if (mortgageKey != null) {
                BigDecimal value = (fee.getAmount() == null) ? BigDecimal.valueOf(0): fee.getAmount();
                double descValue = StringUtils.isBlank(fee.getDescription()) ? 0 : Double.valueOf(fee.getDescription());
                switch (mortgageKey) {
                    case MinMonthlyPayment:
                        mortgage.setMinMonthlyPayment(descValue);
                        break;
                    case MinMonthlyPaymentPercent:
                        if (descValue >= 1) {
                            descValue = descValue / 100;
                        }
                        mortgage.setMinMonthlyPaymentPercent(descValue);
                        break;
                    case EffectiveInterestRate:
                        mortgage.setEffectiveInterestRate(value.doubleValue());
                    case HandlingFeeType:
                        mortgage.setHandlingFeeType(value.intValue());
                        break;
                    case AnnualHandlingFee:
                        mortgage.setAnnualHandlingFee(value.doubleValue());
                        break;
                    default:
                }
            }
        }
        for (Interest interest : personalLoan.getInterestList()) {
            MortgageKeys mortgageKey = MortgageKeys.findByKey(interest.getTypeValue());
            if (mortgageKey != null) {
                double doubleVal = NumberUtils.toDouble(interest.getDescription());
                BigDecimal bdValue = (interest.getAmount() == null) ? BigDecimal.valueOf(0): interest.getAmount();
                switch (mortgageKey) {
                    case MonthlyInterestRate:
                        mortgage.setMonthlyInterestRate(doubleVal);
                        break;
                    case MinHandlingFeePercent:
                        double feePercent = bdValue.doubleValue();
                        if (feePercent >= 1) {
                            feePercent = feePercent / 100;
                        }
                        mortgage.setMinHandlingFeePercent(feePercent);
                        break;
                    case MinHandlingFeeAmount:
                        mortgage.setMinHandlingFeeAmount(bdValue.doubleValue());
                        break;
                    case InterestProfile:
                        int profile = StringUtils.isEmpty(interest.getDescription()) ? 0 : Integer.valueOf(interest.getDescription());
                        mortgage.setInterestProfile(profile);
                        break;
                    case LowestApr:
                        mortgage.setLowestApr(bdValue.doubleValue());
                        break;
                    case AprBasis:
                        mortgage.setAprBasis(interest.getDescription());
                        break;
                    case EffectiveInterestRateCombined:
                        mortgage.setEffectiveInterestRateCombined(interest.getDescription());
                        break;
                    case MaxBalanceTrfTenure:
                        mortgage.setMaxBalanceTrfTenure(interest.getDescription());
                        break;
                    case Fir2Years:
                        mortgage.setFir2Years(bdValue.doubleValue());
                        break;
                    default:
                }
            }
        }
        for (Filter filter : personalLoan.getFilter()) {
            InterestProfileSG interestProfile = InterestProfileSG.findByKey(filter.getTypeValue());
            if (interestProfile != null) {
                boolean filterEnabled = "1".equals(filter.getValue()) ? true : false;
                if (filterEnabled) {
                    mortgage.setCategoryProfile(interestProfile);
                }
            }
        }
        if (mortgage.getCategoryProfile() == null) {
            mortgage.setCategoryProfile(InterestProfileSG.PersonalInstalment);
        }

        for (Criteria criteria : personalLoan.getEligibilityList()) {
            MortgageKeys mortgageKey = MortgageKeys.findByKey(criteria.getTypeValue());
            if (mortgageKey != null) {
                long maxValue = criteria.getMaximum();
                switch (mortgageKey) {
                    case Tenure:
                        mortgage.setMaxTenure(maxValue);
                        break;
                    default:
                }
            }
        }

        for (GeneralInfo generalInfo : personalLoan.getGeneralInfo()) {
            MortgageKeys mortgageKey = MortgageKeys.findByKey(generalInfo.getTypeValue());
            if (mortgageKey != null) {
                String descValue = generalInfo.getDescription();
                switch (mortgageKey) {
                    case AnnualRateText:
                        mortgage.setAnnualRateText(descValue);
                        break;
                    default:
                }
            }
        }
    }

    private double getMonthlyPaymentForTaxLoan(Mortgage mortgage) {
        double loanAmount = mortgage.getPrincipalLoanAmount();
        double monthlyFlatRateValue = mortgage.getMonthlyInterestRate();
        int loanTenure = Mortgage.MONTHS_IN_A_YEAR;
        int handlingFeeType = mortgage.getHandlingFeeType();

        PaymentHelperHK paymentHelperHK = (PaymentHelperHK) paymentHelper;
        return paymentHelperHK.getMonthlyFlatRate(loanAmount,
                monthlyFlatRateValue,
                loanTenure,
                handlingFeeType,
                mortgage.getMinHandlingFee(),
                mortgage.getAnnualHandlingFee());
    }

    public double getCreditLineMonthlyRepayment(double loanAmount,
                                                double minMonthlyPayment,
                                                double minMonthlyPaymentPercent) {
        double minimumRateVal = 0;

        if (minMonthlyPayment == 0) {
            if (minMonthlyPaymentPercent == 0) {
                minimumRateVal = 0;
            } else {
                minimumRateVal = (loanAmount * minMonthlyPaymentPercent) / 100;
            }
        }
        else {
            if (minMonthlyPaymentPercent == 0) {
                minimumRateVal = minMonthlyPayment;
            } else {
                minimumRateVal = (loanAmount * minMonthlyPaymentPercent) / 100;
            }
        }
        return minimumRateVal;
    }

    public double getCreditLineDailyInterest(double loanAmount, double annualRateVal) {
        double dailyInterestVal = 0;
        if (annualRateVal != 0) {
            dailyInterestVal = (loanAmount * (annualRateVal / (365 * 100)));
        }
        return dailyInterestVal;
    }



    private double getSafeDouble(String doubleValue) {
        double safeVal = 0;
        try {
            safeVal = Double.valueOf(doubleValue);
        } catch (Exception ex) {
            safeVal = 0;
        }
        return safeVal;
    }

}
