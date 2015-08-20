package com.compareglobal.service.loans.personal.domain.xx;

import com.compareglobal.service.common.domain.Filter;
import com.compareglobal.service.common.utils.ListHelper;
import com.compareglobal.service.loans.personal.domain.Mortgage;
import com.compareglobal.service.loans.personal.domain.PersonalLoan;
import com.compareglobal.service.loans.personal.domain.fees.Fee;
import com.compareglobal.service.loans.personal.domain.interestrates.Interest;
import com.compareglobal.service.loans.personal.logic.PaymentHelper;
import com.compareglobal.service.loans.personal.logic.hk.FeeHelper;
import com.compareglobal.service.loans.personal.logic.xx.PaymentHelperXX;
import com.compareglobal.service.loans.personal.view.xx.MortgageXX;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import java.math.BigDecimal;
import java.util.List;

@Component("monthlyPaymentHelperXX")
public class MonthlyPaymentHelperXX {

    public enum MortgageKeys {
        LowestApr("lowestApr"),
        MonthlyInterestRate("monthly_interest_rate"),
        MinMonthlyPayment("minMonthlyPaymentXXD"),
        MinMonthlyPaymentPercent("minMonthlyPaymentPercent"),
        InterestProfile("interestProfile"),
        HandlingFeeType("handling_fee_type"),
        HasInstallmentLoan("hasPersonalInstalmentLoan"),
        HasTaxLoan("hasTaxLoan"),
        HasCreditLine("hasPersonalCreditLine"),
        TypeValue("typeValue"),;

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
    public MonthlyPaymentHelperXX(@Named("paymentHelperXX") PaymentHelper paymentHelper) {
        this.paymentHelper = paymentHelper;
    }

    public MortgageXX populateMortgage(double loanAmount, int tenure, PersonalLoan personalLoan) {

        MortgageXX mortgage = new MortgageXX();
        mortgage.setPrincipalLoanAmount(loanAmount);
        mortgage.setTenureInMonths(tenure);

        //set fees
        updateMortgageKeys(personalLoan,mortgage);

        FeeHelper feeHelper = new FeeHelper(ListHelper.setToList(personalLoan.getFeesList()));
        mortgage.setAnnualHandlingFee(feeHelper.getAnnualHandlingFee());
        mortgage.setMinHandlingFee(feeHelper.getMinHandlingFee());

        // set Payments
        List<InterestRateInfoXX> interestRates = RateTypeConverterXX.convert(personalLoan.getInterestList());
        InterestRateHelperXX rateHelper = new InterestRateHelperXX();
        rateHelper.updateMortgageInterest(mortgage, interestRates);

        mortgage.setMonthlyPayment(paymentHelper.getLoanPayment(mortgage));
        mortgage.setTotalRepayment(mortgage.getMonthlyPayment() * mortgage.getTenureInMonths());

        if (mortgage.needsSeparateTaxLoanComputation()) {
            mortgage.setMonthlyPaymentForTaxLoan(getMonthlyPaymentForTaxLoan(mortgage));
        }

        // APR
        mortgage.setApr(rateHelper.getRateAPR(mortgage, interestRates, mortgage.getLowestApr()));

        // Credit Line
        Filter creditLineFilter = ListHelper.findItemByTypeT(personalLoan.getFilter(),
                MortgageKeys.TypeValue.getMortgageKey(),
                MortgageKeys.HasCreditLine.getMortgageKey());

        if (creditLineFilter != null
                && "1".equals(creditLineFilter.getValue())) {
            mortgage.setHasCreditLine(true);
        }

        if (mortgage.isHasCreditLine()) {
            mortgage.setCreditLineAnnualInterestRate(mortgage.getMonthlyInterestRate());
            double minMonthlyPayment = mortgage.getMinMonthlyPayment();
            double minMonthlyPaymentPercent = mortgage.getMinMonthlyPaymentPercent();

            mortgage.setCreditLineMonthlyRepayment(getCreditLineMonthlyRepayment(mortgage.getPrincipalLoanAmount(), minMonthlyPayment, minMonthlyPaymentPercent));
            mortgage.setCreditLineDailyInterest(getCreditLineDailyInterest(mortgage.getPrincipalLoanAmount(), mortgage.getCreditLineAnnualInterestRate()));
        }
        return mortgage;
    }

    private void updateMortgageKeys(PersonalLoan personalLoan, MortgageXX mortgage) {
        for (Fee fee : personalLoan.getFeesList()) {
            MortgageKeys mortgageKey = MortgageKeys.findByKey(fee.getTypeValue());
            if (mortgageKey != null) {
                BigDecimal value = (fee.getAmount() == null) ? BigDecimal.valueOf(0): fee.getAmount();
                switch (mortgageKey) {
                    case MonthlyInterestRate:
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
                        BigDecimal value = (interest.getAmount() == null) ? BigDecimal.valueOf(0): interest.getAmount();
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

    private double getMonthlyPaymentForTaxLoan(Mortgage mortgage) {
        double loanAmount = mortgage.getPrincipalLoanAmount();
        double monthlyFlatRateValue = mortgage.getMonthlyInterestRate();
        int loanTenure = Mortgage.MONTHS_IN_A_YEAR;
        int handlingFeeType = mortgage.getHandlingFeeType();

        PaymentHelperXX paymentHelperXX = (PaymentHelperXX) paymentHelper;
        return paymentHelperXX.getMonthlyFlatRate(loanAmount,
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
}

