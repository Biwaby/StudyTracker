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
                                // GET
                                .requestMatchers(HttpMethod.GET, "/timer").authenticated()
                                .requestMatchers(HttpMethod.GET, "/timer/**").authenticated()
                                .requestMatchers(HttpMethod.GET, "/projects").authenticated()
                                .requestMatchers(HttpMethod.GET, "/projects/**").authenticated()
                                .requestMatchers(HttpMethod.GET, "/tags").authenticated()
                                .requestMatchers(HttpMethod.GET, "/tags/**").authenticated()
                                .requestMatchers(HttpMethod.GET, "/summary/**").authenticated()
                                // POST
                                .requestMatchers(HttpMethod.POST, "/timer/record").authenticated()
                                .requestMatchers(HttpMethod.POST, "/projects/create").authenticated()
                                .requestMatchers(HttpMethod.POST, "/projects/tasks/add/**").authenticated()
                                .requestMatchers(HttpMethod.POST, "/tags/add").authenticated()
                                //PUT
                                .requestMatchers(HttpMethod.PUT, "/timer/edit").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/timer/addTag").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/timer/removeTag").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/timer/addProject").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/timer/removeProject").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/timer/addTask").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/timer/removeTask").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/projects/edit").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/projects/tasks/edit").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/tags/edit").authenticated()
                                //DELETE
                                .requestMatchers(HttpMethod.DELETE, "/timer/delete").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/projects/delete").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/projects/tasks/delete").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/tags/delete").authenticated()
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
