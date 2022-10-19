package com.auth.service;

import com.auth.entity.AppUser;
import com.auth.entity.Authority;
import com.auth.entity.AuthorizationGroup;
import com.auth.entity.Role;
import com.auth.repository.AppUserRepository;
import org.hibernate.Hibernate;
import org.hibernate.LazyInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

/**
 * @author Bilal Hassan on 12-Oct-2022
 * @project auth-ms
 */

@Transactional
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> appUserOptional = Optional.ofNullable(appUserRepository.findByUsername(username));
        if(appUserOptional.isPresent()) {
            try {
                Set<AuthorizationGroup> authorizationGroups = appUserOptional.get().getAuthorizationGroups();
                Hibernate.initialize(authorizationGroups);
                authorizationGroups.forEach(authorizationGroup -> {
                    Set<Role> roles = authorizationGroup.getRoles();
                    Hibernate.initialize(roles);
                    roles.forEach(role -> {
                        Set<Authority> authorities = role.getAuthorities();
                        Hibernate.initialize(authorities);
                    });
                });
            } catch (LazyInitializationException e) {
                e.printStackTrace();
            }
            return appUserOptional.get();
        }
        return null;
    }

    public String getUserId(String username){
        Optional<AppUser> appUserOptional = Optional.ofNullable(appUserRepository.findByUsername(username));
        if(appUserOptional.isPresent()){
            return appUserOptional.get().getId().toString();
        }
        return "";
    }
}
