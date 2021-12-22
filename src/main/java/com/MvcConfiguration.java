package com;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfiguration implements WebMvcConfigurer {    
    @Bean
    public WebClient webClient() {
		return WebClient.builder().baseUrl("http://localhost:8099").filter(ExchangeFilterFunctions
                .basicAuthentication("oauth2-jwt-client", "pass")).build();
    }
}
