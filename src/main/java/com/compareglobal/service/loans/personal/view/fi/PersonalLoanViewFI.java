package com.compareglobal.service.loans.personal.view.fi;

import com.compareglobal.service.common.domain.Filter;
import com.compareglobal.service.common.domain.Link;
import com.compareglobal.service.common.domain.LinkUrlHelper;
import com.compareglobal.service.common.domain.converter.EntityWithTypeToMap;
import com.compareglobal.service.common.utils.ListHelper;
import com.compareglobal.service.loans.personal.domain.DBKeys;
import com.compareglobal.service.loans.personal.domain.GeneralInfo;
import com.compareglobal.service.loans.personal.domain.PersonalLoan;
import com.compareglobal.service.loans.personal.domain.Promotion;
import com.compareglobal.service.loans.personal.domain.benefits.Benefit;
import com.compareglobal.service.loans.personal.domain.fees.Fee;
import com.compareglobal.service.loans.personal.domain.interestrates.Interest;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Luis Miguel Osorio.
 */
public class PersonalLoanViewFI {

    public static JSONObject createDisplayView(final PersonalLoan personalLoan, final MortgageDisplayFI payments) {
        final ImmutableMap.Builder<Object, Object> displayView = ImmutableMap.builder();

        final Map<String, Filter> personalLoanFilters = EntityWithTypeToMap.convert(personalLoan.getFilter());

        setPaymentRanges(displayView, payments);

        displayView.put("handlingFee", createHandlingFee(personalLoanFilters));


        JSONObject productInfo = new JSONObject();
        String productImageUrl = LinkUrlHelper.getImageValue(personalLoan.getImages(), DBKeys.ProductImage.toString());
        productInfo.put(DBKeys.ProductImage.toString(), productImageUrl);
        String productLink = LinkUrlHelper.getLinkValue(personalLoan.getLinks(), DBKeys.ProductLink.toString());

        if (StringUtils.isEmpty(productLink)) {
            productLink = LinkUrlHelper.getLinkValue(personalLoan.getLinks(), DBKeys.Link.toString());
        }

        productInfo.put("productLink", productLink);
        displayView.put("productInfo", productInfo);

        Filter exclusiveOffer = personalLoanFilters.get(DBKeys.OnlineOffer.toString());
        if ( hasActiveFilter(exclusiveOffer) ) {
            Promotion offerText = ListHelper.findItemByTypeT(personalLoan.getPromotions(), DBKeys.TypeValue.toString(), DBKeys.OnlineOfferText.toString());
            displayView.put("exclusive", offerText == null ? "" : offerText.getDescription());
        }

        JSONObject logo = new JSONObject();
        logo.put("icon", productImageUrl);
        logo.put("companyMobileLogo", productImageUrl);
        displayView.put("logo", logo);

        displayView.put("apply", getApplyInfo(personalLoan, personalLoanFilters, productLink));

        for (GeneralInfo generalInfo : personalLoan.getGeneralInfo()) {
            final String type = generalInfo.getTypeValue();
            if ( DBKeys.TotalTag.equals(type) ) {
                displayView.put("totalTag", generalInfo.getDescription());
            }

            if ( DBKeys.MonthlyRepaymentTag.equals(type) ) {
                displayView.put("monthlyRepaymentTag", generalInfo.getDescription());
            }
        }

        JSONObject promotions = new JSONObject();
        for (Promotion promotion : personalLoan.getPromotions()) {
            if (StringUtils.isNotEmpty(promotion.getTypeValue())) {
                promotions.put(promotion.getTypeValue(), promotion);
            }
        }

        Filter promoFlag = personalLoanFilters.get(DBKeys.PromoFlag.toString());
        JSONObject promo = new JSONObject();
        promo.put("hasPromo", hasFilter(promoFlag));
        promo.put("showPromoLogo", promoFlag == null);

        Promotion promoTag = ListHelper.findItemByTypeT(personalLoan.getPromotions(), DBKeys.TypeValue.toString(), DBKeys.PromoTag.toString());

        promo.put("showPromoTooltip", promoTag != null);
        displayView.put("promotions", promo);

        Fee repaymentFee = ListHelper.findItemByTypeT(personalLoan.getFeesList(), DBKeys.TypeValue.toString(), DBKeys.EarlyRepaymentFee.toString());
        JSONObject fees = new JSONObject();
        fees.put("earlyRepaymentFee", repaymentFee != null && repaymentFee.getAmount().compareTo(BigDecimal.ZERO) > 0);
        displayView.put("fees", fees);


        final Map<String, Object> interestRate = new HashMap<>();
        for (Interest interest : personalLoan.getInterestList()) {
            final String type = interest.getTypeValue();
            final String description = interest.getDescription();
            if ( DBKeys.InterestRate.isPrefixOf(type) && StringUtils.isNotEmpty(description) ) {
                interestRate.put(type, description);
            }
        }
        displayView.put("interests", interestRate);


        loadPropertiesFromCreditCardBenefits(personalLoan, displayView);

        return new JSONObject(displayView.build());
    }

