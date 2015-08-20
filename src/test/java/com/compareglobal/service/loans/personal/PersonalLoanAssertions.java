package com.compareglobal.service.loans.personal;

import com.compareglobal.service.loans.personal.domain.Payment;
import org.junit.Assert;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Created by Luis Miguel Osorio.
 */
public class PersonalLoanAssertions {
    public static void assertThatHasPersonalLoans(final ResponseEntity<List> response) {
        assertThatHasPersonalLoans("", response);
    }

    public static void assertThatHasPersonalLoans(final String message, final ResponseEntity<List> response) {
        assertThatHasList("the service should returns a list of personal loans " + message, response);
    }

    public static void assertThatHasProviders(final ResponseEntity<List> response) {
        assertThatHasList("the service should returns a list of providers", response);
    }

    public static void assertThatHasList(final String message, final ResponseEntity<List> response) {
        Assert.assertNotNull(message, response.getBody());
        Assert.assertFalse(message, response.getBody().isEmpty());
    }

    public static void assertThatHasFilter(final Map<String, Object> personalLoan, final String dataBaseFilterName) {
        final List<Map<String, String>> filters = (List<Map<String, String>>) personalLoan.get("filters");
        Assert.assertNotNull("the personal loan should has a list of filters", filters);

        boolean hasDataBaseFilter = false;
        for (Map<String, String> creditCardFilter : filters) {
            if (creditCardFilter.get("type").equals(dataBaseFilterName) && creditCardFilter.get("value").equals("true")) {
                hasDataBaseFilter = true;
            }
        }

        Assert.assertTrue("the personal loan should has `" + dataBaseFilterName + "` filter", hasDataBaseFilter);
    }

    public static void assertEqualsPayment(final Payment expectedPayment, final Map<String, Object> actualPayment) {
        final Double actualInterestRate = Double.parseDouble((String)actualPayment.get("interestRate"));
        final Double actualMonthlyPayment = Double.parseDouble((String)actualPayment.get("monthlyPayment"));
        final Double actualTotalRepayment = Double.parseDouble((String)actualPayment.get("totalRepayment"));
        assertEquals("should be equals interestRate", expectedPayment.getInterestRate(), actualInterestRate);
        assertEquals("should be equals monthlyPayment", expectedPayment.getMonthlyPayment(), actualMonthlyPayment);
        assertEquals("should be equals totalRepayment", expectedPayment.getTotalRepayment(), actualTotalRepayment);
    }
}
