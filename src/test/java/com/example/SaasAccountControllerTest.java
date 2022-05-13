package com.example;

import io.micronaut.context.annotation.Property;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@Property(name = "micronaut.security.filter.enabled", value = StringUtils.FALSE)
@MicronautTest
class SaasAccountControllerTest {

    @Inject
    @Client("/")
    HttpClient httpClient;

    @Test
    void testHappyPath() {
        HttpRequest<?> request = HttpRequest.POST("/account", Collections.singletonMap("name", "CompanyA"));
        BlockingHttpClient client = httpClient.toBlocking();
        HttpResponse<?> response = client.exchange(request);
        assertEquals(HttpStatus.CREATED, response.status());
    }

    @MockBean(SaasAccountRepository.class)
    SaasAccountRepository saasAccountRepository() {
        return mock(SaasAccountRepository.class);
    }

}