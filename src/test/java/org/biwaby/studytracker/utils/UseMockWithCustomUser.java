package org.biwaby.studytracker.utils;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = UseMockWithCustomUserSecurityContextFactory.class)
public @interface UseMockWithCustomUser {
    String username() default "testUser";
    String name() default "testUser";
}
