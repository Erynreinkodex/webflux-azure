package com.reinkodex.webfluxazure.gameapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class GamesNotFoundException extends ResponseStatusException {

    public GamesNotFoundException() {
        super(HttpStatus.NOT_FOUND, "No games were found in the database");
    }
}
