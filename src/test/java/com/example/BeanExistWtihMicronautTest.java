package com.example;

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.BeanContext;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
class BeanExistWtihMicronautTest {

    @Inject
    BeanContext beanContext;

    @Test
    void beanExistsWithoutMicronautTest() {
        assertTrue(beanContext.containsBean(SaasAccountController.class));
    }

}
