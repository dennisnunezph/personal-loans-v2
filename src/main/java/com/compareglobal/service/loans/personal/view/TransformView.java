package com.compareglobal.service.loans.personal.view;

import com.compareglobal.service.loans.personal.domain.PersonalLoan;
import org.json.simple.JSONObject;

/**
 * Created by dennis on 3/31/15.
 */
public interface TransformView {
    JSONObject view(PersonalLoan personalLoan);
}
