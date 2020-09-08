package com.mac.offerservice.rest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OfferNotFoundException extends RuntimeException {
    public OfferNotFoundException() {
        super(HttpStatus.NOT_FOUND.getReasonPhrase());
    }
}
