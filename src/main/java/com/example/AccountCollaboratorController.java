package com.example;

import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.security.Principal;

@Controller("/withhttpclient")
public class AccountCollaboratorController {

    private final AccountClient accountClient;

    public AccountCollaboratorController(AccountClient accountClient) {
        this.accountClient = accountClient;
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Post
    @SingleResult
    Publisher<HttpResponse<?>> save(@Body @Valid SaasAccountSave saasAccountSave, Principal principal) {
        return Mono.from(accountClient.save(saasAccountSave))
                .map(account -> HttpResponse.created(UriBuilder.of("/account").path(account.getId()).build()));
    }
}
