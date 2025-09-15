package com.stego.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Configuration
@EnableWebSecurity
public class AppConfig {

//	@Value("${base_url}")
//	private String baseUrl;

	  @Bean
	    PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }

	    @Bean
	    @Order(1)
	    SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
	        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = OAuth2AuthorizationServerConfigurer.authorizationServer();

	        http.securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
	                .authorizeHttpRequests(authz -> authz.requestMatchers("/files/**").permitAll()
	                	.anyRequest().authenticated())
	                .with(authorizationServerConfigurer, (authorizationServer) -> authorizationServer.oidc(Customizer.withDefaults()));


	        http.cors(cors -> cors.configurationSource(new CorsConfigurationSource() {
	            @Override
	            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
	                CorsConfiguration config = new CorsConfiguration();
	                config.setAllowCredentials(true);
	                config.addAllowedOriginPattern("*");
	                config.addAllowedHeader("*");
	                config.addAllowedMethod("*");
	                config.addExposedHeader("Authorization");
	                config.setMaxAge(366000L);
	                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    	        source.registerCorsConfiguration("/files/**", config);
	                return config;

	            }
	        })).exceptionHandling((exceptions) -> exceptions.defaultAuthenticationEntryPointFor(
	                new LoginUrlAuthenticationEntryPoint("/login"), new MediaTypeRequestMatcher(MediaType.TEXT_HTML)));

	        return http.build();
	    }

	    @Bean
	    @Order(2)
	    SecurityFilterChain jwtFilterChain(HttpSecurity http) throws Exception {
	        http.securityMatcher("/rest/api/**")
	                .authorizeHttpRequests((authorize) -> authorize
	                        .requestMatchers("/api/public/message", "/api/notices")
	                        .permitAll().anyRequest().authenticated())
	                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	                .oauth2ResourceServer((resourceServer) -> resourceServer.opaqueToken(Customizer.withDefaults()))
	                .cors(cors -> cors.configurationSource(new CorsConfigurationSource() {
	    	            @Override
	    	            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
	    	                CorsConfiguration config = new CorsConfiguration();
	    	                config.setAllowCredentials(true);
	    	                config.addAllowedOriginPattern("*");
	    	                config.addAllowedHeader("*");
	    	                config.addAllowedMethod("*");
	    	                config.addExposedHeader("Authorization");
	    	                config.setMaxAge(366000L);
	    	                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    	    	        source.registerCorsConfiguration("/files/**", config);
	    	                return config;

	    	            }
	    	        }))
	                .csrf(AbstractHttpConfigurer::disable);

	        return http.build();
	    }

	    @Bean
	    @Order(3)
	    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
	        http.authorizeHttpRequests((authorize) -> authorize
	                        .requestMatchers("/", "/index.html", "/static/**", "/assets/**", "/favicon.ico", "/images/**", "/*.ttf", // Allow
	                                // in
	                                // subdirectories
	                                "/*.woff", "/*.woff2", "/*.eot", "/*.svg", "/v3/api-docs/**", "/swagger-ui/**",
	                                "/swagger-ui.html", "/*.js", "/*.css", // Root-level CSS and JS
	                                "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html")
	                        .permitAll().anyRequest().authenticated())
	        .cors(cors -> cors.configurationSource(new CorsConfigurationSource() {
	            @Override
	            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
	                CorsConfiguration config = new CorsConfiguration();
	                config.setAllowCredentials(true);
	                config.addAllowedOriginPattern("*");
	                config.addAllowedHeader("*");
	                config.addAllowedMethod("*");
	                config.addExposedHeader("Authorization");
	                config.setMaxAge(366000L);
	                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    	        source.registerCorsConfiguration("/files/**", config);
	                return config;

	            }
	        })).formLogin(Customizer.withDefaults())
	                .logout(logout -> logout.logoutUrl("/logout"))
	                .httpBasic(Customizer.withDefaults()).csrf(AbstractHttpConfigurer::disable);

	        return http.build();
	    }
	


}