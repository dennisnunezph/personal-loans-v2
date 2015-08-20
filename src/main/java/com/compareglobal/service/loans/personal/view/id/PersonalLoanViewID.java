package com.compareglobal.service.loans.personal.view.id;


import com.compareglobal.service.common.domain.Filter;
import com.compareglobal.service.common.domain.Link;
import com.compareglobal.service.common.domain.LinkUrlHelper;
import com.compareglobal.service.common.domain.converter.EntityWithTypeToMap;
import com.compareglobal.service.common.utils.ListHelper;
import com.compareglobal.service.loans.personal.domain.GeneralInfo;
import com.compareglobal.service.loans.personal.domain.PersonalLoan;
import com.compareglobal.service.loans.personal.domain.Promotion;
import com.compareglobal.service.loans.personal.domain.benefits.Benefit;
import com.compareglobal.service.loans.personal.domain.fees.Fee;
import com.compareglobal.service.loans.personal.domain.interestrates.Interest;
import com.compareglobal.service.loans.personal.view.TransformView;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;

import java.math.BigDecimal;
import java.util.*;

public class PersonalLoanViewID implements TransformView {

    public enum ProductKeys {
        ProductImage("product_icon"),
        Link("link"),
        ProductLink("product_link"),
        TypeValue("typeValue"),
        OnlineOffer("hasExclusiveOnlineOffer"),
        OnlineOfferText("exclusiveOnlineOfferText"),
        ApplyButtonFlag("hasApplyBtn"),
        MobileLink("product_mobile_link"),
        HandlingFeeWaiverLogo("handlingfeewaiverlogo"),
        PromoFlag("hasPromo"),
        PromoTag("promoTag"),
        EarlyRepaymentFee("earlyRepaymentfee");

        private final String productKey;

        ProductKeys(String productKey) {
            this.productKey = productKey;
        }

        private String getProductKey() {
            return productKey;
        }
    }

    public enum ViewKeys {
        Apply("apply"),
        ApplyButton("hasApplyBtn"),
        ApplyLink("applyLink"),
        Benefits("benefits"),
        EarlyRepaymentFee("earlyRepaymentfee"),
        Exclusive("exclusive"),
        Fees("fees"),
        HandlingFee("handlingFee"),
        HandlingFeeFlag("hasNoHandlingFee"),
        Icon("icon"),
        Logo("logo"),
        MobileApplyLink("mobileApplyLink"),
        MobileLogo("companyMobileLogo"),
        ProductLink("productLink"),
        ProductImage("productImage"),
        ProductInfo("productInfo"),
        PromoFlag("hasPromo"),
        Promotions("promotions"),
        ShowPromo("showPromoLogo"),
        ShowPromoTooltip("showPromoTooltip"),
        AprBasis("aprBasis"),
        TotalTag("totalTag"),
        MonthlyRepaymentTag("monhtlyRepaymentTag");

        private final String viewKey;

        ViewKeys(String viewKey) {
            this.viewKey = viewKey;
        }

        public String getViewKey() {
            return viewKey;
        }

        public boolean equals(String key) {
            return this.viewKey.equals(key);
        }
    }

