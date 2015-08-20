/*
 * Copyright (c) 2015.
 * Compare Asia Group
 */
package com.compareglobal.service.loans.personal.view;

import com.compareglobal.service.loans.personal.view.hk.PersonalLoanViewHK;
import org.json.simple.JSONObject;

/**
 * Created by dennis on 3/31/15.
 */
public class PersonalLoanView {
    private JSONObject productInfo;
    private JSONObject offers;
    private JSONObject logo;
    private JSONObject apply;
    private JSONObject handlingFee;
    private String[] benefits;
    private JSONObject promotions;
    private JSONObject fees;

    public JSONObject getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(JSONObject productInfo) {
        this.productInfo = productInfo;
    }

    public void setOffers(JSONObject offers) {
        this.offers = offers;
    }

    public JSONObject getOffers() {
        return offers;
    }

    public void setLogo(JSONObject logo) {
        this.logo = logo;
    }

    public JSONObject getLogo() {
        return logo;
    }

    public void setApply(JSONObject apply) {
        this.apply = apply;
    }

    public JSONObject getApply() {
        return apply;
    }

    public void setHandlingFee(JSONObject handlingFee) {
        this.handlingFee = handlingFee;
    }

    public JSONObject getHandlingFee() {
        return handlingFee;
    }

    public void setBenefits(String[] benefits) {
        this.benefits = benefits;
    }

    public String[] getBenefits() {
        return benefits;
    }

    public void setPromotions(JSONObject promotions) {
        this.promotions = promotions;
    }

    public JSONObject getPromotions() {
        return promotions;
    }

    public void setFees(JSONObject fees) {
        this.fees = fees;
    }

    public JSONObject getFees() {
        return fees;
    }
}
