/*
 * Copyright (c) 2015.
 * Compare Asia Group
 */
package com.compareglobal.service.common.utils;

import com.compareglobal.service.loans.personal.domain.Compare;
import com.compareglobal.service.loans.personal.domain.PersonalLoan;

/**
 * Created by dennis on 4/22/15.
 */
public interface CompareHelper {
    boolean recordIncluded(Compare.Filter filter, PersonalLoan pl);
}