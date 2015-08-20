package com.compareglobal.service.loans.personal.view.xx;


import com.compareglobal.service.common.domain.Filter;
import com.compareglobal.service.common.domain.Link;
import com.compareglobal.service.common.domain.LinkUrlHelper;
import com.compareglobal.service.common.domain.converter.EntityWithTypeToMap;
import com.compareglobal.service.common.utils.ListHelper;
import com.compareglobal.service.loans.personal.domain.PersonalLoan;
import com.compareglobal.service.loans.personal.domain.Promotion;
import com.compareglobal.service.loans.personal.domain.benefits.Benefit;
import com.compareglobal.service.loans.personal.domain.fees.Fee;
import com.compareglobal.service.loans.personal.view.TransformView;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PersonalLoanViewXX implements TransformView {

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
        ShowPromoTooltip("showPromoTooltip");
        private final String viewKey;

        ViewKeys(String viewKey) {
            this.viewKey = viewKey;
        }

        public String getViewKey() {
            return viewKey;
        }
    }

    @Override
    public JSONObject view(PersonalLoan personalLoan) {
        JSONObject displayView = new JSONObject();

        Map<String, Filter> filterMap = EntityWithTypeToMap.convert(personalLoan.getFilter());

        JSONObject productInfo = new JSONObject();
        String productImageUrl = LinkUrlHelper.getImageValue(personalLoan.getImages(),
                ProductKeys.ProductImage.getProductKey());
        productInfo.put(ViewKeys.ProductImage.getViewKey(), productImageUrl);
        String productLink = LinkUrlHelper.getLinkValue(personalLoan.getLinks(),
                ProductKeys.ProductLink.getProductKey());
        if (StringUtils.isEmpty(productLink)) {
            productLink = LinkUrlHelper.getLinkValue(personalLoan.getLinks(),
                    ProductKeys.Link.getProductKey());
        }
        productInfo.put(ViewKeys.ProductLink.getViewKey(), productLink);

        displayView.put(ViewKeys.ProductInfo.getViewKey(), productInfo);

        DescValue descValue = new DescValue();
        Filter exclusiveOffer = filterMap.get(ProductKeys.OnlineOffer.getProductKey());
        if (exclusiveOffer != null
                && "1".equals(exclusiveOffer.getValue())) {
            Promotion offerText = ListHelper.findItemByTypeT(personalLoan.getPromotions(),
                    ProductKeys.TypeValue.getProductKey(),
                    ProductKeys.OnlineOfferText.getProductKey());
            descValue.setDescription(offerText == null ? "" : offerText.getDescription());
            displayView.put(ViewKeys.Exclusive.getViewKey(), descValue);
        }

        JSONObject logo = new JSONObject();
        logo.put(ViewKeys.Icon.getViewKey(), productImageUrl);
        logo.put(ViewKeys.MobileLogo.getViewKey(), productImageUrl);
        displayView.put(ViewKeys.Logo.getViewKey(), logo);

        displayView.put(ViewKeys.Apply.getViewKey(), getApplyInfo(personalLoan, filterMap, productLink));

        JSONObject handlingFee = new JSONObject();
        Filter feewaivingLogo = filterMap.get(ProductKeys.HandlingFeeWaiverLogo.getProductKey());
        handlingFee.put(ProductKeys.HandlingFeeWaiverLogo.getProductKey(),
                feewaivingLogo != null && "1".equals(feewaivingLogo.getValue()));
        handlingFee.put(ViewKeys.HandlingFeeFlag.getViewKey(), feewaivingLogo == null);
        displayView.put(ViewKeys.HandlingFee.getViewKey(), handlingFee);

        JSONObject promotions = new JSONObject();
        for (Promotion promotion : personalLoan.getPromotions()) {
            if (StringUtils.isNotEmpty(promotion.getTypeValue())) {
                promotions.put(promotion.getTypeValue(), promotion);
            }
        }

        Filter hasPromoFilter = filterMap.get(ProductKeys.PromoFlag.getProductKey());
        JSONObject promo = new JSONObject();
        promo.put(ViewKeys.PromoFlag.getViewKey(), hasPromoFilter != null);
        promo.put(ViewKeys.ShowPromo.getViewKey(), hasPromoFilter == null);
        Promotion promoTag = ListHelper.findItemByTypeT(personalLoan.getPromotions(),
                ProductKeys.TypeValue.getProductKey(),
                ProductKeys.PromoTag.getProductKey());
        promo.put(ViewKeys.ShowPromoTooltip.getViewKey(), promoTag != null);
        displayView.put(ViewKeys.Promotions.getViewKey(), promo);

        Fee repaymentFee = ListHelper.findItemByTypeT(personalLoan.getFeesList(),
                ProductKeys.TypeValue.getProductKey(),
                ProductKeys.EarlyRepaymentFee.getProductKey());
        JSONObject fees = new JSONObject();
        fees.put(ViewKeys.EarlyRepaymentFee.getViewKey(), repaymentFee != null
                && repaymentFee.getAmount().compareTo(BigDecimal.ZERO) > 0);
        displayView.put(ViewKeys.Fees.getViewKey(), fees);

        List<String> benefitList = new ArrayList<>();
        for (Benefit benefit : personalLoan.getBenefitsList()) {
            if (StringUtils.isNotEmpty(benefit.getDescription())) {
                benefitList.add(benefit.getDescription());
            }
        }

        if (CollectionUtils.isNotEmpty(benefitList)) {
            String[] benefitsArr = new String[benefitList.size()];
            displayView.put(ViewKeys.Benefits.getViewKey(), benefitList.toArray(benefitsArr));
        }

        return displayView;
    }

    private JSONObject getApplyInfo(PersonalLoan personalLoan,
                                    Map<String, Filter> filterMap,
                                    String productLink) {
        JSONObject apply = new JSONObject();
        Filter applyButton = filterMap.get(ProductKeys.ApplyButtonFlag.getProductKey());
        boolean hasApplyButton = applyButton != null
                &&  "1".equals(applyButton.getValue());
        apply.put(ViewKeys.ApplyButton.getViewKey(), hasApplyButton);
        if (hasApplyButton) {
            apply.put(ViewKeys.ApplyLink.getViewKey(), productLink);
        }
        Link mobileApply = ListHelper.findItemByTypeT(personalLoan.getLinks(),
                ProductKeys.TypeValue.getProductKey(), ProductKeys.MobileLink.getProductKey());
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
}
