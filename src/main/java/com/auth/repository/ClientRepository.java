package com.auth.repository;

import com.auth.entity.Client;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.QueryHint;
import java.util.Optional;

/**
 * @author Bilal Hassan on 12-Oct-2022
 * @project auth-ms
 */

public interface ClientRepository extends BaseRepository<Client, Integer> {

    @QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
    Optional<Client> findByClientId(String clientId);
}
