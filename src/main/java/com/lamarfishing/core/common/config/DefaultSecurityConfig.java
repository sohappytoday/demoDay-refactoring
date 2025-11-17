//package com.lamarfishing.core.common.config;
//
//import com.querydsl.core.annotations.Config;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.convert.converter.Converter;
//import org.springframework.security.authentication.AbstractAuthenticationToken;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.oauth2.jwt.Jwt;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
//import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
//import org.springframework.security.web.SecurityFilterChain;
//
//import java.util.ArrayList;
//
//@Configuration
//public class DefaultSecurityConfig {
//
//    @Bean
//    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//
//        http
//                .cors(Customizer.withDefaults())
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(request -> request.anyRequest().authenticated())
//                .oauth2ResourceServer(resource -> resource.jwt(
//                        jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
//                ));
//
//        return http.build();
//    }
//
//    @Bean
//    public Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {
//
//        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
//
//        return new Converter<Jwt, AbstractAuthenticationToken>() {
//
//            @Override
//            public AbstractAuthenticationToken convert(Jwt jwt) {
//
//                ArrayList<GrantedAuthority> authorities = new ArrayList<>(converter.convert(jwt));
//                authorities.add(new SimpleGrantedAuthority("GRADE_" + jwt.getClaim("grade").toString()));
//
//                return new JwtAuthenticationToken(jwt, authorities, jwt.getSubject());
//            }
//        };
//    }
//}
