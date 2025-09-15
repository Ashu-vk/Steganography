package com.stego.filter;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.util.StringUtils;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CustomUsernameAuthFilter implements Filter {

	private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

	private final SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder
			.getContextHolderStrategy();

	public static final String AUTHENTICATION_SCHEME_BASIC = "Basic";
	private Charset credentialsCharset = StandardCharsets.UTF_8;
//
//	private final UserDetailsService userDetailsService;
//
//	public CustomLoginFilter(UserDetailsService userDetailsService) {
//		this.userDetailsService = userDetailsService;
//	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		if (httpRequest.getServletPath().equals("/oauth2/authorize")) {
			String credentials = request.getParameter("credentials");
			if (credentials != null) {
				byte[] base64Token = credentials.getBytes(StandardCharsets.UTF_8);
				byte[] decoded;
				try {
					decoded = Base64.getDecoder().decode(base64Token);
					String token = new String(decoded, credentialsCharset);
					int delim = token.indexOf(":");
					if (delim == -1) {
						throw new BadCredentialsException("Invalid basic authentication token");
					}
					String email = token.substring(0, delim);
					if (email.toLowerCase().contains("test")) {
						httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
						return;
					}
//					UserDetails userDetails = userDetailsService.loadUserByUsername(email);
//					System.err.println("inside validatoer");
//					System.err.println(userDetails.getUsername());
//
//					Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
//							userDetails.getAuthorities());
					SecurityContext context = securityContextHolderStrategy.createEmptyContext();
//					context.setAuthentication(authentication);
					securityContextRepository.saveContext(context, httpRequest, httpResponse);
					securityContextHolderStrategy.setContext(context);
				} catch (IllegalArgumentException e) {
					throw new BadCredentialsException("Failed to decode basic authentication token");
				}
			}

		}
		filterChain.doFilter(request, response);

	}

}
