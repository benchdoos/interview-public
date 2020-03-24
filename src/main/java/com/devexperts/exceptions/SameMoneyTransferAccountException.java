package com.devexperts.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception, thrown when money transfer is initiated from and to the same account.
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Source account and target account can not be the same. " +
        "Money transfer is invalid.")
public class SameMoneyTransferAccountException extends MoneyTransferException {
}
