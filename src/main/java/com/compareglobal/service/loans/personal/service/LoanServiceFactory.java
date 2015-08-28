/*
 * Copyright (c) 2015.
 * Compare Asia Group
 */
package com.compareglobal.service.loans.personal.service;

import com.compareglobal.service.loans.personal.domain.MonthlyPaymentHelper;
import com.compareglobal.service.loans.personal.domain.PersonalLoanHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Created by dennis on 2/17/15.
 */
@Component
public class LoanServiceFactory {

    @Autowired
    private ApplicationContext context;

    public final LoanService getInstance(final String locale) {
        final String loanServiceBeanName = getLoanServiceBeanName(locale);

        if (!context.containsBean(loanServiceBeanName)) {
            throw new IllegalStateException(loanServiceBeanName + " bean not implemented");
        }

        return (LoanService) context.getBean(loanServiceBeanName);
    }

    public final MonthlyPaymentHelper getLoanComputationInstance(final String locale) {
        final String serviceBeanName = getLoanServiceBeanName(locale);
        if (!context.containsBean(serviceBeanName)) {
            throw new IllegalStateException(serviceBeanName + " bean not implemented");
        }
        return (MonthlyPaymentHelper) context.getBean(serviceBeanName);
    }

    private String getLoanServiceBeanName(final String locale) {
        return getBeanName(locale, "monthlyPaymentHelper" );
    }

    private String getBeanName(final String locale, final String service) {
        return service + getCountryFromLocale(locale);
    }


    private String getCountryFromLocale(final String locale) {
        return locale.substring(3);
    }
}