    private static void setPaymentRanges(final ImmutableMap.Builder<Object, Object> displayView, final MortgageDisplayFI payments) {

        displayView.put("EARRange", getRangeFormat(payments.getEAR(), payments.getMaxEAR() + '%', payments.doubleMaxEAR()));

        displayView.put("totalRepaymentRange", getRangeFormat(
                String.valueOf(Math.round(payments.doubleTotalRepayment())),
                String.valueOf(Math.round(payments.doubleMaxTotalRepayment())),
                payments.doubleMaxTotalRepayment()));

        displayView.put("monthlyRepaymentRange", getRangeFormat(payments.getMonthlyRepayment(), payments.getMaxMonthlyRepayment(), payments.doubleMaxMonthlyRepayment()));
    }

    private static String getRangeFormat(final String fromValue, final String toValue, final Double maxValue) {
        if (maxValue == 0)
            return "";

        return String.format("%s - %s", fromValue, toValue);
    }

    private static void loadPropertiesFromCreditCardBenefits(final PersonalLoan personalLoan, final ImmutableMap.Builder<Object, Object> displayView ) {

        final Map<String, Object> benefits = new HashMap<>();

        final TreeMap<String, String> negativePoints = new TreeMap<>();
        final TreeMap<String, String> goodPoints = new TreeMap<>();

        for (Benefit benefit: personalLoan.getBenefitsList()) {
            final String type = benefit.getTypeValue();
            final String description = benefit.getDescription();

            if (DBKeys.KeyFeature.isPrefixOf(type) && StringUtils.isNotEmpty(description)) {
                goodPoints.put(type, description);
            }


            if (DBKeys.BeAware.isPrefixOf(type)) {
                negativePoints.put(type, description);
            }
        }

        if (!goodPoints.isEmpty()) {
            benefits.put("goodPoints", goodPoints.values());
        }

        if (!negativePoints.isEmpty()){
            benefits.put("negativePoints", negativePoints.values());
        }


        displayView.put("benefits", benefits);
    }

    private static JSONObject getApplyInfo(PersonalLoan personalLoan, Map<String, Filter> filterMap, String productLink) {
        JSONObject apply = new JSONObject();
        Filter applyButtonFlag = filterMap.get(DBKeys.ApplyButtonFlag.toString());
        boolean hasApplyButtonFlag = hasActiveFilter(applyButtonFlag);

        apply.put("hasApplyBtn", hasApplyButtonFlag);

        if ( hasApplyButtonFlag ) {
            apply.put("applyLink", productLink);
        }

        Link mobileApply = ListHelper.findItemByTypeT(personalLoan.getLinks(),DBKeys.TypeValue.toString(), DBKeys.MobileLink.toString());
        apply.put("mobileApplyLink", mobileApply == null ? "" : mobileApply.getUrl());

        return apply;
    }

    private static JSONObject createHandlingFee(final Map<String, Filter> personalLoanFilters) {
        final ImmutableMap.Builder<Object, Object> handlingFee = ImmutableMap.builder();
        final Filter handlingFeeWaiverLogo = personalLoanFilters.get(DBKeys.HandlingFeeWaiverLogo.toString());
        handlingFee.put("handlingFeeWaiverLogo", hasActiveFilter(handlingFeeWaiverLogo));
        handlingFee.put("hasNoHandlingFee", handlingFeeWaiverLogo == null);
        return new JSONObject(handlingFee.build());
    }

    private static boolean hasActiveFilter(final Filter filter) {
        return hasFilter(filter) &&  "1".equals(filter.getValue());
    }

    private static boolean hasFilter(final Filter filter) {
        return filter != null;
    }
}
