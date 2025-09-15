package com.stego.repository.impl;


import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stego.entity.RegisteredClientEntity;
import com.stego.repository.RegisteredClientJpaRepository;

@Service
public class JpaRegisteredClientRepositoryImpl implements RegisteredClientRepository {

	@Autowired
	private RegisteredClientJpaRepository jpaRepository;

	@Override
	public void save(RegisteredClient registeredClient) {
		jpaRepository.save(toEntity(registeredClient));
	}

	@Override
	public RegisteredClient findById(String id) {
		return jpaRepository.findById(id).map(this::toObject).orElse(null);
	}

	@Override
	public RegisteredClient findByClientId(String clientId) {
		return jpaRepository.findByClientId(clientId).map(this::toObject).orElse(null);
	}

	private RegisteredClientEntity toEntity(RegisteredClient registeredClient) {
		RegisteredClientEntity entity = new RegisteredClientEntity();
		entity.setId(registeredClient.getId());
		entity.setClientId(registeredClient.getClientId());
		entity.setClientSecret(registeredClient.getClientSecret());
		entity.setClientName(registeredClient.getClientName());
		entity.setClientAuthenticationMethods(
				StringUtils.collectionToCommaDelimitedString(registeredClient.getClientAuthenticationMethods().stream()
						.map(ClientAuthenticationMethod::getValue).collect(Collectors.toSet())));
		entity.setAuthorizationGrantTypes(
				StringUtils.collectionToCommaDelimitedString(registeredClient.getAuthorizationGrantTypes().stream()
						.map(AuthorizationGrantType::getValue).collect(Collectors.toSet())));
		entity.setRedirectUris(StringUtils.collectionToCommaDelimitedString(registeredClient.getRedirectUris()));
		entity.setScopes(StringUtils.collectionToCommaDelimitedString(registeredClient.getScopes()));
		entity.setClientSettings(writeMap(registeredClient.getClientSettings().getSettings()));
		entity.setTokenSettings(writeMap(registeredClient.getTokenSettings().getSettings()));
		return entity;
	}

	private RegisteredClient toObject(RegisteredClientEntity entity) {
		Set<String> clientAuthenticationMethods = StringUtils
				.commaDelimitedListToSet(entity.getClientAuthenticationMethods());
		Set<String> authorizationGrantTypes = StringUtils.commaDelimitedListToSet(entity.getAuthorizationGrantTypes());
		Set<String> redirectUris = StringUtils.commaDelimitedListToSet(entity.getRedirectUris());
		Set<String> clientScopes = StringUtils.commaDelimitedListToSet(entity.getScopes());

		return RegisteredClient.withId(entity.getId()).clientId(entity.getClientId())
				.clientSecret(entity.getClientSecret()).clientName(entity.getClientName())
				.clientAuthenticationMethods(methods -> clientAuthenticationMethods.forEach(
						authenticationMethod -> methods.add(resolveClientAuthenticationMethod(authenticationMethod))))
				.authorizationGrantTypes((types) -> authorizationGrantTypes
						.forEach(grantType -> types.add(resolveAuthorizationGrantType(grantType))))
				.redirectUris(uris -> uris.addAll(redirectUris)).scopes(scopes -> scopes.addAll(clientScopes))
				.clientSettings(
						ClientSettings.builder().requireAuthorizationConsent(false).requireProofKey(true).build())
				.tokenSettings(TokenSettings.builder().accessTokenTimeToLive(Duration.ofMinutes(30))
						.refreshTokenTimeToLive(Duration.ofDays(30)).reuseRefreshTokens(false).build())
				.build();
	}

	private String writeMap(Map<String, Object> data) {
		try {
			return new ObjectMapper().writeValueAsString(data);
		} catch (Exception ex) {
			throw new IllegalArgumentException(ex.getMessage(), ex);
		}
	}

	private Map<String, Object> parseMap(String data) {
		try {
			return new ObjectMapper().readValue(data, new TypeReference<Map<String, Object>>() {
			});
		} catch (Exception ex) {
			throw new IllegalArgumentException(ex.getMessage(), ex);
		}
	}

	private ClientAuthenticationMethod resolveClientAuthenticationMethod(String clientAuthenticationMethod) {
		if (ClientAuthenticationMethod.CLIENT_SECRET_BASIC.getValue().equals(clientAuthenticationMethod)) {
			return ClientAuthenticationMethod.CLIENT_SECRET_BASIC;
		} else if (ClientAuthenticationMethod.CLIENT_SECRET_POST.getValue().equals(clientAuthenticationMethod)) {
			return ClientAuthenticationMethod.CLIENT_SECRET_POST;
		} else if (ClientAuthenticationMethod.NONE.getValue().equals(clientAuthenticationMethod)) {
			return ClientAuthenticationMethod.NONE;
		}
		return new ClientAuthenticationMethod(clientAuthenticationMethod);
	}

	private AuthorizationGrantType resolveAuthorizationGrantType(String authorizationGrantType) {
		if (AuthorizationGrantType.AUTHORIZATION_CODE.getValue().equals(authorizationGrantType)) {
			return AuthorizationGrantType.AUTHORIZATION_CODE;
		} else if (AuthorizationGrantType.CLIENT_CREDENTIALS.getValue().equals(authorizationGrantType)) {
			return AuthorizationGrantType.CLIENT_CREDENTIALS;
		} else if (AuthorizationGrantType.REFRESH_TOKEN.getValue().equals(authorizationGrantType)) {
			return AuthorizationGrantType.REFRESH_TOKEN;
		}
		return new AuthorizationGrantType(authorizationGrantType);
	}
}
