package org.eventbook.eventbooking.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.eventbook.eventbooking.web.security.JwtTokenFilter;
import org.eventbook.eventbooking.web.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
//@EnableMethodSecurity
@RequiredArgsConstructor(onConstructor = @__(@Lazy))
public class ApplicationConfig {

    private final JwtTokenProvider tokenProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            final AuthenticationConfiguration configuration)
            throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("Bearer",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                ).info(new Info()
                        .title("Event Booking API")
                        .description("This API allows customers to create, "
                                + "find and reserve tickets for events, "
                                + "view and manage their reservations, "
                                + "and be notified before the event kickoff.")
                        .version("1.0")
                );
    }



    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity httpSecurity)
            throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement ->
                        sessionManagement
                                .sessionCreationPolicy(
                                        SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(configurer ->
                        configurer.authenticationEntryPoint(
                                (request, response, exception) -> {
                                    response.setStatus(
                                            HttpStatus.UNAUTHORIZED
                                                    .value()
                                    );
                                    response.getWriter()
                                            .write("Unauthorized.");
                                })
                                .accessDeniedHandler(
                                        (request, response, exception) -> {
                                            response.setStatus(
                                                    HttpStatus.FORBIDDEN
                                                            .value()
                                            );
                                            response.getWriter()
                                                    .write("Unauthorized.");
                                        }))
                .authorizeHttpRequests(configurer ->
                        configurer.requestMatchers("/auth",
                                                    "/swagger-ui/**",
                                                    "/graphiql",
                                                    "/users",
                                                    "/v3/api-docs/**")
                                .permitAll()
                                .anyRequest()
                                .authenticated())
                .anonymous(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JwtTokenFilter(tokenProvider),
                        UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
