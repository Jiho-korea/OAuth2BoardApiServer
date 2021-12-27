package com.config;


import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.resourceId("resource-server-rest-api").authenticationManager(authenticationManagerBean())
				.tokenExtractor(new CustomTokenExtractor());
	}

	@Bean
	public ResourceServerTokenServices tokenService() {
		CustomRemoteTokenService tokenServices = new CustomRemoteTokenService();
		return tokenServices;
	}
	
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		OAuth2AuthenticationManager authenticationManager = new OAuth2AuthenticationManager();
		authenticationManager.setTokenServices(tokenService());
		return authenticationManager;
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.httpBasic().disable().anonymous().and().authorizeRequests().antMatchers("/user/**").authenticated()
				.antMatchers("/public/**").permitAll();
	}
	
	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
	  JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
	  Resource resource = new ClassPathResource("oauth2_publickey.txt");
	  String publicKey = null;
	  try {
	      publicKey = IOUtils.toString(resource.getInputStream());
	  } catch (final IOException e) {
	      throw new RuntimeException(e);
	  }
	  converter.setVerifierKey(publicKey);
	  return converter;
	}
}

