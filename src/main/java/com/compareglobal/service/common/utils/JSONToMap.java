package com.compareglobal.service.common.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dennis on 5/13/15.
 */
public class JSONToMap {

    private JSONToMap(){}

    public static Map<String, String> toMap(String jsonData) {

        Map<String, String> ratesMap = new HashMap<String, String>();
        if (StringUtils.isNotBlank(jsonData)) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                ratesMap = mapper.readValue(jsonData, new TypeReference<HashMap<String, String>>() {
                });
            } catch (IOException e) {
                ratesMap = null;
            }
        }
        return  ratesMap;
    }
}
