package com.reinkodex.webfluxazure.gameapi.reactive;

import com.reinkodex.webfluxazure.gameapi.domain.Game;
import com.reinkodex.webfluxazure.gameapi.dto.CreateGameDto;
import com.reinkodex.webfluxazure.gameapi.dto.GameDto;
import com.reinkodex.webfluxazure.gameapi.exception.GameNotFoundException;
import com.reinkodex.webfluxazure.gameapi.exception.GamesNotFoundException;
import com.reinkodex.webfluxazure.gameapi.service.GameService;
import org.bson.types.ObjectId;
import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class GameHandler {

    private final GameService service;

    public GameHandler(GameService service) {
        this.service = service;
    }

    Mono<ServerResponse> findAll(ServerRequest request) {
        return readGameResponse(this.service.findAllGames());
    }

    Mono<ServerResponse> findById(ServerRequest request) {
        return readGameResponse(this.service.findById(getId(request)));
    }

    Mono<ServerResponse> create(ServerRequest request) {
        var flux = request.bodyToFlux(CreateGameDto.class)
                .flatMap(this.service::createGame);
        return writeGameResponse(flux);
    }

    Mono<ServerResponse> declareWinner(ServerRequest request) {
        var winner = request.pathVariable("winner");
        return readGameResponse(this.service.declareWinner(getId(request), winner));
    }

    private static Mono<ServerResponse> readGameResponse(Publisher<GameDto> game) {
        return Mono.from(game)
                .flatMap(g -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(g))
                .onErrorResume(e -> {
                    if (e instanceof GameNotFoundException || e instanceof GamesNotFoundException) {
                        return ServerResponse.notFound().build();
                    } else {
                        return ServerResponse.badRequest().build();
                    }
                });
    }

    private static Mono<ServerResponse> writeGameResponse(Publisher<GameDto> game) {
        return Mono.from(game)
                .flatMap(g -> ServerResponse.created(URI.create("/api/v1/game/" + g.getId()))
                        .contentType(MediaType.APPLICATION_JSON).body(g, GameDto.class))
                .onErrorResume(e -> ServerResponse.badRequest().build());
    }

    private static ObjectId getId(ServerRequest r) {
        return new ObjectId(r.pathVariable("id"));
    }

}
