package com.reinkodex.webfluxazure.gameapi.web;

import com.reinkodex.webfluxazure.gameapi.dto.CreateGameDto;
import com.reinkodex.webfluxazure.gameapi.dto.GameDto;
import com.reinkodex.webfluxazure.gameapi.exception.GameNotFoundException;
import com.reinkodex.webfluxazure.gameapi.exception.GamesNotFoundException;
import com.reinkodex.webfluxazure.gameapi.service.GameService;
import org.bson.types.ObjectId;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "api/v1/game", produces = MediaType.APPLICATION_JSON_VALUE)
public class GameController {

    private final GameService service;

    public GameController(GameService service) {
        this.service = service;
    }

    @GetMapping
    Publisher<ResponseEntity<List<GameDto>>> getAllGames() {
        return service.findAllGames()
                .collectList()
                .flatMap(this::getAllGamesResponse)
                .onErrorResume(this::returnGameListError);
    }

    @GetMapping("/{id}")
    Publisher<ResponseEntity<GameDto>> findById(@PathVariable String id) {
        return service.findById(new ObjectId(id))
                .flatMap(this::getGameResponse)
                .onErrorResume(this::returnGameError);
    }

    @PostMapping
    Publisher<ResponseEntity<GameDto>> create(@RequestBody CreateGameDto dto) {
        return service.createGame(dto)
                .flatMap(d -> Mono.just(ResponseEntity.created(URI.create("/api/v1/game/"
                        + d.getId())).contentType(MediaType.APPLICATION_JSON).body(d)))
                .onErrorResume(this::returnGameError);
    }

    @PutMapping("/{id}/declarewinner/{winner}")
    Publisher<ResponseEntity<GameDto>> declareWinner(@PathVariable String id, @PathVariable String winner) {
        return service.declareWinner(new ObjectId(id), winner)
                .flatMap(this::getGameResponse)
                .onErrorResume(this::returnGameError);
    }

    private Mono<ResponseEntity<GameDto>> returnGameError(Throwable e) {
        if (e instanceof GameNotFoundException) {
            return Mono.error(e);
        }
        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST));
    }

    private Mono<ResponseEntity<GameDto>> getGameResponse(GameDto dto) {
        return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(dto));
    }

    private Mono<ResponseEntity<List<GameDto>>> getAllGamesResponse(List<GameDto> gameDtos) {
        return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(gameDtos));
    }

    private Mono<ResponseEntity<List<GameDto>>> returnGameListError(Throwable e) {
        if (e instanceof GamesNotFoundException) {
            return Mono.error(e);
        }
        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST));
    }

}
