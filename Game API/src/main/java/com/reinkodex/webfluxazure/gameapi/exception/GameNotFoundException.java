package com.reinkodex.webfluxazure.gameapi.exception;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class GameNotFoundException extends ResponseStatusException {

    public GameNotFoundException(ObjectId id) {
        super(HttpStatus.NOT_FOUND, String.format("Game with  id: %s was not found", id));
    }
}
