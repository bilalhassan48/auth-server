package com.auth.dto;

import lombok.*;

/**
 * @author Bilal Hassan on 13-Oct-2022
 * @project auth-ms
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ErrorInfo {
    String code;
    String message;
    String trackCode;

    @Override
    public String toString() {
        return "ErrorInfo{" + "code='" + code + '\'' + ", message='" + message + '\'' + ", trackCode='" + trackCode + '}';
    }
}
