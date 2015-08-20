/*
 * Copyright (c) 2015.
 * Compare Asia Group
 */
package com.compareglobal.service.loans.personal.domain.dk;

import com.compareglobal.service.common.domain.CompanyInfoHelper;
import com.compareglobal.service.common.domain.Filter;
import com.compareglobal.service.common.domain.LinkUrlHelper;
import com.compareglobal.service.common.domain.converter.TypeValueHelper;
import com.compareglobal.service.common.domain.Item;
import com.compareglobal.service.common.utils.ListHelper;
import com.compareglobal.service.loans.personal.domain.GeneralInfo;
import com.compareglobal.service.loans.personal.domain.PersonalLoan;
import com.compareglobal.service.loans.personal.domain.PersonalLoanHelper;
import com.compareglobal.service.loans.personal.domain.PersonalLoanPublic;
import com.compareglobal.service.loans.personal.domain.fees.Fee;
import com.compareglobal.service.loans.personal.logic.PaymentHelper;
import com.compareglobal.service.loans.personal.view.dk.PersonalLoanDisplayDK;
import com.compareglobal.service.loans.personal.view.dk.PersonalLoanViewDK;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by dennis on 2/16/15.
 */
@Component("personalLoanHelperDK")
public class PersonalLoanHelperDK implements PersonalLoanHelper {

    private static final String ONLINE_LENDER_KEY = "kviklaanTap";
    private static final String TYPE_VALUE = "typeValue";

    public enum MortgageKeys {
        MonthlyFlatRateMin("lowestMonthlyFlatRate"),
        MonthlyFlatRateMax("highestMonthlyFlatRate"),
        FloatingHandlingFee("floatingHandlingFee"),
        MinimumFloatingHandlingFee("minFloatingHandlingFee"),
        MonthlyFee("monthlyFee"),
        ProductImage("logo"),
        ProductLink("link"),
        LegalText("legalText");

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
    public PersonalLoanHelperDK(@Named("paymentHelperDK") PaymentHelper paymentHelper) {
        this.paymentHelper = paymentHelper;
    }

    @Override
    public PersonalLoanPublic build(PersonalLoan personalLoan, double loanAmount, int tenure) {
        PersonalLoanPublic loanPublic = new PersonalLoanDisplayDK(personalLoan);

        loanPublic.setId(personalLoan.getId());
        //company
        CompanyInfoHelper companyInfoHelper = new CompanyInfoHelper(personalLoan.getProvider(), personalLoan.getImages());
        loanPublic.setCompany(companyInfoHelper.getCompanyInfo());

        MortgageDK mortgage = new MortgageDK();
        mortgage.setPrincipalLoanAmount(loanAmount);
        mortgage.setTenureInMonths(tenure);

        updateMortgage(personalLoan, mortgage);

        double minMonthlyPayment = paymentHelper.getLoanPayment(mortgage, mortgage.getMonthlyInterestRate());
        minMonthlyPayment = minMonthlyPayment + mortgage.getMonthlyFee();
        mortgage.setMonthlyPayment(minMonthlyPayment);

        double maxMonthlyPayment = paymentHelper.getLoanPayment(mortgage, mortgage.getMonthlyInterestRateMax());
        mortgage.setMonthlyPaymentMax(maxMonthlyPayment);

        MortgageDisplayDK mortgageDisplay = new MortgageDisplayDK();
        mortgageDisplay.setInterestRate(mortgage.getMonthlyInterestRate());
        mortgageDisplay.setInterestRateMax(mortgage.getMonthlyInterestRateMax());
        mortgageDisplay.setMonthlyPayment(minMonthlyPayment);
        mortgageDisplay.setMonthlyPaymentMax(maxMonthlyPayment);

        double minAPR = paymentHelper.getAPR(mortgage.getTenureInMonths(),
                            mortgage.getMonthlyPayment(),
                            mortgage.getPrincipalLoanAmount(),
                            mortgage.getMonthlyInterestRate());
        mortgageDisplay.setApr(minAPR);



        double maxAPR = paymentHelper.getAPR(mortgage.getTenureInMonths(),
                            mortgage.getMonthlyPaymentMax(),
                            mortgage.getPrincipalLoanAmount(),
                            mortgage.getMonthlyInterestRateMax());
        mortgageDisplay.setAprMax(maxAPR);
        mortgageDisplay.setLegalText(mortgage.getLegalText());

        loanPublic.setPayments(mortgageDisplay);

        //filter
        if (personalLoan.getFilter() != null) {
            List<Item> filterListOutput = TypeValueHelper.getParsedItem(personalLoan.getFilter());
            loanPublic.setFilters(filterListOutput);
        }

        //ranking
        if (personalLoan.getRankings() != null) {
            List<Item> rankListOutput = TypeValueHelper.getParsedItem(personalLoan.getRankings());
            loanPublic.setRanking(rankListOutput);
        }

        //featured
        if (personalLoan.getFeatures() != null) {
            List<Item> featuredItemList = TypeValueHelper.getParsedItem(personalLoan.getFeatures());
            loanPublic.setFeatured(featuredItemList);
        }

        JSONObject productInfo = new JSONObject();
        productInfo.put("productImage", LinkUrlHelper.getImageValue(personalLoan.getImages(),
                MortgageKeys.ProductImage.getMortgageKey()));
        productInfo.put("productLink", LinkUrlHelper.getLinkValue(personalLoan.getLinks(),
                MortgageKeys.ProductLink.getMortgageKey()));
        loanPublic.setProduct(productInfo);

        update(personalLoan, loanPublic);

        return loanPublic;
    }

