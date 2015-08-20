package com.compareglobal.service.loans.personal.domain.id;

import com.compareglobal.service.common.domain.Filter;
import com.compareglobal.service.common.utils.ListHelper;
import com.compareglobal.service.loans.personal.domain.PersonalLoan;
import com.compareglobal.service.loans.personal.domain.fees.Fee;
import com.compareglobal.service.loans.personal.domain.interestrates.Interest;
import com.compareglobal.service.loans.personal.logic.id.FeeHelperID;
import com.compareglobal.service.loans.personal.logic.id.PaymentHelperID;
import com.compareglobal.service.loans.personal.view.id.MortgageID;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component("monthlyPaymentHelperID")
public class MonthlyPaymentHelperID {

    private PaymentHelperID paymentHelper;

    public enum MortgageKeys {
        LowestApr("lowestApr"),
        MinMonthlyPayment("minMonthlyPaymentXXD"),
        MinMonthlyPaymentPercent("minMonthlyPaymentPercent"),
        InterestProfile("interestProfile"),
        HasInstallmentLoan("hasPersonalInstalmentLoan"),
        HasTaxLoan("hasTaxLoan"),
        HasCreditLine("hasPersonalCreditLine"),
        TypeValue("typeValue"),
        MaxLoanAmount("maxLoanAmount"),
        MinLoanAmount("minLoanAmount"),
        MonthlyInterestRate("monthly_interest_rate"),
        AnnualHandlingFee("annualHandlingFee"),
        HandlingFeeType("handling_fee_type"),
        FirstYearPercentageHandlingFee("1stYearPercentageHandlingFee"),
        FirstYearFixedHanlingFee("1stYearFixedHanlingFee"),
        MinHandlingFee("minHandlingFee");

        private final String mortgageKey;

        MortgageKeys(String mortgageKey) {
            this.mortgageKey = mortgageKey;
        }

        public String getMortgageKey() {
            return mortgageKey;
        }

        public static MortgageKeys findByKey(String rateKey) {
            for (MortgageKeys key : values()) {
                if (key.getMortgageKey().equalsIgnoreCase(rateKey)) {
                    return key;
                }
            }
            return null;
        }
    }

    public MortgageID populateMortgage(double loanAmount, int tenure, PersonalLoan personalLoan) {

        paymentHelper = new PaymentHelperID();

        MortgageID mortgage = new MortgageID();
        mortgage.setPrincipalLoanAmount(loanAmount);
        mortgage.setTenureInMonths(tenure);

        //set fees
        updateMortgageKeys(personalLoan, mortgage);

        FeeHelperID feeHelperID = new FeeHelperID(ListHelper.setToList(personalLoan.getFeesList()));
        mortgage.setAnnualHandlingFee(feeHelperID.getAnnualHandlingFee());
        mortgage.setMinHandlingFee(feeHelperID.getMinHandlingFee());

        // set Payments
        List<InterestRateInfoID> interestRates = RateTypeConverterID.convert(personalLoan.getInterestList());
        InterestRateHelperID rateHelper = new InterestRateHelperID();

        mortgage.setMonthlyPayment(paymentHelper.getLoanPayment(mortgage));
        mortgage.setTotalRepayment(paymentHelper.getTotalPayment(mortgage));

        // APR
        mortgage.setApr(rateHelper.getRateAPR(mortgage, interestRates, mortgage.getLowestApr()));

        return mortgage;
    }

    private void updateMortgageKeys(PersonalLoan personalLoan, MortgageID mortgage) {
        for (Fee fee : personalLoan.getFeesList()) {
            MortgageKeys mortgageKey = MortgageKeys.findByKey(fee.getTypeValue());
            if (mortgageKey != null) {
                BigDecimal value = (fee.getAmount() == null) ? BigDecimal.valueOf(0) : fee.getAmount();
                switch (mortgageKey) {


                   case MaxLoanAmount :
                    paymentHelper.setMaxLoanAmount(value.doubleValue());
                    break;

                    case MinLoanAmount :
                    paymentHelper.setMinLoanAmount(value.doubleValue());
                    break;

                    case  LowestApr:
                    paymentHelper.setLowestApr(value.doubleValue());
                    break;

                    case AnnualHandlingFee:
                    paymentHelper.setAnnualHandlingFee(value.doubleValue());
                    break;

                    case  FirstYearPercentageHandlingFee:
                    paymentHelper.setFirstYearPercentageHandlingFee(value.doubleValue());
                    break;

                    case FirstYearFixedHanlingFee:
                    paymentHelper.setFirstYearFixedHanlingFee(value.doubleValue());
                    break;

                    case  MinHandlingFee:
                    paymentHelper.setMinHandlingFee(value.doubleValue());
                    break;

                    case MonthlyInterestRate:
                        paymentHelper.setMonthlyInterestRate(value.doubleValue());
                        mortgage.setMonthlyInterestRate(value.doubleValue());

                        break;
                    case MinMonthlyPayment:
                        mortgage.setMinMonthlyPayment(value.doubleValue());
                        break;
                    case MinMonthlyPaymentPercent:
                        mortgage.setMinMonthlyPaymentPercent(value.doubleValue());
                        break;
                    case HandlingFeeType:
                        mortgage.setHandlingFeeType(value.intValue());
                        paymentHelper.setHandlingFeeType(value.intValue());
                        break;
                    default:
                }
            }
        }

        for (Interest interest : personalLoan.getInterestList()) {
            MortgageKeys mortgageKey = MortgageKeys.findByKey(interest.getTypeValue());
            if (mortgageKey != null) {

                switch (mortgageKey) {
                    case InterestProfile:
                        int profile = StringUtils.isEmpty(interest.getDescription()) ? 0 : Integer.valueOf(interest.getDescription());
                        mortgage.setInterestProfile(profile);
                        break;
                    case LowestApr:
                        BigDecimal value = (interest.getAmount() == null) ? BigDecimal.valueOf(0) : interest.getAmount();
                        mortgage.setLowestApr(value.doubleValue());
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
                    case HasInstallmentLoan:
                        mortgage.setHasPersonalInstallment(value);
                        break;
                    case HasTaxLoan:
                        mortgage.setHasTaxLoan(value);
                        break;
                    case HasCreditLine:
                        mortgage.setHasCreditLine(value);
                        break;
                    default:
                }
            }
        }
    }

}

