package com.reinkodex.webfluxazure.gameapi.reactive;

import com.reinkodex.webfluxazure.gameapi.AbstractGameTest;
import com.reinkodex.webfluxazure.gameapi.dto.GameDto;
import com.reinkodex.webfluxazure.gameapi.repository.IGameRepository;
import com.reinkodex.webfluxazure.gameapi.service.GameService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest
@Import({GameService.class, GameEndpointConfiguration.class, GameHandler.class})
public class FindGameReactiveTest extends AbstractGameTest {

    @Autowired
    WebTestClient client;

    @MockBean
    private IGameRepository repository;

    @Test
    public void testFindAllGames() {
        var defaultGame = createDefaultGame();
        var openGame = createOpenGame();

        when(repository.findAll()).thenReturn(Flux.just(defaultGame, openGame));

        client.get()
                .uri("/api/v1/reactive/game")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(GameDto.class);
    }

    @Test
    public void findAllNotFound() {
        when(repository.findAll()).thenReturn(Flux.empty());
        client.get()
                .uri("/api/v1/reactive/game")
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    public void findByIdSuccess() {
        var defaultGame = createDefaultGame();
        var id = defaultGame.getId().toString();

        when(repository.findById(any(ObjectId.class))).thenReturn(Mono.just(defaultGame));

        var responseDto = client.get()
                .uri("/api/v1/reactive/game/" + id)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(GameDto.class)
                .returnResult()
                .getResponseBody();

        var defaultDto = createDefaultDto();

        Assertions.assertEquals(defaultDto.getDateTime(), responseDto.getDateTime());
        Assertions.assertEquals(defaultDto.getOpponent1(), responseDto.getOpponent1());
        Assertions.assertEquals(defaultDto.getOpponent2(), responseDto.getOpponent2());
        Assertions.assertEquals(defaultDto.getWinningOpponent(), responseDto.getWinningOpponent());
    }

    @Test
    public void findByIdNotFound() {
        var id = new ObjectId().toString();

        when(repository.findById(any(ObjectId.class))).thenReturn(Mono.empty());

        client.get()
                .uri("/api/v1/reactive/game/" + id)
                .exchange()
                .expectStatus()
                .isNotFound();
    }
}