    @Override
    public JSONObject view(PersonalLoan personalLoan) {
        JSONObject displayView = new JSONObject();

        Map<String, Filter> filterMap = EntityWithTypeToMap.convert(personalLoan.getFilter());

        JSONObject productInfo = new JSONObject();
        String productImageUrl = LinkUrlHelper.getImageValue(personalLoan.getImages(), ProductKeys.ProductImage.getProductKey());
        productInfo.put(ViewKeys.ProductImage.getViewKey(), productImageUrl);
        String productLink = LinkUrlHelper.getLinkValue(personalLoan.getLinks(), ProductKeys.ProductLink.getProductKey());

        if (StringUtils.isEmpty(productLink)) {
            productLink = LinkUrlHelper.getLinkValue(personalLoan.getLinks(), ProductKeys.Link.getProductKey());
        }

        productInfo.put(ViewKeys.ProductLink.getViewKey(), productLink);
        displayView.put(ViewKeys.ProductInfo.getViewKey(), productInfo);

        DescValue descValue = new DescValue();
        Filter exclusiveOffer = filterMap.get(ProductKeys.OnlineOffer.getProductKey());
        if ( hasActiveFilter(exclusiveOffer) ) {
            Promotion offerText = ListHelper.findItemByTypeT(personalLoan.getPromotions(), ProductKeys.TypeValue.getProductKey(), ProductKeys.OnlineOfferText.getProductKey());
            descValue.setDescription(offerText == null ? "" : offerText.getDescription());
            displayView.put(ViewKeys.Exclusive.getViewKey(), descValue);
        }

        JSONObject logo = new JSONObject();
        logo.put(ViewKeys.Icon.getViewKey(), productImageUrl);
        logo.put(ViewKeys.MobileLogo.getViewKey(), productImageUrl);
        displayView.put(ViewKeys.Logo.getViewKey(), logo);

        displayView.put(ViewKeys.Apply.getViewKey(), getApplyInfo(personalLoan, filterMap, productLink));

        JSONObject handlingFee = new JSONObject();
        Filter handlingFeeWaiverLogo = filterMap.get(ProductKeys.HandlingFeeWaiverLogo.getProductKey());
        handlingFee.put(ProductKeys.HandlingFeeWaiverLogo.getProductKey(), hasActiveFilter(handlingFeeWaiverLogo));
        handlingFee.put(ViewKeys.HandlingFeeFlag.getViewKey(), handlingFeeWaiverLogo == null);
        displayView.put(ViewKeys.HandlingFee.getViewKey(), handlingFee);

        JSONObject promotions = new JSONObject();
        for (Promotion promotion : personalLoan.getPromotions()) {
            if (StringUtils.isNotEmpty(promotion.getTypeValue())) {
                promotions.put(promotion.getTypeValue(), promotion);
            }
        }

        Filter promoFlag = filterMap.get(ProductKeys.PromoFlag.getProductKey());
        JSONObject promo = new JSONObject();
        promo.put(ViewKeys.PromoFlag.getViewKey(), hasFilter(promoFlag));
        promo.put(ViewKeys.ShowPromo.getViewKey(), promoFlag == null);

        Promotion promoTag = ListHelper.findItemByTypeT(personalLoan.getPromotions(), ProductKeys.TypeValue.getProductKey(), ProductKeys.PromoTag.getProductKey());

        promo.put(ViewKeys.ShowPromoTooltip.getViewKey(), promoTag != null);
        displayView.put(ViewKeys.Promotions.getViewKey(), promo);

        Fee repaymentFee = ListHelper.findItemByTypeT(personalLoan.getFeesList(), ProductKeys.TypeValue.getProductKey(), ProductKeys.EarlyRepaymentFee.getProductKey());
        JSONObject fees = new JSONObject();
        fees.put(ViewKeys.EarlyRepaymentFee.getViewKey(), repaymentFee != null && repaymentFee.getAmount().compareTo(BigDecimal.ZERO) > 0);
        displayView.put(ViewKeys.Fees.getViewKey(), fees);

        Map<Integer, String> orderedList = new TreeMap<>();
        for (Benefit benefit : personalLoan.getBenefitsList()) {
            String description= benefit.getDescription();
            try {
                if (StringUtils.isNotEmpty(description) && benefit.getTypeValue().startsWith("keyFeature"))
                    orderedList.put(Integer.parseInt(benefit.getTypeValue().substring(10).trim()),description);
            }
            catch (Exception e){}
        }

        if (orderedList != null
                && orderedList.size() > 0) {
            displayView.put(ViewKeys.Benefits.getViewKey(), orderedList.values());
        }

        extractDisplayDataFromInterest(displayView, personalLoan);

        extractDisplayDataFromGeneralInfo(displayView, personalLoan);

        return displayView;
    }

    private void extractDisplayDataFromGeneralInfo(JSONObject displayView, PersonalLoan personalLoan) {
        for (GeneralInfo generalInfo : personalLoan.getGeneralInfo()) {

            if ( ViewKeys.TotalTag.equals(generalInfo.getTypeValue()) ) {
                displayView.put(ViewKeys.TotalTag, generalInfo.getDescription());
            }

            if ( ViewKeys.MonthlyRepaymentTag.equals(generalInfo.getTypeValue()) ) {
                displayView.put(ViewKeys.MonthlyRepaymentTag, generalInfo.getDescription());
            }
        }
    }

    private void extractDisplayDataFromInterest(JSONObject displayView, PersonalLoan personalLoan) {
        for (Interest interest : personalLoan.getInterestList()) {
            if ( ViewKeys.AprBasis.equals(interest.getTypeValue()) ) {
                displayView.put(ViewKeys.AprBasis, interest.getDescription());
            }
        }
    }

    private JSONObject getApplyInfo(PersonalLoan personalLoan, Map<String, Filter> filterMap, String productLink) {
        JSONObject apply = new JSONObject();
        Filter applyButtonFlag = filterMap.get(ProductKeys.ApplyButtonFlag.getProductKey());
        boolean hasApplyButtonFlag = hasActiveFilter(applyButtonFlag);

        apply.put(ViewKeys.ApplyButton.getViewKey(), hasApplyButtonFlag);

        if ( hasApplyButtonFlag ) {
            apply.put(ViewKeys.ApplyLink.getViewKey(), productLink);
        }

        Link mobileApply = ListHelper.findItemByTypeT(personalLoan.getLinks(),ProductKeys.TypeValue.getProductKey(), ProductKeys.MobileLink.getProductKey());
        apply.put(ViewKeys.MobileApplyLink.getViewKey(), mobileApply == null ? "" : mobileApply.getUrl());

        return apply;
    }

    public static class DescValue {
        private String description;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

    }

    private boolean hasActiveFilter(Filter filter) {
        return hasFilter(filter) &&  "1".equals(filter.getValue());
    }

    private boolean hasInactiveFilter(Filter filter) {
        return hasFilter(filter) &&  "0".equals(filter.getValue());
    }

    private boolean hasFilter(Filter filter) {
        return filter != null;
    }
}
