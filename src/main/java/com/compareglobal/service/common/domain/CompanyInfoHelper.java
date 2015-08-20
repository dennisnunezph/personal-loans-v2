/*
 * Copyright (c) 2015.
 * Compare Asia Group
 */
package com.compareglobal.service.common.domain;

import java.util.Set;

/**
 * Created by dennis on 3/3/15.
 */
public class CompanyInfoHelper {

    protected static final String COMPANY_LOGO = "company_logo";
    protected static final String LOGO = "logo";
    private final CompanyInfo companyInfo;

    public CompanyInfoHelper(Provider provider) {
        String logo = "";
        if (provider != null) {
            logo = getCompanyLogo(provider.getProviderInfo());
        }
        companyInfo = new CompanyInfo(provider.getName(), logo);

    }

    public CompanyInfoHelper(Provider provider, Set<Image> images) {
        String companyLogo = "";
        for (Image image : images) {
            if (LOGO.equalsIgnoreCase(image.getTypeValue())) {
                companyLogo = image.getUrl();
                break;
            }
        }
        companyInfo = new CompanyInfo(provider.getName(), companyLogo);
    }

    public CompanyInfo getCompanyInfo() {
        return companyInfo;
    }

    private String getCompanyLogo(Set<ProviderInfo> providerInfo) {
        String companyLogo = "";
        for (ProviderInfo provider : providerInfo) {
            if (COMPANY_LOGO.equalsIgnoreCase(provider.getTypeValue())) {
                companyLogo = provider.getValue();
                break;
            }
        }
        return companyLogo;
    }
}
