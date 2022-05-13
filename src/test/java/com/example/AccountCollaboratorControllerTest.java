package com.example;

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.core.convert.ArgumentConversionContext;
import io.micronaut.core.io.socket.SocketUtils;
import io.micronaut.core.util.CollectionUtils;
import io.micronaut.core.util.StringUtils;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.runtime.server.EmbeddedServer;
import io.micronaut.security.authentication.PrincipalArgumentBinder;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.micronaut.test.support.TestPropertyProvider;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.reactivestreams.Publisher;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Property(name = "spec.name", value = "AccountCollaboratorControllerTest")
@Property(name = "micronaut.security.filter.enabled", value = StringUtils.FALSE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@MicronautTest
public class AccountCollaboratorControllerTest implements TestPropertyProvider {

    @Inject
    @Client("/")
    HttpClient httpClient;
    EmbeddedServer collaborator;

    @Override
    public Map<String, String> getProperties() {
        int collaboratorPort = SocketUtils.findAvailableTcpPort();
        Map<String, Object> collaboratorProperties = CollectionUtils.mapOf(
        "micronaut.server.port", collaboratorPort,
        "micronaut.security.filter.enabled", StringUtils.FALSE,
        "spec.name", "AccountCollaboratorControllerTestCollaborator");
        this.collaborator = ApplicationContext.run(EmbeddedServer.class, collaboratorProperties);
        return Collections.singletonMap("micronaut.http.services.foo.url", "http://localhost:" + collaboratorPort);
    }

    @AfterAll
    public void teardown() {
        collaborator.close();
    }

    @Test
    void testHappyPath() {
        HttpRequest<?> request = HttpRequest.POST("/withhttpclient", Collections.singletonMap("name", "CompanyA"));
        BlockingHttpClient client = httpClient.toBlocking();
        HttpResponse<?> response = client.exchange(request);
        assertEquals(HttpStatus.CREATED, response.status());
    }

    @Requires(property = "spec.name", value = "AccountCollaboratorControllerTestCollaborator")
    @Controller
    static class MockCollaboratorController {
        @Post("/account/save")
        Account save(@Body SaasAccountSave accountSave) {
            return new Account(UUID.randomUUID().toString(), accountSave.getName());
        }
    }

    @Requires(property = "spec.name", value = "AccountCollaboratorControllerTest")
    @Replaces(PrincipalArgumentBinder.class)
    @Singleton
    static class PrincipalArgumentBinderReplacement extends PrincipalArgumentBinder {

        @Override
        public BindingResult<Principal> bind(ArgumentConversionContext<Principal> context, HttpRequest<?> source) {
            return () -> Optional.of((Principal) () -> "Sergio");
        }
    }
}
