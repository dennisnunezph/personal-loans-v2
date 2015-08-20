package com.compareglobal.service.loans.personal.view.dk;

import com.compareglobal.service.common.domain.Filter;
import com.compareglobal.service.common.domain.Link;
import com.compareglobal.service.common.domain.LinkUrlHelper;
import com.compareglobal.service.common.domain.converter.EntityWithTypeToMap;
import com.compareglobal.service.common.utils.ListHelper;
import com.compareglobal.service.loans.personal.domain.GeneralInfo;
import com.compareglobal.service.loans.personal.domain.PersonalLoan;
import com.compareglobal.service.loans.personal.domain.Promotion;
import com.compareglobal.service.loans.personal.domain.benefits.Benefit;
import com.compareglobal.service.loans.personal.view.TransformView;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by dennis on 4/17/15.
 */
public class PersonalLoanViewDK implements TransformView {

    public enum ProductKeys {
        ProductImage("logo"),
        ProductLink("link"),
        TypeValue("typeValue"),
        OnlineOffer("hasExclusiveOnlineOffer"),
        OnlineOfferText("exclusiveOnlineOfferText"),
        ApplyButtonFlag("hasApplyBtn"),
        MobileLink("product_mobile_link");

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
        GoodPoints("goodPoints"),
        NegativePoints("negativePoints"),
        Exclusive("exclusive"),
        Icon("icon"),
        Logo("logo"),
        MobileApplyLink("mobileApplyLink"),
        MobileLogo("companyMobileLogo"),
        ProductLink("productLink"),
        ProductImage("productImage"),
        ProductInfo("productInfo"),
        Requirement("requirement");
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

        JSONObject promotions = new JSONObject();
        for (Promotion promotion : personalLoan.getPromotions()) {
            if (StringUtils.isNotEmpty(promotion.getTypeValue())) {
                promotions.put(promotion.getTypeValue(), promotion);
            }
        }

        JSONObject benefits = new JSONObject();
        benefits.put(ViewKeys.GoodPoints.getViewKey(),
                getBenefitsList(personalLoan.getBenefitsList()));
        benefits.put(ViewKeys.NegativePoints.getViewKey(),
                getNegativePoints(personalLoan.getGeneralInfo()));

        displayView.put(ViewKeys.Benefits.getViewKey(), benefits);

        return displayView;
    }

    private String[] getBenefitsList(Set<Benefit> benefits) {
        List<String> benefitList = new ArrayList<>();
        for (Benefit benefit : benefits) {
            if (StringUtils.isNotBlank(benefit.getDescription())) {
                benefitList.add(benefit.getDescription());
            }
        }

        if (CollectionUtils.isNotEmpty(benefitList)) {
            String[] benefitsArr = new String[benefitList.size()];
            return benefitList.toArray(benefitsArr);
        }
        return null;
    }

    private String[] getNegativePoints(Set<GeneralInfo> generalInfoSet) {
        List<String> negativePoint = new ArrayList<>();
        for (GeneralInfo generalInfo : generalInfoSet) {
           if (generalInfo.getTypeValue().startsWith(ViewKeys.Requirement.getViewKey())
                   && StringUtils.isNotBlank(generalInfo.getDescription())) {
               negativePoint.add(generalInfo.getDescription());
           }
        }
        if (CollectionUtils.isNotEmpty(generalInfoSet)) {
            String[] negativePointsArr = new String[negativePoint.size()];
            return negativePoint.toArray(negativePointsArr);
        }
        return null;
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
