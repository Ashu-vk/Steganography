package com.stego.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestOauth2SecurityController {

	@GetMapping("/rest/api/private/message")
	public String apiProtectedEndpoint() {
		return "JWT PROTECTED STRING";
	}

	@GetMapping("/rest/api/public/message")
	public String apiUnprotectedEndpoint() {
		return "JWT UNPROTECTED STRING";
	}

	@GetMapping("/api/test/currentUser")
	@ResponseBody
	public String apiCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			String currentUserName = authentication.getName();
			return authentication.getClass() + ": " + currentUserName;
		}
		return "anonymous";
	}

}