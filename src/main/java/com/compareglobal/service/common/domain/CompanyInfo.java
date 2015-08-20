package com.compareglobal.service.common.domain;

/**
 * Created by dennis on 1/21/15.
 */
public class CompanyInfo {
    private String name;
    private String logo;

    public CompanyInfo(String name, String logo) {
        this.name = name;
        this.logo = logo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
