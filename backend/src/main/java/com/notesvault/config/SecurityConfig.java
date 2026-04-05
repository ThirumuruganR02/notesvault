package com.notesvault.config;

import com.notesvault.common.ApiPaths;
import com.notesvault.security.JwtAuthenticationFilter;
import com.notesvault.security.JsonUnauthorizedEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JsonUnauthorizedEntryPoint jsonUnauthorizedEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        auth ->
                                auth.requestMatchers(HttpMethod.OPTIONS, "/**")
                                        .permitAll()
                                        .requestMatchers(ApiPaths.DOC_PATHS)
                                        .permitAll()
                                        .requestMatchers(ApiPaths.AUTH_ANT)
                                        .permitAll()
                                        .requestMatchers("/error")
                                        .permitAll()
                                        .requestMatchers(HttpMethod.GET, ApiPaths.NOTES)
                                        .authenticated()
                                        .requestMatchers(HttpMethod.POST, ApiPaths.NOTES)
                                        .authenticated()
                                        .requestMatchers(HttpMethod.PUT, ApiPaths.NOTES + "/*")
                                        .authenticated()
                                        .requestMatchers(HttpMethod.DELETE, ApiPaths.NOTES + "/*")
                                        .authenticated()
                                        .anyRequest()
                                        .denyAll())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jsonUnauthorizedEntryPoint))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
