package com.reinkodex.webfluxazure.gameapi.classic;

import com.reinkodex.webfluxazure.gameapi.AbstractGameTest;
import com.reinkodex.webfluxazure.gameapi.domain.Game;
import com.reinkodex.webfluxazure.gameapi.dto.GameDto;
import com.reinkodex.webfluxazure.gameapi.repository.IGameRepository;
import com.reinkodex.webfluxazure.gameapi.service.GameService;
import com.reinkodex.webfluxazure.gameapi.web.GameController;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = GameController.class)
@Import(GameService.class)
public class DeclareWinnerTest extends AbstractGameTest {

    @Autowired
    WebTestClient client;

    @MockBean
    private IGameRepository repository;

    @Test
    public void declareWinnerSuccess() {
        var openGame = createOpenGame();
        var id = openGame.getId().toString();

        when(repository.findById(any(ObjectId.class))).thenReturn(Mono.just(openGame));
        when(repository.save(Mockito.any(Game.class))).thenReturn(Mono.just(openGame));

        var winningOpponent = openGame.getOpponent1();
        var responseDto = client.put()
                .uri("/api/v1/game/" + id + "/declarewinner/" + winningOpponent)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(GameDto.class)
                .returnResult()
                .getResponseBody();

        var openGameDto = createOpenGameDto();
        Assertions.assertEquals(openGameDto.getDateTime(), responseDto.getDateTime());
        Assertions.assertEquals(openGameDto.getOpponent1(), responseDto.getOpponent1());
        Assertions.assertEquals(openGameDto.getOpponent2(), responseDto.getOpponent2());
        Assertions.assertEquals(winningOpponent, responseDto.getWinningOpponent());
    }

    @Test
    public void declareWinnerGameNotFound() {
        var id = new ObjectId().toString();
        when(repository.findById(any(ObjectId.class))).thenReturn(Mono.empty());

        var winningOpponent = "Not Found";

        client.put()
                .uri("/api/v1/game/" + id + "/declarewinner/" + winningOpponent)
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    public void declareWinnerGameInvalid() {
        var openGame = createOpenGame();
        var id = openGame.getId().toString();

        when(repository.findById(any(ObjectId.class))).thenReturn(Mono.just(openGame));

        var winningOpponent = "Invalid Opponent";

        client.put()
                .uri("/api/v1/game/" + id + "/declarewinner/" + winningOpponent)
                .exchange()
                .expectStatus()
                .isBadRequest();
    }
}
