package com.devexperts.exceptions;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception, thrown when money transfer can not be done for some reason
 */
@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Money transfer can not be finished")
@NoArgsConstructor
public class MoneyTransferException extends RuntimeException {

    public MoneyTransferException(String reason) {
        super(reason);
    }
}
