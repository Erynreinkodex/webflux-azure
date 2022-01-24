package com.reinkodex.webfluxazure.leagueapi.exception;

import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class LeagueNotFoundException extends ResponseStatusException {

    public LeagueNotFoundException(ObjectId id) {
        super(HttpStatus.NOT_FOUND, String.format("League id: %s was not found", id.toString()));
    }
}
