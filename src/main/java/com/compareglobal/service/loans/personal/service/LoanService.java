/*
 * Copyright (c) 2015.
 * Compare Asia Group
 */

package com.compareglobal.service.loans.personal.service;

import com.compareglobal.service.loans.personal.domain.Compare;
import com.compareglobal.service.loans.personal.domain.PersonalLoan;
import com.compareglobal.service.loans.personal.domain.PersonalLoanPublic;

import java.util.List;

/**
 * Created by dennis on 2/17/15.
 */
public interface LoanService {
    List<PersonalLoanPublic> convert(List<PersonalLoan> personalLoans, Compare compare);
}
