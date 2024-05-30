package org.biwaby.studytracker.utils;

import org.biwaby.studytracker.models.Role;
import org.biwaby.studytracker.models.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.HashSet;
import java.util.List;

public class UseMockWithCustomUserSecurityContextFactory implements WithSecurityContextFactory<UseMockWithCustomUser> {

    @Override
    public SecurityContext createSecurityContext(UseMockWithCustomUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        User user = new User(1L, "testingUser", "1234", true, new HashSet<>(List.of(new Role(1L, "USER"))));
        Authentication auth = UsernamePasswordAuthenticationToken.authenticated(user, "1234", user.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}
