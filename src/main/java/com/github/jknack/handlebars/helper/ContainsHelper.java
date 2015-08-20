package com.github.jknack.handlebars.helper;

import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dennis on 7/16/15.
 */
public class ContainsHelper implements Helper<Object> {

    public static final Helper<Object> INSTANCE = new ContainsHelper();
    /**
     * The helper's name.
     */
    public static final String NAME = "contains";
    private static final String KEYWORDS = "keyword";

    @Override
    public CharSequence apply(Object context, Options options) throws IOException {
        if (options.isFalsy(context)) {
            return options.inverse();
        } else {
            String keywords =  String.valueOf(options.hash.get(KEYWORDS))
                                     .toUpperCase();
            if (StringUtils.isNotBlank(keywords)) {
                String stringContext = context.toString().toUpperCase();
                List<String> keywordsList = Arrays.asList(keywords.split(","));
                for (String keyword : keywordsList) {
                    if (StringUtils.contains(stringContext, keyword.trim())) {
                        return options.fn();
                    }
                }
            }
            return options.inverse();
        }
    }
}