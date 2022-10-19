package com.auth.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Bilal Hassan on 12-Oct-2022
 * @project auth-ms
 */

@Getter
@Setter
@Table(name = "clients")
@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class Client {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "secret_key")
    private String secretKey;

    @Column(name = "access_token_validity")
    private int accessTokenValiditySeconds;

    @Column(name = "refresh_token_validity")
    private int refreshTokenValiditySeconds;

    @Column(name = "authorized_grant_types")
    private String authorizedGrantTypes;

    @Column(name = "scopes")
    private String scopes;

    @Column(name = "is_active")
    private boolean isActive;

    @ManyToMany(cascade = {
            CascadeType.REFRESH
    }, fetch = FetchType.LAZY)
    @JoinTable(name = "authorization_client_group",
            joinColumns = @JoinColumn(name = "client_id"),
            inverseJoinColumns = @JoinColumn(name = "authorization_group_id")
    )
    private Set<AuthorizationGroup> authorizationGroups;


    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();

        authorizationGroups.forEach(group -> {
            group.getRoles().forEach(role -> {
                role.getAuthorities().forEach(permission -> {
                    authorities.add(new SimpleGrantedAuthority(permission.getName()));
                });
            });
        });
        return authorities;
    }
}
