package com.reinkodex.webfluxazure.leagueapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class LeaguesNotFoundException extends ResponseStatusException {

    public LeaguesNotFoundException() {
        super(HttpStatus.NOT_FOUND, "No leagues were found.");
    }
}
