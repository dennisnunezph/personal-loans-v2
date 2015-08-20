package com.compareglobal.service.loans.personal.validator;

import com.compareglobal.service.loans.personal.domain.Compare;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by nova-pc on 12/1/14.
 */
public class CompareValidator implements Validator {
    @Override
    public boolean supports(Class clazz) {
        return Compare.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Compare compare = (Compare) target;

        if(compare.getLoanTenure() == null) {
            errors.rejectValue("loanTenure", "loan tenure[emptyTenure]");
        }

        if(compare.getLoanAmount() == null) {
            errors.rejectValue("loanAmount", "loan amount[emptyAmount]");
        }

    }
}
