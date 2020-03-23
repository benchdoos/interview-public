package com.devexperts.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Given account has not enough money balance to proceed")
public class NotEnoughBalanceException extends RuntimeException {
}
