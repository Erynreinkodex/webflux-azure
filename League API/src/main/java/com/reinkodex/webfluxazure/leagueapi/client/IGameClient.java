package com.reinkodex.webfluxazure.leagueapi.client;

import com.reinkodex.webfluxazure.leagueapi.dto.CreateGameDto;
import com.reinkodex.webfluxazure.leagueapi.dto.GameDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(name = "game", url = "http://game:8080")
public interface IGameClient {
    
    @GetMapping("/api/v1/game")
    Flux<GameDto> findGamesByLeague(String leagueId);
    
    @PostMapping("/api/v1/game")
    Mono<GameDto> createGame(CreateGameDto dto);
}
