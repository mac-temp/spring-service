package com.mac.offerservice.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class OfferAlreadyExistsException extends RuntimeException {
    public OfferAlreadyExistsException() {
        super(HttpStatus.CONFLICT.getReasonPhrase());
    }
}
