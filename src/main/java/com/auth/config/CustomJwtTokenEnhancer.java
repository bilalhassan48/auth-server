package com.auth.config;

import com.auth.entity.AppUser;
import com.auth.service.CustomUserDetailsService;
import com.auth.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bilal Hassan on 16-Oct-2022
 * @project auth-ms
 */

@Slf4j
public class CustomJwtTokenEnhancer extends JwtAccessTokenConverter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    private TokenStore tokenStore;

    public CustomJwtTokenEnhancer(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        this.additionalInformation(accessToken, authentication);
        return super.enhance(accessToken, authentication);
    }

    public Map<String, Object> decode(String token) {
        log.info("decoding token");
        return super.decode(token);
    }

    private void additionalInformation(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        if(null != authentication && null != authentication.getUserAuthentication()){
            Map<String, Object> additionalInformation = new HashMap<>();
            Object principal = authentication.getUserAuthentication().getPrincipal();
            String userId = null;
            if(principal instanceof User){
                userId = customUserDetailsService.getUserId(((User) principal).getUsername());
            }else if (principal instanceof AppUser){
                userId = ((AppUser) principal).getId().toString();
            }
            additionalInformation.put(Constants.UUID_ID_KEY,userId);
            ((DefaultOAuth2AccessToken)accessToken ).setAdditionalInformation(additionalInformation);
        }
    }
}
