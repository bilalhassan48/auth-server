package com.auth.exception;

import com.auth.dto.ErrorInfo;
import com.auth.utils.Constants;

/**
 * @author Bilal Hassan on 14-Oct-2022
 * @project auth-ms
 */

public class TokenValidationException extends RuntimeException {
	private ErrorInfo errorCode;

	public TokenValidationException(Constants.ErrorCode errorCode) {
		super(errorCode.getValue());
		this.errorCode = new ErrorInfo(errorCode.getCode(),errorCode.getValue(),null);
	}
}
