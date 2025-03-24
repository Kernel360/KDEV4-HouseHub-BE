package com.househub.backend.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * {
 *     "status": false,
 *     "message": input_message,
 *     "code": input_code,
 *     "errors": null,
 *     "data": null
 * }
 */
@ResponseStatus(HttpStatus.CONFLICT)
@Getter
public class AlreadyExistsException extends RuntimeException {
    private final String code;

    public AlreadyExistsException(String message, String code) {
        super(message);
        this.code = code;
    }

}
