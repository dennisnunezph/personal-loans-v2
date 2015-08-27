package com.compareglobal.service.loans.personal.domain;


import com.compareglobal.service.common.domain.CompanyInfo;
import com.compareglobal.service.common.domain.Image;
import com.compareglobal.service.common.domain.Item;
import com.compareglobal.service.common.domain.Link;
import com.compareglobal.service.common.loans.personal.domain.MortgageDisplay;
import com.compareglobal.service.loans.personal.domain.benefits.Benefit;
import com.compareglobal.service.loans.personal.domain.eligibility.Criteria;
import com.compareglobal.service.loans.personal.domain.fees.Fee;
import org.json.simple.JSONObject;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * Created by dennis on 11/25/14.
 */
public class PersonalLoanPublic {
    private Long id;
    private String name;
    private Boolean active;
    private String locale;
    private CompanyInfo company;
    private PersonalLoan personalLoan;
    private MortgageDisplay payments;
    private Object filters;
    private List<Item> ranking;
    private List<Item> featured;
    private List<Item> promotions;
    private JSONObject product;
    private List<Benefit> sortedBenefits;

    public PersonalLoanPublic() {
    }
    public PersonalLoanPublic(PersonalLoan personalLoan) {
        this.personalLoan = personalLoan;
        name = personalLoan.getName();
        active = personalLoan.getActive();
        locale = personalLoan.getLocale();
    }

    public Set<Criteria> getEligibilities() {
        return personalLoan.getEligibilityList();
    }

    public Set<Image> getImages() {
        return personalLoan.getImages();
    }

    public Set<Link> getLinks() { return personalLoan.getLinks(); }

    public Set<Fee> getFees() { return personalLoan.getFeesList(); }

    public Set<Benefit> getBenefits() {
        return personalLoan.getBenefitsList();
    }

    public String getName() { return name; }

    public Boolean getActive() { return active; }

    public String getLocale() { return locale; }

    public CompanyInfo getCompany() {
        return company;
    }
    
    public Object getFilters() {
        return filters;
    }

    public void setFilters(Object filters) { this.filters = filters; }

    public List<Item> getRanking() { return ranking; }

    public void setRanking(List<Item> ranking) { this.ranking = ranking; }

    public List<Item> getFeatured() { return featured; }

    public void setFeatured(List<Item> featured) { this.featured = featured;  }

    public void setCompany(CompanyInfo company) {
        this.company = company;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MortgageDisplay getPayments() {
        return payments;
    }

    public void setPayments(MortgageDisplay payments) {
        this.payments = payments;
    }

    public void setPromotions(List<Item> promotions) {
        this.promotions = promotions;
    }

    public List<Item> getPromotions() {
        return promotions;
    }

    public JSONObject getProduct() {
        return product;
    }

    public void setProduct(JSONObject product) {
        this.product = product;
    }

    public PersonalLoan getPersonalLoan() {
        return personalLoan;
    }

    public void setSortedBenefits(List<Benefit> sortedBenefits) {
        this.sortedBenefits = sortedBenefits;
    }

    public List<Benefit> getSortedBenefits() {
        return sortedBenefits;
    }
}
