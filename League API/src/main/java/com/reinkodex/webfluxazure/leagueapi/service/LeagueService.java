package com.reinkodex.webfluxazure.leagueapi.service;

import com.reinkodex.webfluxazure.leagueapi.domain.League;
import com.reinkodex.webfluxazure.leagueapi.dto.CreateLeagueDto;
import com.reinkodex.webfluxazure.leagueapi.dto.LeagueDto;
import com.reinkodex.webfluxazure.leagueapi.exception.LeagueNotFoundException;
import com.reinkodex.webfluxazure.leagueapi.exception.LeaguesNotFoundException;
import com.reinkodex.webfluxazure.leagueapi.repository.ILeagueRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class LeagueService {

    private final ILeagueRepository repository;

    public LeagueService(ILeagueRepository repository){
        this.repository = repository;
    }

    public Flux<LeagueDto> findAllLeagues() {
        return this.repository.findAll()
                .switchIfEmpty(Mono.error(new LeaguesNotFoundException()))
                .map(LeagueDto::new);
    }

    public Mono<LeagueDto> findLeagueById(ObjectId id) {
        return this.repository.findById(id)
                .switchIfEmpty(Mono.error(new LeagueNotFoundException(id)))
                .map(LeagueDto::new)
                .onErrorResume(Mono::error);
    }

    public Mono<LeagueDto> createLeague(CreateLeagueDto dto) {
        return Mono.just(dto)
                .map(League::new)
                .flatMap(this.repository::save)
                .map(LeagueDto::new)
                .onErrorResume(Mono::error);
    }
}
