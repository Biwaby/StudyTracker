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
                                // права для всех
                                .requestMatchers(HttpMethod.POST, "/users/register").permitAll()
                                // только для зарегистрированных (имеющих роль USER и/или ADMIN)
                                // GET
                                .requestMatchers(HttpMethod.GET, "/timetable").authenticated()
                                .requestMatchers(HttpMethod.GET, "/timetable/**").authenticated()
                                .requestMatchers(HttpMethod.GET, "/subjects").authenticated()
                                .requestMatchers(HttpMethod.GET, "/subjects/**").authenticated()
                                .requestMatchers(HttpMethod.GET, "/classtypes").authenticated()
                                .requestMatchers(HttpMethod.GET, "/classtypes/**").authenticated()
                                .requestMatchers(HttpMethod.GET, "/tasks").authenticated()
                                .requestMatchers(HttpMethod.GET, "/tasks/**").authenticated()
                                .requestMatchers(HttpMethod.GET, "/teachers").authenticated()
                                .requestMatchers(HttpMethod.GET, "/teachers/**").authenticated()
                                .requestMatchers(HttpMethod.GET, "/timernotes").authenticated()
                                .requestMatchers(HttpMethod.GET, "/timernotes/**").authenticated()
                                .requestMatchers(HttpMethod.GET, "/classrooms").authenticated()
                                .requestMatchers(HttpMethod.GET, "/classrooms/**").authenticated()
                                .requestMatchers(HttpMethod.GET, "/buildings").authenticated()
                                .requestMatchers(HttpMethod.GET, "/buildings/**").authenticated()
                                // POST
                                .requestMatchers(HttpMethod.POST, "/timetable").authenticated()
                                .requestMatchers(HttpMethod.POST, "/subjects").authenticated()
                                .requestMatchers(HttpMethod.POST, "/classtypes").authenticated()
                                .requestMatchers(HttpMethod.POST, "/tasks").authenticated()
                                .requestMatchers(HttpMethod.POST, "/teachers").authenticated()
                                .requestMatchers(HttpMethod.POST, "/timernotes").authenticated()
                                .requestMatchers(HttpMethod.POST, "/classrooms").authenticated()
                                .requestMatchers(HttpMethod.POST, "/buildings").authenticated()
                                //PUT
                                .requestMatchers(HttpMethod.PUT, "/timetable/{id}").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/subjects/{id}").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/classtypes/{id}").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/tasks/**").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/teachers/{id}").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/timernotes/{id}").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/classrooms/{id}").authenticated()
                                .requestMatchers(HttpMethod.PUT, "/buildings/{id}").authenticated()
                                //DELETE
                                .requestMatchers(HttpMethod.DELETE, "/timetable/{id}").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/subjects/{id}").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/classtypes/{id}").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/tasks/{id}").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/teachers/{id}").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/timernotes/{id}").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/classrooms/{id}").authenticated()
                                .requestMatchers(HttpMethod.DELETE, "/buildings/{id}").authenticated()
                                // только для имеющих роль ADMIN
                                .requestMatchers(HttpMethod.GET, "/users").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/users/**").hasAuthority("ADMIN")
                                .requestMatchers("/roles").hasAuthority("ADMIN")
                                .requestMatchers("/roles/**").hasAuthority("ADMIN")
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
