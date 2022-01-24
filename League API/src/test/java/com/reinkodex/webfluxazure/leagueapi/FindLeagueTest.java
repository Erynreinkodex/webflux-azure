package com.reinkodex.webfluxazure.leagueapi;

import com.reinkodex.webfluxazure.leagueapi.client.IGameClient;
import com.reinkodex.webfluxazure.leagueapi.domain.League;
import com.reinkodex.webfluxazure.leagueapi.dto.LeagueDto;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = LeagueController.class)
@Import(LeagueService.class)
public class FindLeagueTest {

    @Autowired
    WebTestClient client;

    @MockBean
    private ILeagueRepository repository;

    @MockBean
    private IGameClient gameClient;

    @Test
    public void findAllLeagues() {
        var stdLeague = createStandardLeague();
        var secondLeague = createSecondLeague();

        when(repository.findAll()).thenReturn(Flux.just(stdLeague, secondLeague));

        var leagues = client.get()
                .uri("/api/v1/league")
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(LeagueDto.class)
                .getResponseBody()
                .collectList()
                .block();

        Assertions.assertEquals(2, leagues.size());

        var defaultDto = createStdLeagueDto();
        var resultDto = leagues.get(0);

        Assertions.assertEquals(defaultDto.getName(), resultDto.getName());
        Assertions.assertEquals(defaultDto.getLocation(), resultDto.getLocation());
        Assertions.assertEquals(defaultDto.getStartDate(), resultDto.getStartDate());
        Assertions.assertEquals(defaultDto.getEndDate(), resultDto.getEndDate());

        var secondDto = createSecondLeagueDto();
        resultDto = leagues.get(1);

        Assertions.assertEquals(secondDto.getName(), resultDto.getName());
        Assertions.assertEquals(secondDto.getLocation(), resultDto.getLocation());
        Assertions.assertEquals(secondDto.getStartDate(), resultDto.getStartDate());
        Assertions.assertEquals(secondDto.getEndDate(), resultDto.getEndDate());
    }

    @Test
    public void findAllLeaguesNotFound() {
        when(repository.findAll()).thenReturn(Flux.empty());

        client.get()
                .uri("/api/v1/league")
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    public void findSingleLeague() {
        var stdLeague = createStandardLeague();
        var id = stdLeague.getId().toString();

        when(repository.findById(any(ObjectId.class))).thenReturn(Mono.just(stdLeague));

        var resultDto = client.get()
                .uri("/api/v1/league/" + id)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(LeagueDto.class)
                .returnResult()
                .getResponseBody();

        var defaultDto = createStdLeagueDto();

        assert resultDto != null;
        Assertions.assertEquals(defaultDto.getName(), resultDto.getName());
        Assertions.assertEquals(defaultDto.getLocation(), resultDto.getLocation());
        Assertions.assertEquals(defaultDto.getStartDate(), resultDto.getStartDate());
        Assertions.assertEquals(defaultDto.getEndDate(), resultDto.getEndDate());
    }

    @Test
    public void findSingleLeagueNotFound() {
        var id = new ObjectId().toString();

        when(repository.findById(any(ObjectId.class))).thenReturn(Mono.empty());

        client.get()
                .uri("/api/v1/league/" + id)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    private League createStandardLeague() {
        return new League(new ObjectId(), "Game League", "Bamberg", LocalDateTime.of(2022, 1, 1, 0, 0),
                LocalDateTime.of(2023, 1, 1, 0, 0));
    }

    private League createSecondLeague() {
        return new League(new ObjectId(), "Second League", "München", LocalDateTime.of(2022, 1, 1, 0, 0),
                LocalDateTime.of(2022, 6, 1, 0, 0));
    }

    private LeagueDto createStdLeagueDto() {
        return new LeagueDto(createStandardLeague());
    }

    private LeagueDto createSecondLeagueDto() {
        return new LeagueDto(createSecondLeague());
    }
}
