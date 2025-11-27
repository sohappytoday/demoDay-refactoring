package com.lamarfishing.core.common.config;

import com.lamarfishing.core.common.CustomJwtAuthenticationToken;
import com.lamarfishing.core.user.dto.command.AuthenticatedUser;
import com.querydsl.core.annotations.Config;
import org.hibernate.StatelessSession;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.ArrayList;

@Configuration
public class DefaultSecurityConfig {

    @Bean
    @Order(1)
    public SecurityFilterChain prometheusFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/actuator/**")
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/health").permitAll()
                        .requestMatchers("/actuator/prometheus").authenticated()
                        .anyRequest().denyAll()
                )
                .httpBasic(Customizer.withDefaults())        // 🔥 여기서는 Basic 사용
                .oauth2ResourceServer(AbstractHttpConfigurer::disable); // JWT 비활성화

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

        http

                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/schedules/*/reservation/normal", "/schedules/main", "schedules/sch*").permitAll()
                        .requestMatchers(HttpMethod.POST, "/schedules/*/reservation").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/", "/error").permitAll()
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(resource -> resource.jwt(
                        jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
                ))
                .logout(AbstractHttpConfigurer::disable);




        return http.build();
    }


    @Bean
    public Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {

        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();

        return new Converter<Jwt, AbstractAuthenticationToken>() {

            @Override
            public AbstractAuthenticationToken convert(Jwt jwt) {

                ArrayList<GrantedAuthority> authorities = new ArrayList<>(converter.convert(jwt));
                authorities.add(new SimpleGrantedAuthority("GRADE_" + jwt.getClaim("grade").toString()));

                AuthenticatedUser authenticatedUser = AuthenticatedUser.from(jwt);

                return new CustomJwtAuthenticationToken(jwt, authorities, authenticatedUser);
            }
        };
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
        UserDetails prometheusUser = User.withUsername("jjubull")
                .password(encoder.encode("1234"))
                .build();
        return new InMemoryUserDetailsManager(prometheusUser);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
