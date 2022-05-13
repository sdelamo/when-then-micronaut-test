package com.example;

import io.micronaut.context.ApplicationContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class BeanExistTest {

    @Test
    void beanExistsWithoutMicronautTest() {

        try(ApplicationContext context = ApplicationContext.run()) {
            assertTrue(context.containsBean(SaasAccountController.class));
        }
    }

}
