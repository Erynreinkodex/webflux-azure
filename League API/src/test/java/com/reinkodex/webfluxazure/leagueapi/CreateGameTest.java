package com.reinkodex.webfluxazure.leagueapi;

import com.reinkodex.webfluxazure.leagueapi.client.IGameClient;
import com.reinkodex.webfluxazure.leagueapi.domain.League;
import com.reinkodex.webfluxazure.leagueapi.dto.CreateGameDto;
import com.reinkodex.webfluxazure.leagueapi.dto.GameDto;
import com.reinkodex.webfluxazure.leagueapi.repository.ILeagueRepository;
import com.reinkodex.webfluxazure.leagueapi.service.LeagueService;
import com.reinkodex.webfluxazure.leagueapi.web.LeagueController;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
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

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = LeagueController.class)
@Import(LeagueService.class)
public class CreateGameTest {

    @Autowired
    WebTestClient client;

    @MockBean
    private ILeagueRepository repository;

    @MockBean
    private IGameClient gameClient;

    @Test
    public void createMatchSuccess() {
        var stdLeague = createStandardLeague();
        var leagueId = stdLeague.getId().toString();

        var dateTime = LocalDateTime.of(2022,1,15,0,0);
        var longDateTime = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
        var dto = new CreateGameDto(leagueId, "Opponent 1", "Opponent 2", longDateTime);

        var gameDto = new GameDto(new ObjectId().toString(), leagueId, "Opponent 1", "Opponent 2", longDateTime, "");
        when(gameClient.createGame(dto)).thenReturn(Mono.just(gameDto));

        var resultDto = client.post()
                .uri("/api/v1/league/" + leagueId + "/creategame")
                .bodyValue(dto)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(GameDto.class)
                .returnResult()
                .getResponseBody();

        assert resultDto != null;

        Assertions.assertEquals(gameDto.getOpponent1(), resultDto.getOpponent1());
        Assertions.assertEquals(gameDto.getOpponent2(), resultDto.getOpponent2());
        Assertions.assertEquals(gameDto.getDateTime(), resultDto.getDateTime());
        Assertions.assertEquals(gameDto.getWinningOpponent(), resultDto.getWinningOpponent());
    }

    @Test
    public void createMatchFailure() {
        var stdLeague = createStandardLeague();
        var leagueId = stdLeague.getId().toString();

        var dateTime = LocalDateTime.of(2022,1,15,0,0);
        var longDateTime = dateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
        var dto = new CreateGameDto(leagueId, "Opponent 1", "Opponent 2", longDateTime);

        when(gameClient.createGame(dto)).thenReturn(Mono.error(new IllegalArgumentException("Error creating game")));

        client.post()
                .uri("/api/v1/league/" + leagueId + "/creategame")
                .bodyValue(dto)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }

    private League createStandardLeague() {
        return new League(new ObjectId(), "Game League", "Bamberg", LocalDateTime.of(2022, 1, 1, 0, 0),
                LocalDateTime.of(2023, 1, 1, 0, 0));
    }
}
