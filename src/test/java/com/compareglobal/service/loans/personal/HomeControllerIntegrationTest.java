package com.compareglobal.service.loans.personal;

import com.compareglobal.service.TestBase;
import com.compareglobal.service.loans.personal.domain.Compare;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import java.util.Map;

import static com.compareglobal.service.loans.personal.PersonalLoanAssertions.*;
import static org.assertj.core.api.Assertions.assertThat;


@SuppressWarnings("unchecked")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest("server.port=9000")
public class HomeControllerIntegrationTest extends TestBase {

    @Test
    public void shouldReturnPersonalLoanListFromIndonesia() {
        final Compare params = newCompareIndonesia(24, 25000000);

        shouldReturnPersonalLoanListFrom(params);
    }

    @Test
    public void shouldReturnProviderListFromIndonesia() {
        shouldReturnProviderListFrom(EN_ID);
    }

    @Test
    public void shouldReturnPersonalLoanListFromFinland() {
        final Compare params = newCompareFinland(24, 10000);

        shouldReturnPersonalLoanListFrom(params);
    }

    @Test
    public void shouldReturnProviderListFromFinland() {
        shouldReturnProviderListFrom(FI_FI);
    }

    @Test
    public void shouldReturnPersonalLoanListFromFinlandFilteredByTermLoan() {
        final Compare.Filter requestFilter = Compare.Filter.TERMLOAN;
        final String dataBaseFilterName = "hasTermLoan";

        shouldReturnPersonalLoanListFromFinlandFilteredBy(requestFilter, dataBaseFilterName);
    }

    @Test
    public void shouldReturnPersonalLoanListFromFinlandFilteredByBalanceTransfer() {
        // for backward with mule
        final Compare.Filter requestFilter = Compare.Filter.BALANCETRANSFER;
        final String dataBaseFilterName = "hasTermLoan";

        shouldReturnPersonalLoanListFromFinlandFilteredBy(requestFilter, dataBaseFilterName);
    }


    @Test
    public void shouldReturnPersonalLoanListFromFinlandFilteredByRevolvingLoan() {
        final Compare.Filter requestFilter = Compare.Filter.REVOLVINGLOAN;
        final String dataBaseFilterName = "hasRevolvingLoan";

        shouldReturnPersonalLoanListFromFinlandFilteredBy(requestFilter, dataBaseFilterName);
    }


    @Test
    public void shouldReturnPersonalLoanListFromFinlandFilteredByConsolidatedCredit() {
        // for backward with mule
        final Compare.Filter requestFilter = Compare.Filter.CONSOLIDATEDCREDIT;
        final String dataBaseFilterName = "hasRevolvingLoan";

        shouldReturnPersonalLoanListFromFinlandFilteredBy(requestFilter, dataBaseFilterName);
    }


    @Ignore @Test
    public void shouldReturnPersonalLoanListFromFinlandFilteredByQuickLoan() {
        final Compare.Filter requestFilter = Compare.Filter.QUICKLOAN;
        final String dataBaseFilterName = "hasQuickLoan";

        shouldReturnPersonalLoanListFromFinlandFilteredBy(6, 200, requestFilter, dataBaseFilterName);
    }


    @Ignore @Test
    public void shouldReturnPersonalLoanListFromFinlandFilteredByCreditLine() {
        // for backward with mule
        final Compare.Filter requestFilter = Compare.Filter.CREDITLINE;
        final String dataBaseFilterName = "hasQuickLoan";

        shouldReturnPersonalLoanListFromFinlandFilteredBy(6, 200,requestFilter, dataBaseFilterName);
    }

    private void shouldReturnPersonalLoanListFromFinlandFilteredBy(final Compare.Filter requestFilter, final String dataBaseFilterName) {
        shouldReturnPersonalLoanListFromFilteredBy(FI_FI, requestFilter, dataBaseFilterName);
    }

    private void shouldReturnPersonalLoanListFromFinlandFilteredBy(final int loanTenure, final int loanAmount, final Compare.Filter requestFilter, final String dataBaseFilterName) {
        shouldReturnPersonalLoanListFromFilteredBy(FI_FI, loanTenure, loanAmount, requestFilter, dataBaseFilterName);
    }

    private void shouldReturnPersonalLoanListFromFilteredBy(final String locale, final Compare.Filter requestFilter, final String dataBaseFilterName) {
        shouldReturnPersonalLoanListFromFilteredBy(locale, 12, 12000, requestFilter, dataBaseFilterName);
    }


    private void shouldReturnPersonalLoanListFromFilteredBy(final String locale, final int loanTenure, final int loanAmount, final Compare.Filter requestFilter, final String dataBaseFilterName) {
        final ResponseEntity<List> response = shouldReturnPersonalLoanListFrom(newCompare(locale, loanTenure, loanAmount, requestFilter));

        final List<Map<String, Object>> personalLoans = response.getBody();
        for (final Map<String, Object> personalLoan : personalLoans) {
            assertThatHasFilter(personalLoan, dataBaseFilterName);
        }
    }

    private ResponseEntity<List> shouldReturnPersonalLoanListFrom(final Compare params) {
        final ResponseEntity<List> response = postPersonalLoans(params);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThatHasPersonalLoans(response);
        return response;
    }

    private ResponseEntity<List> shouldReturnProviderListFrom(final String locale) {
        final ResponseEntity<List> response = postPersonalLoanProviders(locale);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThatHasProviders(response);
        return response;
    }

    private static Compare newCompareFinland(final Integer loanTenure, final Integer loanAmount) {
        return newCompare(FI_FI, loanTenure, loanAmount, null);
    }

    private static Compare newCompareIndonesia(final Integer loanTenure, final Integer loanAmount) {
        return newCompare(EN_ID, loanTenure, loanAmount, null);
    }

    private static Compare newCompare(final String locale, final Integer loanTenure, final Integer loanAmount, final Compare.Filter filter) {
        final Compare compare = new Compare();
        compare.setLocale(locale);
        compare.setLoanTenure(loanTenure);
        compare.setLoanAmount(loanAmount);
        compare.setFilter(filter);
        return compare;
    }

    private static final String FI_FI = "fi-FI";

    private static final String EN_ID = "en-ID";
}