    private void update(PersonalLoan personalLoan,
                        PersonalLoanPublic loanPublic) {
        PersonalLoanViewDK viewDK = new PersonalLoanViewDK();
        JSONObject plView = viewDK.view(personalLoan);
        PersonalLoanDisplayDK loanDisplayDK = (PersonalLoanDisplayDK) loanPublic;
        loanDisplayDK.setDisplayView(plView);
    }

    private void updateMortgage(PersonalLoan personalLoan, MortgageDK mortgage) {
        for (Fee fee : personalLoan.getFeesList()) {
            MortgageKeys mortgageKey = MortgageKeys.findByKey(fee.getTypeValue());
            if (mortgageKey != null) {
                switch (mortgageKey) {
                    case MonthlyFlatRateMin:
                        if (fee.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                            mortgage.setMonthlyInterestRate(fee.getAmount().doubleValue() / 100);
                        }
                        break;
                    case MonthlyFlatRateMax:
                        if (fee.getAmount().compareTo(BigDecimal.ZERO) > 0) {
                            mortgage.setMonthlyInterestRateMax(fee.getAmount().doubleValue() / 100);
                        }
                        break;
                    case FloatingHandlingFee:
                        mortgage.setFloatingHandlingFee(fee.getAmount().doubleValue());
                        break;
                    case MinimumFloatingHandlingFee:
                        mortgage.setMinimumFloatingHandlingFee(fee.getAmount().doubleValue());
                        break;
                    case MonthlyFee:
                        mortgage.setMonthlyFee(fee.getAmount().doubleValue());
                        break;
                    default:
                }
            }
        }

        GeneralInfo legalTextInfo = ListHelper.findItemByTypeT(personalLoan.getGeneralInfo(),
                TYPE_VALUE, MortgageKeys.LegalText.getMortgageKey());
        if (legalTextInfo != null) {
            String legalText = legalTextInfo.getDescription();
            legalText = StringUtils.isBlank(legalText)
                    || "0".equals(legalText) ? "" : legalText;
            mortgage.setLegalText(legalText);
        }

        Filter filter = ListHelper.findItemByTypeT(personalLoan.getFilter(), TYPE_VALUE, ONLINE_LENDER_KEY);
        boolean onlineLender = filter != null
                && "0".equals(filter.getValue());
        mortgage.setOnlineLender(onlineLender);
    }

    @Override
    public String getLocale() {
        return "en-DK";
    }
}
