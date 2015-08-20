/*
 * Copyright (c) 2015.
 * Compare Asia Group
 */
package com.compareglobal.service.loans.personal.domain.converter;

import com.compareglobal.service.common.domain.Filter;
import com.compareglobal.service.common.domain.Item;
import com.compareglobal.service.loans.personal.domain.Promotion;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by dennis on 3/10/15.
 */
public class PromotionHelper {

    private PromotionHelper(){}

    public static <E> List<Item> convertToItem(Set<E> data) {
        List<Item> listOutput = new ArrayList<>();
        String type = "";
        String value = "";

        for (E items : data) {
            if(items instanceof Promotion) {
                type = ((Promotion) items).getTypeValue();
                value = ((Promotion) items).getDescription();
            }
            listOutput.add(new Item(type,value));
        }
        return listOutput;
    }
}
