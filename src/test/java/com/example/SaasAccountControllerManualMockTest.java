package com.example;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.HttpHeaders;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.Test;

import javax.validation.constraints.NotBlank;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

@Property(name = "spec.name", value = "SaasAccountControllerManualMockTest")
@Property(name = "micronaut.security.filter.enabled", value = StringUtils.FALSE)
@MicronautTest
class SaasAccountControllerManualMockTest {

    @Inject
    @Client("/")
    HttpClient httpClient;

    @Test
    void testHappyPath() {
        HttpRequest<?> request = HttpRequest.POST("/account", Collections.singletonMap("name", "CompanyA"));
        BlockingHttpClient client = httpClient.toBlocking();
        HttpResponse<?> response = client.exchange(request);
        assertEquals(HttpStatus.CREATED, response.status());
        assertEquals("/account/XXX", response.getHeaders().get(HttpHeaders.LOCATION));

    }

    @Requires(property = "spec.name", value = "SaasAccountControllerManualMockTest")
    @Replaces(SaasAccountRepository.class)
    @Singleton
    static class SaasAccountRepositoryReplacement implements SaasAccountRepository {

        @Override
        @NonNull
        public String save(@NonNull @NotBlank String name) {
            return "XXX";
        }
    }
}