package com.compareglobal.service.common.domain;

import com.compareglobal.service.common.utils.ListHelper;

import java.util.Set;

/**
 * Created by dennis on 3/27/15.
 */
public class LinkUrlHelper {
    private LinkUrlHelper(){}

    public static String getImageValue(Set<Image> itemList, String typeValue) {
        Image image = ListHelper.findItemByTypeT(itemList, "typeValue", typeValue);
        return image == null ? "" : image.getUrl();
    }

    public static String getLinkValue(Set<Link> itemList, String typeValue) {
        Link link = ListHelper.findItemByTypeT(itemList, "typeValue", typeValue);
        return link == null ? "" : link.getUrl();
    }

}
