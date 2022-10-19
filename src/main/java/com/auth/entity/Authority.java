package com.auth.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Bilal Hassan on 11-Oct-2022
 * @project auth-ms
 */


@Entity
@Table(name = "authority")
@Getter
@Setter
public class Authority implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "is_admin")
    private Boolean isAdmin;
}
