package com.stlcbc.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ModelNotFound extends RuntimeException {
    public ModelNotFound(String model){
        super("Requested " + model + " was not found");
    }
}
