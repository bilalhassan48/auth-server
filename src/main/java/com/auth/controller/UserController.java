/*
package com.auth.controller;

import com.auth.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

*/
/**
 * @author Bilal Hassan on 14-Oct-2022
 * @project auth-ms
 *//*


@RestController
@RequestMapping("/api")
@Slf4j
public class UserController {

    @Autowired
    private ConsumerTokenServices consumerTokenServices;

    @GetMapping(value = "/logout")
    public String logout(HttpServletRequest request){
        String jwt = getJwtFromRequest(request);
        consumerTokenServices.revokeToken(jwt);
        return Constants.ResponseMessage.LOGOUT_SUCCESSFUL;
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader(Constants.HEADER_AUTHORIZATION);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(Constants.HEADER_AUTHORIZATION_PREFIX)) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}
*/
