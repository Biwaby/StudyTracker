package org.biwaby.studytracker.сonfigs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth ->
                        auth
                                // for all
                                .requestMatchers(HttpMethod.POST, "/users/register").permitAll()
                                // only for authenticated users
                                .requestMatchers("/timer/**").authenticated()
                                .requestMatchers("/projects/**").authenticated()
                                .requestMatchers("/tags/**").authenticated()
                                .requestMatchers("/summary/**").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/users/delete").authenticated()
                                // only fon admin users
                                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                )
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
