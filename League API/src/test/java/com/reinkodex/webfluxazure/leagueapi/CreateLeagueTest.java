package com.reinkodex.webfluxazure.leagueapi;

import com.reinkodex.webfluxazure.leagueapi.client.IGameClient;
import com.reinkodex.webfluxazure.leagueapi.domain.League;
import com.reinkodex.webfluxazure.leagueapi.dto.CreateLeagueDto;
import com.reinkodex.webfluxazure.leagueapi.repository.ILeagueRepository;
import com.reinkodex.webfluxazure.leagueapi.service.LeagueService;
import com.reinkodex.webfluxazure.leagueapi.web.LeagueController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = LeagueController.class)
@Import(LeagueService.class)
public class CreateLeagueTest {

    @Autowired
    WebTestClient client;

    @MockBean
    private ILeagueRepository repository;

    @MockBean
    private IGameClient gameClient;

    @Test
    public void createLeagueSuccess() {
        var startDate = LocalDateTime.of(2022,1,15,0,0);
        var endDate = LocalDateTime.of(2022,2,15,0,0);

        var dto = new CreateLeagueDto("New League", "New Location", startDate.toInstant(ZoneOffset.UTC).toEpochMilli(),
                endDate.toInstant(ZoneOffset.UTC).toEpochMilli());

        var league = new League(dto);
        when(repository.save(any(League.class))).thenReturn(Mono.just(league));

        client.post()
                .uri("/api/v1/league")
                .bodyValue(dto)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    public void createLeagueError() {
        var startDate = LocalDateTime.of(2022,1,15,0,0);
        var endDate = LocalDateTime.of(2022,2,15,0,0);

        var dto = new CreateLeagueDto("New League", "New Location", startDate.toInstant(ZoneOffset.UTC).toEpochMilli(),
                endDate.toInstant(ZoneOffset.UTC).toEpochMilli());

        when(repository.save(any(League.class))).thenReturn(Mono.error(new IllegalArgumentException("Stuff happened")));

        client.post()
                .uri("/api/v1/league")
                .bodyValue(dto)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

}
