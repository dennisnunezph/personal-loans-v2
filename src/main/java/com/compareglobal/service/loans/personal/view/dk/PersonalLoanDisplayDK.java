package com.compareglobal.service.loans.personal.view.dk;

import com.compareglobal.service.loans.personal.domain.PersonalLoan;
import com.compareglobal.service.loans.personal.domain.PersonalLoanPublic;
import org.json.simple.JSONObject;

/**
 * Created by dennis on 4/17/15.
 */
public class PersonalLoanDisplayDK extends PersonalLoanPublic {
    private JSONObject displayView;

    public PersonalLoanDisplayDK(PersonalLoan personalLoan) {
        super(personalLoan);
    }

    public JSONObject getDisplayView() {
        return displayView;
    }

    public void setDisplayView(JSONObject displayView) {
        this.displayView = displayView;
    }
}
