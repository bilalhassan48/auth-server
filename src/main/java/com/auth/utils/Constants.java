package com.auth.utils;

/**
 * @author Bilal Hassan on 12-Oct-2022
 * @project auth-ms
 */

public class Constants {


    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_AUTHORIZATION_PREFIX = "Bearer ";
    public static final String UUID_ID_KEY = "user";



    public interface ResponseMessage{
        String LOGOUT_SUCCESSFUL = "You have been successfully logged out";
        String USER_NOT_EXISTS = "User does not exist";
        String INVALID_CREDENTIALS = "Username or Password incorrect";
    }

    public enum ErrorCode {
        EXPIRED_TOKEN("0001","Token has been expired");
        String code;
        String value;

        ErrorCode(String code, String value){
            this.code=code;
            this.value=value;
        }

        public String getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }
}
