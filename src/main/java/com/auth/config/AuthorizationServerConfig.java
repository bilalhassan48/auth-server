package com.auth.config;

import com.auth.service.CustomClientDetailsService;
import com.auth.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

/**
 * @author Bilal Hassan on 14-Oct-2022
 * @project auth-ms
 */

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private CustomClientDetailsService clientDetailsService;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Value("${signing.key}")
    private String signingKey;

    @Override
    public void configure(ClientDetailsServiceConfigurer clientDetailsServiceConfigurer) throws Exception {
        clientDetailsServiceConfigurer.withClientDetails(clientDetailsService);
    }

    @Bean
    public TokenStore tokenStore() {
        return new RedisTokenStore(redisConnectionFactory);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer authorizationServerEndpointsConfigurer) {
        authorizationServerEndpointsConfigurer
                .authenticationManager(authenticationManager)
                .userDetailsService(customUserDetailsService)
                .tokenStore(tokenStore())
                .accessTokenConverter(jwtAccessTokenConvert());
    }

    @Bean
    public CustomJwtTokenEnhancer jwtAccessTokenConvert() {
        CustomJwtTokenEnhancer jwtAccessTokenConverter = new CustomJwtTokenEnhancer(tokenStore());
        jwtAccessTokenConverter.setSigningKey(signingKey);
        jwtAccessTokenConverter.setJwtClaimsSetVerifier(jwtClaimsSetVerifier());
        return jwtAccessTokenConverter;
    }

    @Bean
    public CustomJwtClaimsSetVerifier jwtClaimsSetVerifier(){
        return new CustomJwtClaimsSetVerifier();
    }
}
