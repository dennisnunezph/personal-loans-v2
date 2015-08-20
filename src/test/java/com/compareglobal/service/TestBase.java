package com.compareglobal.service;

import com.compareglobal.service.loans.personal.domain.Compare;
import org.apache.commons.lang.StringUtils;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public abstract class TestBase {

    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            String methodName = StringUtils.join(StringUtils.splitByCharacterTypeCamelCase(description.getMethodName()), " ");
            String className = description.getClassName();
            className = className.substring(className.lastIndexOf('.') + 1);
            System.out.println("### TEST [" + className + "] " + methodName);
            System.out.println("```");
            super.starting(description);
        }

        @Override
        protected void finished(Description description) {
            System.out.println("```");
            super.finished(description);
        }
    };

    private void log(String info) {
        System.err.println(info);
    }

    protected ResponseEntity<List> postPersonalLoanProviders(final String locale) {
        final Compare params = new Compare();
        params.setLocale(locale);
        return postPersonalLoan("/providers", params);
    }

    protected ResponseEntity<List> postPersonalLoans(final Compare params) {
        return postPersonalLoan("/personalLoans", params);
    }

    private ResponseEntity<List> postPersonalLoan(final String uri, final Compare params) {
        return postPersonalLoan(uri, new HttpEntity<>(params, getDefaultRequestHeaders()));
    }

    private ResponseEntity<List> postPersonalLoan(final String uri, final HttpEntity<?> requestEntity) {
        return requestPersonalLoan(uri, HttpMethod.POST, requestEntity);
    }

    private ResponseEntity<List> requestPersonalLoan(final String uri, final HttpMethod httpMethod, final HttpEntity<?> requestEntity) {
        final String API_URL = "http://localhost:9000";
        final String url = String.format("%s%s", API_URL, uri);
        log(String.format("Request \nMethod=%s\nUrl=%s\nHeaders=%s\nbody=%s\n", httpMethod, url, requestEntity.getHeaders(), requestEntity.getBody()));
        final ResponseEntity<List> response = restTemplate.exchange(url, httpMethod, requestEntity, listTypeReference());
        log(String.format("Response \nStatus=%s\n", response.getStatusCode()));
        return response;
    }

    private HttpHeaders getDefaultRequestHeaders() {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(new MediaType("application","json"));
        return requestHeaders;
    }

    private ParameterizedTypeReference<List> listTypeReference() {
        return new ParameterizedTypeReference<List>() { };
    }

    private final RestTemplate restTemplate = new TestRestTemplate();
}
