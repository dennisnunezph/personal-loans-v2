package com.compareglobal.service.common.domain;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CompanyInfoHelperTest {
    private CompanyInfoHelper infoHelper;
    private Provider mockProvider;

    @Before
    public void init() {
        mockProvider = mock(Provider.class);
        Set<ProviderInfo> infoSet = new HashSet<>();
        ProviderInfo providerInfo = new ProviderInfo();
        providerInfo.setTypeValue(CompanyInfoHelper.COMPANY_LOGO);
        providerInfo.setValue("logo.png");
        infoSet.add(providerInfo);
        when(mockProvider.getProviderInfo()).thenReturn(infoSet);
        infoHelper = new CompanyInfoHelper(mockProvider);
    }

    @Test
    public void testLogoFromProvider() throws Exception {
        CompanyInfo companyInfo = infoHelper.getCompanyInfo();
        assertNotNull(companyInfo);
        assertEquals("logo.png", companyInfo.getLogo());
        verify(mockProvider).getProviderInfo();
    }

    @Test
    public void testLogoFromImages() throws Exception {
        Set<Image> imagesStub = new HashSet<>();
        Image image = mock(Image.class);
        when(image.getTypeValue()).thenReturn(CompanyInfoHelper.LOGO);
        when(image.getUrl()).thenReturn("productLogo.png");
        imagesStub.add(image);

        infoHelper = new CompanyInfoHelper(mockProvider, imagesStub);
        CompanyInfo companyInfo = infoHelper.getCompanyInfo();
        assertNotNull(companyInfo);
        assertEquals("productLogo.png", companyInfo.getLogo());
        verify(image).getTypeValue();
    }
}
