/*
 * Copyright (c) 2015.
 * Compare Asia Group
 */
package com.compareglobal.service.loans.personal.view;

import com.compareglobal.service.loans.personal.domain.PersonalLoan;
import com.compareglobal.service.loans.personal.domain.PersonalLoanPublic;
import org.json.simple.JSONObject;

/**
 * Created by dennis on 3/31/15.
 */
public class PersonalLoanDisplay extends PersonalLoanPublic {

    private JSONObject offers;
    private JSONObject displayView;

    public PersonalLoanDisplay(PersonalLoan personalLoan) {
        super(personalLoan);
    }

    public JSONObject getDisplayView() {
        return displayView;
    }

    public void setDisplayView(JSONObject displayView) {
        this.displayView = displayView;
    }

    public JSONObject getOffers() {
        return offers;
    }

    public void setOffers(JSONObject offers) {
        this.offers = offers;
    }

}
