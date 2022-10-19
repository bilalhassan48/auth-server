package com.auth.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @author Bilal Hassan on 11-Oct-2022
 * @project auth-ms
 */

@Getter
@Setter
@Table(name = "authorization_group")
@Entity
public class AuthorizationGroup implements Serializable {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Column(name="name")
    private String name;

    @Column(name="description")
    private String description;

    @ManyToMany(cascade = {
            CascadeType.REFRESH
    }, fetch = FetchType.LAZY)
    @JoinTable(name = "authorization_user_group",
            joinColumns = @JoinColumn(name = "authorization_group_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @JsonBackReference
    private Set<AppUser> users;

    @ManyToMany(cascade = {
            CascadeType.REFRESH
    }, fetch = FetchType.LAZY)
    @JoinTable(name = "authorization_client_group",
            joinColumns = @JoinColumn(name = "authorization_group_id"),
            inverseJoinColumns = @JoinColumn(name = "client_id")
    )
    @JsonBackReference
    private Set<Client> clients;

    @ManyToMany(cascade = {
            CascadeType.REFRESH
    })
    @JoinTable(name = "group_role",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;
}
