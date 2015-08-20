package com.compareglobal.service.loans.personal.service;

import com.compareglobal.service.common.domain.Provider;
import com.compareglobal.service.common.repository.ProviderRepository;
import com.compareglobal.service.loans.personal.domain.Compare;
import com.compareglobal.service.loans.personal.domain.PersonalLoan;
import com.compareglobal.service.loans.personal.repository.PersonalLoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompareService {
	
	@Autowired
	private PersonalLoanRepository personalLoanRepository;

	@Autowired
	private ProviderRepository providerRepository;

	public List<PersonalLoan> compare(Compare compare) {
		return personalLoanRepository.findByActiveAndLocale(compare.getLocale());
	}

	public List<Provider> getProviders(String locale) {
		return providerRepository.findByCountry(locale);
	}

}
