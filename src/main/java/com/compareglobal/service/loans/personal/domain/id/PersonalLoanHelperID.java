package com.compareglobal.service.loans.personal.domain.id;

import com.compareglobal.service.common.domain.CompanyInfoHelper;
import com.compareglobal.service.common.domain.Item;
import com.compareglobal.service.common.domain.LinkUrlHelper;
import com.compareglobal.service.common.domain.converter.TypeValueHelper;
import com.compareglobal.service.loans.personal.domain.PersonalLoan;
import com.compareglobal.service.loans.personal.domain.PersonalLoanHelper;
import com.compareglobal.service.loans.personal.domain.PersonalLoanPublic;
import com.compareglobal.service.loans.personal.domain.converter.PromotionHelper;
import com.compareglobal.service.loans.personal.view.PersonalLoanDisplay;
import com.compareglobal.service.loans.personal.view.id.MortgageDisplayID;
import com.compareglobal.service.loans.personal.view.id.MortgageID;
import com.compareglobal.service.loans.personal.view.id.PersonalLoanViewID;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;

@Component("personalLoanHelperID")
public class PersonalLoanHelperID implements PersonalLoanHelper {

    public enum PLKeys {
        ProductImage("product_icon"),
        ProductLink("product_link");

        private final String plKey;

        PLKeys(String plKey) {
            this.plKey = plKey;
        }

        private String getPLKey() {
            return plKey;
        }
    }


    private final MonthlyPaymentHelperID monthlyPaymentHelperID;

    @Inject
    public PersonalLoanHelperID(MonthlyPaymentHelperID monthlyPaymentHelperID) {
        this.monthlyPaymentHelperID = monthlyPaymentHelperID;
    }

    public PersonalLoanPublic build(PersonalLoan personalLoan, double loanAmount, int loanTenure) {

        PersonalLoanDisplay personalLoanDisplay = new PersonalLoanDisplay(personalLoan);
        personalLoanDisplay.setId(personalLoan.getId());

        // set payments
        MortgageID mortgage = monthlyPaymentHelperID.populateMortgage(loanAmount, loanTenure, personalLoan);
        MortgageDisplayID mortgageDisplay = new MortgageDisplayID(mortgage);
        personalLoanDisplay.setPayments(mortgageDisplay);

        // set filters
        if (personalLoan.getFilter() != null) {
            List<Item> filterListOutput = TypeValueHelper.getParsedItem(personalLoan.getFilter());
            personalLoanDisplay.setFilters(filterListOutput);
        }

        // set ranking
        if (personalLoan.getRankings() != null) {
            List<Item> rankListOutput = TypeValueHelper.getParsedItem(personalLoan.getRankings());
            personalLoanDisplay.setRanking(rankListOutput);
        }

        // set featured
        if (personalLoan.getFeatures() != null) {
            List<Item> featuredItemList = TypeValueHelper.getParsedItem(personalLoan.getFeatures());
            personalLoanDisplay.setFeatured(featuredItemList);
        }

        // set company
        CompanyInfoHelper companyInfoHelper = new CompanyInfoHelper(personalLoan.getProvider());
        personalLoanDisplay.setCompany(companyInfoHelper.getCompanyInfo());

        // set promotions
        personalLoanDisplay.setPromotions(PromotionHelper.convertToItem(personalLoan.getPromotions()));

        // set product
        JSONObject productInfo = new JSONObject();
        productInfo.put("productImage", LinkUrlHelper.getImageValue(personalLoan.getImages(),PLKeys.ProductImage.getPLKey()));
        productInfo.put("productLink", LinkUrlHelper.getLinkValue(personalLoan.getLinks(),PLKeys.ProductLink.getPLKey()));
        personalLoanDisplay.setProduct(productInfo);

        // set displayView
        PersonalLoanViewID personalLoanViewID = new PersonalLoanViewID();
        personalLoanDisplay.setDisplayView(personalLoanViewID.view(personalLoan));

        return personalLoanDisplay;
    }

    @Override
    public String getLocale() {
        return "en-ID";
    }
}
