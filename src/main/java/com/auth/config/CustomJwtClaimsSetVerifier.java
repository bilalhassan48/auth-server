package com.auth.config;

import com.auth.exception.TokenValidationException;
import com.auth.utils.Constants;
import org.springframework.security.oauth2.provider.token.store.JwtClaimsSetVerifier;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Bilal Hassan on 17-Oct-2022
 * @project auth-ms
 */

public class CustomJwtClaimsSetVerifier implements JwtClaimsSetVerifier {
    @Override
    public void verify(Map<String, Object> claims) throws TokenValidationException {
        if (claims.containsKey("exp") && claims.get("exp") instanceof Long) {
            Long longValue = (Long) claims.get("exp");
            Date expiration = new Date(TimeUnit.SECONDS.toMillis(longValue));
            if(isExpired(expiration))
                throw new TokenValidationException(Constants.ErrorCode.EXPIRED_TOKEN);
        }
    }

    public boolean isExpired(Date expiration) {
        return expiration != null && expiration.before(new Date());
    }
}
