package com.reinkodex.webfluxazure.leagueapi.web;

import com.reinkodex.webfluxazure.leagueapi.client.IGameClient;
import com.reinkodex.webfluxazure.leagueapi.dto.CreateGameDto;
import com.reinkodex.webfluxazure.leagueapi.dto.CreateLeagueDto;
import com.reinkodex.webfluxazure.leagueapi.dto.GameDto;
import com.reinkodex.webfluxazure.leagueapi.dto.LeagueDto;
import com.reinkodex.webfluxazure.leagueapi.exception.LeagueNotFoundException;
import com.reinkodex.webfluxazure.leagueapi.exception.LeaguesNotFoundException;
import com.reinkodex.webfluxazure.leagueapi.service.LeagueService;
import lombok.AllArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/league")
@AllArgsConstructor
public class LeagueController {

    private final LeagueService service;

    private final IGameClient gameClient;

    @GetMapping
    public Mono<ResponseEntity<List<LeagueDto>>> findAll() {
        return this.service.findAllLeagues()
                .collectList()
                .flatMap(this::getAllLeaguesResponse)
                .onErrorResume(this::returnLeagueListError);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<LeagueDto>> findById(@PathVariable String id) {
        return this.service.findLeagueById(new ObjectId(id))
                .flatMap(this::getLeagueResponse)
                .onErrorResume(this::returnLeagueError);
    }

    @PostMapping
    public Mono<ResponseEntity<LeagueDto>> create(@RequestBody CreateLeagueDto dto) {
        return this.service.createLeague(dto)
                .flatMap(d -> Mono.just(ResponseEntity.created(URI.create("/api/v1/league/"
                        + d.getId())).contentType(MediaType.APPLICATION_JSON).body(d)))
                .onErrorResume(this::returnLeagueError);
    }

    @PostMapping("/{id}/creategame")
    public Mono<ResponseEntity<GameDto>> createLeagueGame(@PathVariable String id, @RequestBody CreateGameDto dto) {
        return Mono.just(dto)
                .flatMap(d -> {
                    d.setLeagueId(id);
                    return Mono.just(d);
                })
                .flatMap(gameClient::createGame)
                .flatMap(d -> Mono.just(ResponseEntity.created(URI.create("/api/v1/game/" + d.getId()))
                        .contentType(MediaType.APPLICATION_JSON).body(d)))
                .onErrorResume(e -> Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST)));
    }

    private Mono<ResponseEntity<LeagueDto>> returnLeagueError(Throwable e) {
        if (e instanceof LeagueNotFoundException) {
            return Mono.error(e);
        }
        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST));
    }

    private Mono<ResponseEntity<LeagueDto>> getLeagueResponse(LeagueDto dto) {
        return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(dto));
    }

    private Mono<ResponseEntity<List<LeagueDto>>> getAllLeaguesResponse(List<LeagueDto> gameDtos) {
        return Mono.just(ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(gameDtos));
    }

    private Mono<ResponseEntity<List<LeagueDto>>> returnLeagueListError(Throwable e) {
        if (e instanceof LeaguesNotFoundException) {
            return Mono.error(e);
        }
        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST));
    }
}
