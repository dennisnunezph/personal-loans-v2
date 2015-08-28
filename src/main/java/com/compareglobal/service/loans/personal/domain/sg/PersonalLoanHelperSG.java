/*
 * Copyright (c) 2015.
 * Compare Asia Group
 */
package com.compareglobal.service.loans.personal.domain.sg;

import com.compareglobal.service.loans.personal.domain.*;
import com.compareglobal.service.loans.personal.view.PersonalLoanDisplay;
import com.compareglobal.service.loans.personal.view.sg.MortgageDisplaySG;
import com.compareglobal.service.loans.personal.view.sg.PersonalLoanViewSG;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by dennis on 4/22/15.
 */
@Component("personalLoanHelperSG")
public class PersonalLoanHelperSG implements PersonalLoanHelper {


    private final MonthlyPaymentHelper monthlyPaymentHelper;

    @Inject
    public PersonalLoanHelperSG(@Named("monthlyPaymentHelperSG") MonthlyPaymentHelper monthlyPaymentHelper) {
        this.monthlyPaymentHelper = monthlyPaymentHelper;
    }

    public PersonalLoanPublic build(PersonalLoan personalLoan, double loanAmount, int tenure) {

        PersonalLoanPublic loanPublic = new PersonalLoanDisplay(personalLoan);

        loanPublic.setId(personalLoan.getId());

        Mortgage mortgage = monthlyPaymentHelper.computeMortgage(loanAmount, tenure, personalLoan);
        MortgageDisplaySG mortgageDisplay = new MortgageDisplaySG(mortgage);
        loanPublic.setPayments(mortgageDisplay);

        update(personalLoan, loanPublic);

        return loanPublic;
    }

    private void update(PersonalLoan personalLoan,
                        PersonalLoanPublic loanPublic) {
        PersonalLoanViewSG viewSG = new PersonalLoanViewSG();
        JSONObject plView = viewSG.view(personalLoan);
        PersonalLoanDisplay loanDisplay = (PersonalLoanDisplay) loanPublic;
        loanDisplay.setDisplayView(plView);
    }


    @Override
    public String getLocale() {
        return "en-SG";
    }
}