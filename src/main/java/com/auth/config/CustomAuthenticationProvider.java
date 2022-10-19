package com.auth.config;

import com.auth.entity.AppUser;
import com.auth.entity.AuthorizationGroup;
import com.auth.service.CustomUserDetailsService;
import com.auth.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Bilal Hassan on 14-Oct-2022
 * @project auth-ms
 */

@Service
//@Transactional
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        AppUser appUser = (AppUser) customUserDetailsService.loadUserByUsername(username);//getUserFromDB(username);
        if(appUser == null) {
            throw new BadCredentialsException(Constants.ResponseMessage.USER_NOT_EXISTS);
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = getUserAuthToken(username, password, appUser);
        if(usernamePasswordAuthenticationToken != null) {
            //String token = tokenProvider.createToken(appUser);
            usernamePasswordAuthenticationToken.setDetails(appUser.getId());
            return usernamePasswordAuthenticationToken;
        } else {
            throw new BadCredentialsException(Constants.ResponseMessage.INVALID_CREDENTIALS);
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private UsernamePasswordAuthenticationToken getUserAuthToken(String username, String password, AppUser appUser) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = null;

        if(matchCredentials(username, password, appUser)) {
            Collection<GrantedAuthority> grantedAuthorities = getAuthorities(appUser);
            usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(new User(username, password, grantedAuthorities), password, grantedAuthorities);
        }
        return usernamePasswordAuthenticationToken;
    }

    private Collection getAuthorities(AppUser appUser) {
        Set<AuthorizationGroup> authorizationGroups = appUser.getAuthorizationGroups();

        var permissions = new HashSet<SimpleGrantedAuthority>();
        authorizationGroups.forEach(group -> {
            group.getRoles().forEach(role -> {
                role.getAuthorities().forEach(permission -> {
                    permissions.add(new SimpleGrantedAuthority(permission.getName()));
                });
            });
        });

        return permissions;
    }

    private boolean matchCredentials(String usernameToMatch, String passwordToMatch, AppUser appUser) {
        if(appUser == null) {
            return false;
        }
        return matchUsername(usernameToMatch, appUser) && matchPassword(passwordToMatch, appUser);
    }

    private boolean matchUsername(String usernameToMatch, AppUser appUser) {
        return usernameToMatch.equalsIgnoreCase(appUser.getUsername());
    }

    private boolean matchPassword(String passwordToMatch, AppUser appUser) {
        return passwordEncoder.matches(passwordToMatch, appUser.getPassword());
    }
}
