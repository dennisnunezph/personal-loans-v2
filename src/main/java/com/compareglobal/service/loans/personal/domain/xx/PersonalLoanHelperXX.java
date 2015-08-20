package com.compareglobal.service.loans.personal.domain.xx;

import com.compareglobal.service.common.domain.CompanyInfoHelper;
import com.compareglobal.service.common.domain.Item;
import com.compareglobal.service.common.domain.LinkUrlHelper;
import com.compareglobal.service.common.domain.converter.TypeValueHelper;
import com.compareglobal.service.loans.personal.domain.PersonalLoan;
import com.compareglobal.service.loans.personal.domain.PersonalLoanHelper;
import com.compareglobal.service.loans.personal.domain.PersonalLoanPublic;
import com.compareglobal.service.loans.personal.domain.converter.PromotionHelper;
import com.compareglobal.service.loans.personal.domain.xx.MonthlyPaymentHelperXX;
import com.compareglobal.service.loans.personal.view.PersonalLoanDisplay;
import com.compareglobal.service.loans.personal.view.xx.MortgageDisplayXX;
import com.compareglobal.service.loans.personal.view.xx.MortgageXX;
import com.compareglobal.service.loans.personal.view.xx.PersonalLoanViewXX;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;

@Component("personalLoanHelperXX")
public class PersonalLoanHelperXX implements PersonalLoanHelper {

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


    private final MonthlyPaymentHelperXX monthlyPaymentHelperXX;

    @Inject
    public PersonalLoanHelperXX(MonthlyPaymentHelperXX monthlyPaymentHelperXX) {
        this.monthlyPaymentHelperXX = monthlyPaymentHelperXX;
    }

    public PersonalLoanPublic build(PersonalLoan personalLoan, double loanAmount, int tenure) {

        PersonalLoanPublic loanPublic = new PersonalLoanDisplay(personalLoan);

        loanPublic.setId(personalLoan.getId());

        MortgageXX mortgage = monthlyPaymentHelperXX.populateMortgage(loanAmount, tenure, personalLoan);
        MortgageDisplayXX mortgageDisplay = new MortgageDisplayXX(mortgage);
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
        PersonalLoanViewXX viewXX = new PersonalLoanViewXX();
        JSONObject plView = viewXX.view(personalLoan);
        PersonalLoanDisplay loanDisplayXX = (PersonalLoanDisplay) loanPublic;
        loanDisplayXX.setDisplayView(plView);
    }


    @Override
    public String getLocale() {
        return "en-XX";
    }
}
