package com.devexperts.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception, thrown when account not found by given credentials
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Account not found by given credentials")
public class AccountNotFoundException extends RuntimeException {
}
