package com.auth.repository;

import com.auth.entity.AppUser;
import org.springframework.stereotype.Repository;

/**
 * @author Bilal Hassan on 11-Oct-2022
 * @project auth-ms
 */

@Repository
public interface AppUserRepository extends BaseRepository<AppUser, Long> {
    AppUser findByUsername(String username);
}
