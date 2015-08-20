package com.compareglobal.service.loans.personal.domain.fi;

import com.compareglobal.service.common.domain.CompanyInfoHelper;
import com.compareglobal.service.common.domain.Item;
import com.compareglobal.service.common.domain.LinkUrlHelper;
import com.compareglobal.service.common.domain.converter.TypeValueHelper;
import com.compareglobal.service.loans.personal.domain.DBKeys;
import com.compareglobal.service.loans.personal.domain.PersonalLoan;
import com.compareglobal.service.loans.personal.domain.PersonalLoanHelper;
import com.compareglobal.service.loans.personal.domain.PersonalLoanPublic;
import com.compareglobal.service.loans.personal.domain.converter.PromotionHelper;
import com.compareglobal.service.loans.personal.view.PersonalLoanDisplay;
import com.compareglobal.service.loans.personal.view.fi.MortgageDisplayFI;
import com.compareglobal.service.loans.personal.view.fi.PersonalLoanViewFI;
import com.google.common.collect.ImmutableMap;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Luis Miguel Osorio.
 */
@Component("personalLoanHelperFI")
public class PersonalLoanHelperFI implements PersonalLoanHelper {

    @Override
    public PersonalLoanPublic build(final PersonalLoan personalLoan, final double loanAmount, final int loanTenure) {

        PersonalLoanDisplay personalLoanDisplay = new PersonalLoanDisplay(personalLoan);

        personalLoanDisplay.setId(personalLoan.getId());

        // set payments
        final MortgageDisplayFI mortgageDisplay = MortgageHelperFI.createMortgageDisplay(loanAmount, loanTenure, personalLoan);
        personalLoanDisplay.setPayments(mortgageDisplay);

        // set filters
        if (personalLoan.getFilter() != null) {
            final List<Item> filterListOutput = TypeValueHelper.getParsedItem(personalLoan.getFilter());
            personalLoanDisplay.setFilters(filterListOutput);
        }

        // set ranking
        if (personalLoan.getRankings() != null) {
            final List<Item> rankListOutput = TypeValueHelper.getParsedItem(personalLoan.getRankings());
            personalLoanDisplay.setRanking(rankListOutput);
        }

        // set featured
        if (personalLoan.getFeatures() != null) {
            final List<Item> featuredItemList = TypeValueHelper.getParsedItem(personalLoan.getFeatures());
            personalLoanDisplay.setFeatured(featuredItemList);
        }

        // set company
        final CompanyInfoHelper companyInfoHelper = new CompanyInfoHelper(personalLoan.getProvider());
        personalLoanDisplay.setCompany(companyInfoHelper.getCompanyInfo());

        // set promotions
        personalLoanDisplay.setPromotions(PromotionHelper.convertToItem(personalLoan.getPromotions()));

        // set product
        personalLoanDisplay.setProduct(getProductInfo(personalLoan));

        // set displayView
        final JSONObject displayView = PersonalLoanViewFI.createDisplayView(personalLoan, mortgageDisplay);
        personalLoanDisplay.setDisplayView(displayView);

        return personalLoanDisplay;
    }

    private JSONObject getProductInfo(final PersonalLoan personalLoan) {
        return new JSONObject(ImmutableMap.of(
                "productImage", LinkUrlHelper.getImageValue(personalLoan.getImages(), DBKeys.ProductImage.toString()),
                "productLink", LinkUrlHelper.getLinkValue(personalLoan.getLinks(), DBKeys.ProductLink.toString())
        ));
    }

    @Override
    public String getLocale() {
        return "fi-FI";
    }
}
