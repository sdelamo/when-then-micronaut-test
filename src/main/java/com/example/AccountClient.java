package com.example;

import io.micronaut.core.async.annotation.SingleResult;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import org.reactivestreams.Publisher;

@Client(id = "foo")
public interface AccountClient {

    @Post("/account/save")
    @SingleResult
    Publisher<Account> save(@Body SaasAccountSave accountSave);
}
