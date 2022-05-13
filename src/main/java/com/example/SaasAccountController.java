package com.example;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.security.rules.SecurityRuleResult;

import javax.validation.Valid;

@Controller("/account")
public class SaasAccountController {

    private final SaasAccountRepository repository;

    public SaasAccountController(SaasAccountRepository repository) {
        this.repository = repository;
    }

    @Secured(SecurityRule.IS_AUTHENTICATED)
    @Post
    HttpResponse<?> save(@Body @Valid SaasAccountSave saasAccountSave) {
        String id = repository.save(saasAccountSave.getName());
        return HttpResponse.created(UriBuilder.of("/account").path(id).build());
    }
}
