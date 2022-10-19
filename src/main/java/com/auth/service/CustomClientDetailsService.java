package com.auth.service;

import com.auth.entity.Authority;
import com.auth.entity.AuthorizationGroup;
import com.auth.entity.Client;
import com.auth.entity.Role;
import com.auth.repository.ClientRepository;
import org.hibernate.Hibernate;
import org.hibernate.LazyInitializationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.BaseClientDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.transaction.Transactional;
import java.util.*;

/**
 * @author Bilal Hassan on 12-Oct-2022
 * @project auth-ms
 */

@Service
public class CustomClientDetailsService implements ClientDetailsService {

    @Autowired
    private ClientRepository clientRepository;

    @Transactional
    @Override
    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        Optional<Client> clientOptional = clientRepository.findByClientId(clientId);
        Client client = clientOptional.orElseThrow(() -> new ClientRegistrationException("Invalid Client ID"));
        BaseClientDetails details = new BaseClientDetails();
        details.setClientId(client.getClientId());
        details.setClientSecret(passwordEncoder.encode(client.getSecretKey()));
        details.setAuthorizedGrantTypes(getList(client.getAuthorizedGrantTypes()));
        details.setScope(getList(client.getScopes()));
        details.setAccessTokenValiditySeconds(client.getAccessTokenValiditySeconds());
        details.setRefreshTokenValiditySeconds(client.getRefreshTokenValiditySeconds());
        initializeAuthorities(client);
        details.setAuthorities(client.getAuthorities());
        return details;
    }

    private List<String> getList(String commaSeparatedString){
        if(StringUtils.hasText(commaSeparatedString))
            return Arrays.asList(commaSeparatedString.split(","));
        return new ArrayList<>();
    }

    private Set<String> getSet(String commaSeparatedString){
        if(StringUtils.hasText(commaSeparatedString))
            return Set.of(commaSeparatedString.split(","));
        return new HashSet<String>();
    }


    private void initializeAuthorities(Client client){
        try {
            Set<AuthorizationGroup> authorizationGroups = client.getAuthorizationGroups();
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
    }
}
