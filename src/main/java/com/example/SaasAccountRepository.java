package com.example;

import io.micronaut.core.annotation.NonNull;

import javax.validation.constraints.NotBlank;

public interface SaasAccountRepository {

    @NonNull
    String save(@NonNull @NotBlank String name);
}
