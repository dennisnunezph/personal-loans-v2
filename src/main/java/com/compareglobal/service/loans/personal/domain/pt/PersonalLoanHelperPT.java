package com.compareglobal.service.loans.personal.domain.pt;

import com.compareglobal.service.common.domain.CompanyInfoHelper;
import com.compareglobal.service.common.domain.Item;
import com.compareglobal.service.common.domain.LinkUrlHelper;
import com.compareglobal.service.common.domain.converter.TypeValueHelper;
import com.compareglobal.service.loans.personal.domain.*;
import com.compareglobal.service.loans.personal.domain.converter.PromotionHelper;
import com.compareglobal.service.loans.personal.view.PersonalLoanDisplay;
import com.compareglobal.service.loans.personal.view.pt.MortgagePT;
import com.compareglobal.service.loans.personal.view.pt.PersonalLoanViewPT;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * Created by dennis on 4/22/15.
 */
@Component("personalLoanHelperPT")
public class PersonalLoanHelperPT implements PersonalLoanHelper {

    public enum PLKeys {
        ProductImage("product_icon"),
        ProductLink("productLink");

        private final String plKey;

        PLKeys(String plKey) {
            this.plKey = plKey;
        }

        private String getPLKey() {
            return plKey;
        }
    }

    private final MonthlyPaymentHelperPT paymentHelper;

    @Inject
    public PersonalLoanHelperPT(@Named("monthlyPaymentHelperPT") MonthlyPaymentHelperPT paymentHelper) {
        this.paymentHelper = paymentHelper;
    }

    public PersonalLoanPublic build(PersonalLoan personalLoan, double loanAmount, int tenure) {

        PersonalLoanPublic loanPublic = new PersonalLoanDisplay(personalLoan);

        loanPublic.setId(personalLoan.getId());

        MortgagePT mortgage = paymentHelper.computeMortgage(loanAmount, tenure, personalLoan);
        MortgageDisplayPT mortgageDisplay = new MortgageDisplayPT();
        mortgageDisplay.setCommissionAmount(mortgage.getCommissionAmount());
        mortgageDisplay.setTaxOnCredit(mortgage.getTaxOnCredit());
        mortgageDisplay.setLifeInsurance(mortgage.getLifeInsurance());
        mortgageDisplay.setBorrowedAmount(mortgage.getBorrowedAmount());
        mortgageDisplay.setMonthlyPayment(mortgage.getMonthlyPayment());
        mortgageDisplay.setProcessingPercent(mortgage.getMonthlyProcessingPercent());
        mortgageDisplay.setMonthlyProcessingFee(mortgage.getMonthlyProcessingFee());
        mortgageDisplay.setTotalInterest(mortgage.getTotalInterest());
        mortgageDisplay.setTAEG(mortgage.getTAEG());
        mortgageDisplay.setLowestTaxRateTAN(mortgage.getLowestTaxRateTAN());
        mortgageDisplay.setTotalMonthlyPayment(mortgage.getTotalMonthlyPayment());
        mortgageDisplay.setTotalPayment(mortgage.getTotalRepayment());


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

        //company
        CompanyInfoHelper companyInfoHelper = new CompanyInfoHelper(personalLoan.getProvider());
        loanPublic.setCompany(companyInfoHelper.getCompanyInfo());

        loanPublic.setPromotions(PromotionHelper.convertToItem(personalLoan.getPromotions()));

        JSONObject productInfo = new JSONObject();
        productInfo.put("productImage", LinkUrlHelper.getImageValue(personalLoan.getImages(),
                PLKeys.ProductImage.getPLKey()));
        productInfo.put("productLink", LinkUrlHelper.getLinkValue(personalLoan.getLinks(),
                PLKeys.ProductLink.getPLKey()));
        loanPublic.setProduct(productInfo);

        update(personalLoan, loanPublic);

        return loanPublic;
    }

    private void update(PersonalLoan personalLoan,
                        PersonalLoanPublic loanPublic) {
        PersonalLoanViewPT viewPT = new PersonalLoanViewPT();
        JSONObject plView = viewPT.view(personalLoan);
        PersonalLoanDisplay loanDisplay = (PersonalLoanDisplay) loanPublic;
        loanDisplay.setDisplayView(plView);
    }


    @Override
    public String getLocale() {
        return "pt-PT";
    }
}
