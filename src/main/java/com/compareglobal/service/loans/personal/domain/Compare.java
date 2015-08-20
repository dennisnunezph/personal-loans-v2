package com.compareglobal.service.loans.personal.domain;

import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by dennis on 11/26/14.
 */
public class Compare {

    public enum Filter {
        INSTALMENT,
        TAXLOAN,
        DEBTCONSOLIDATION,
        CREDITLINE,
        BALANCETRANSFER,
        ONLINELAAN,
        KVIKLAAN,
        DINBANK,
        QUICKCASH,
        CONSOLIDATEDCREDIT,
        SPECIALIST,
        PERSONALLOAN,
        PERSONALINST,
        OVERDRAFT,
        PAYDAY,
        LENDINGCOMPANIES,
        TERMLOAN,
        REVOLVINGLOAN,
        QUICKLOAN
    }

    private String locale;

    @NotNull
    @Size(min = 1, max = 20)
    private Integer loanAmount;

    @NotNull
    @Size(min = 1, max = 20)
    private Integer loanTenure;

    private Filter filter;

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public Integer getLoanAmount() { return loanAmount; }

    public void setLoanAmount(Integer loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Integer getLoanTenure() {
        return loanTenure;
    }

    public void setLoanTenure(Integer loanTenure) {
        this.loanTenure = loanTenure;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public String getCountrySuffix() {
        if (StringUtils.isNotBlank(locale)) {
            return locale.substring(3);
        }
        return "";
    }

    @Override
    public String toString() {
        return "Compare{" +
                "locale='" + locale + '\'' +
                ", loanAmount=" + loanAmount +
                ", loanTenure=" + loanTenure +
                ", filter=" + filter +
                '}';
    }
}
