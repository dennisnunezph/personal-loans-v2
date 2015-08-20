/*
 * Copyright (c) 2015.
 * Compare Asia Group
 */
package com.compareglobal.service.loans.personal.domain.hk;

import com.compareglobal.service.common.domain.*;
import com.compareglobal.service.common.domain.converter.TypeValueHelper;
import com.compareglobal.service.common.loans.personal.domain.MortgageDisplay;
import com.compareglobal.service.common.utils.ListHelper;
import com.compareglobal.service.loans.personal.domain.CatInfo;
import com.compareglobal.service.loans.personal.domain.PersonalLoan;
import com.compareglobal.service.loans.personal.domain.PersonalLoanHelper;
import com.compareglobal.service.loans.personal.domain.PersonalLoanPublic;
import com.compareglobal.service.loans.personal.domain.converter.PromotionHelper;
import com.compareglobal.service.loans.personal.domain.interestrates.Interest;
import com.compareglobal.service.loans.personal.view.PersonalLoanDisplay;
import com.compareglobal.service.loans.personal.view.hk.MortgageDisplayHK;
import com.compareglobal.service.loans.personal.view.hk.MortgageHK;
import com.compareglobal.service.loans.personal.view.hk.PersonalLoanViewHK;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * Created by dennis on 1/21/15.
 */
@Component("personalLoanHelperHK")
public class PersonalLoanHelperHK implements  PersonalLoanHelper {

    public enum PLKeys {
        ProductImage("product_icon"),
        ProductLink("product_link"),
        TypeValue("typeValue"),
        APRBasis("aprBasis");;

        private final String plKey;

        PLKeys(String plKey) {
            this.plKey = plKey;
        }

        private String getPLKey() {
            return plKey;
        }
    }


    private final MonthlyPaymentHelperHK monthlyPaymentHelperHK;

    @Inject
    public PersonalLoanHelperHK(MonthlyPaymentHelperHK monthlyPaymentHelperHK) {
        this.monthlyPaymentHelperHK = monthlyPaymentHelperHK;
    }

    public PersonalLoanPublic build(PersonalLoan personalLoan, double loanAmount, int tenure) {

        PersonalLoanPublic loanPublic = new PersonalLoanDisplay(personalLoan);

        loanPublic.setId(personalLoan.getId());

        MortgageHK mortgage = monthlyPaymentHelperHK.populateMortgage(loanAmount, tenure, personalLoan);
        MortgageDisplayHK mortgageDisplay = new MortgageDisplayHK(mortgage);
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
        PersonalLoanViewHK viewHK = new PersonalLoanViewHK();
        JSONObject plView = viewHK.view(personalLoan);
        setAPRTooltip(personalLoan.getInterestList(), loanPublic.getPayments(), plView);
        PersonalLoanDisplay loanDisplayHK = (PersonalLoanDisplay) loanPublic;
        loanDisplayHK.setDisplayView(plView);
    }

    private void setAPRTooltip(Set<Interest> interestList, MortgageDisplay mortgageDisplay, JSONObject plView){
        MortgageDisplayHK mortgage = (MortgageDisplayHK) mortgageDisplay;
        Interest interest = ListHelper.findItemByTypeT(interestList,
                PLKeys.TypeValue.getPLKey(),
                PLKeys.APRBasis.getPLKey());
        CatInfo catInfo = new CatInfo();
        if (interest!= null) {
            catInfo.setType(interest.getTypeValue());
            catInfo.setValue(new BigDecimal(mortgageDisplay.getApr()));
            String aprDetails = (mortgage.getInterestProfile() == 0) ? interest.getDescription() : mortgage.getAprBasis();
            catInfo.setDetails(aprDetails);
        }
        plView.put(PLKeys.APRBasis.getPLKey(), catInfo);

    }

    @Override
    public String getLocale() {
        return "en-HK";
    }
}
