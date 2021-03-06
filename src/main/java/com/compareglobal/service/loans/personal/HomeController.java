package com.compareglobal.service.loans.personal;

/**
 * Created by dennis on 11/25/14.
 */

import com.compareglobal.service.common.domain.Provider;
import com.compareglobal.service.common.domain.RetrieveList;
import com.compareglobal.service.loans.personal.domain.*;
import com.compareglobal.service.loans.personal.service.CompareService;
import com.compareglobal.service.loans.personal.service.LoanServiceFactory;
import com.compareglobal.service.loans.personal.transform.PersonalLoanTransformer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jsonSchema.JsonSchema;
import com.fasterxml.jackson.module.jsonSchema.JsonSchemaGenerator;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import org.apache.log4j.Logger;
import org.json.simple.JSONValue;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dennis on 11/24/14.
 */
@Controller
public class HomeController {
    private final ObjectMapper MAPPER = new ObjectMapper();
    private final JsonSchemaGenerator generator = new JsonSchemaGenerator(MAPPER);
    private static final Logger log = Logger.getLogger(HomeController.class);
    private static final String DEFAULT_CONTENT_TYPE_CHARSET = "Content-Type=application/json;charset=UTF-8";

    private final CompareService compareService;
    private final Handlebars handlebars;
    private final LoanServiceFactory serviceFactory;


    @Inject
    public HomeController(CompareService compareService,
                          Handlebars handlebars,
                          LoanServiceFactory serviceFactory) {
        this.compareService = compareService;
        this.handlebars = handlebars;
        this.serviceFactory = serviceFactory;
    }

    @RequestMapping(value = "/personalLoans", method = RequestMethod.GET, headers = DEFAULT_CONTENT_TYPE_CHARSET)
    @ResponseBody
    public JsonSchema home() throws JsonMappingException {

        return generator.generateSchema(Compare.class);
    }

    @RequestMapping(value = "/template", method = RequestMethod.POST, headers = DEFAULT_CONTENT_TYPE_CHARSET)
    @ResponseBody
    public Object template(@RequestBody final Compare compare, BindingResult result) throws IOException {

        double loanAmount = Double.valueOf(compare.getLoanAmount());
        int tenure = Integer.valueOf(compare.getLoanTenure());
        String locale = compare.getLocale();

        List<PersonalLoan> personalLoans = compareService.compare(compare);
        PersonalLoanTransformer loanTransformer = new PersonalLoanTransformer();
        List<PersonalLoanTemplate> personalLoanFiltered = loanTransformer.filteredList(personalLoans, compare);
        Template template = handlebars.compile("personalLoanTemplate" + compare.getCountrySuffix());
        List<Object> resultTemplate = new ArrayList<>();

        MonthlyPaymentHelper computationService = serviceFactory.getLoanComputationInstance(locale);
        for (PersonalLoanTemplate loanPublic : personalLoanFiltered) {

            Mortgage loan = computationService.computeMortgage(loanAmount, tenure, loanPublic.getPersonalLoan());
            loanPublic.setLoan(loan);

            String templateResult = template.apply(loanPublic).replaceAll("\n", "")
                    .replaceAll(" },]", "}]")
                    .replaceAll("&quot;", "\"\"");
            resultTemplate.add(JSONValue.parse(templateResult));
        }

        return resultTemplate;
    }

    @RequestMapping(value = "/providers", method = RequestMethod.POST, headers = DEFAULT_CONTENT_TYPE_CHARSET)
    @ResponseBody
    public List<Provider> providers(@RequestBody final RetrieveList retrieve) {
        return compareService.getProviders(retrieve.getLocale());
    }

}