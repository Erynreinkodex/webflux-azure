package com.reinkodex.webfluxazure.gameapi.service;

import com.reinkodex.webfluxazure.gameapi.domain.Game;
import com.reinkodex.webfluxazure.gameapi.dto.CreateGameDto;
import com.reinkodex.webfluxazure.gameapi.dto.GameDto;
import com.reinkodex.webfluxazure.gameapi.exception.GameNotFoundException;
import com.reinkodex.webfluxazure.gameapi.exception.GamesNotFoundException;
import com.reinkodex.webfluxazure.gameapi.repository.IGameRepository;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GameService {

    /**
     * DI for Game Repository.
     */
    private final IGameRepository gameRepository;

    /**
     * Constructor.
     *
     * @param gameRepo DI for the game repository.
     */
    public GameService(final IGameRepository gameRepo) {
        this.gameRepository = gameRepo;
    }

    /**
     * Finds all games form the database.
     *
     * @return A list of all games.
     */
    public Flux<GameDto> findAllGames() {
        return gameRepository.findAll()
                .switchIfEmpty(Mono.error(new GamesNotFoundException()))
                .map(GameDto::new);
    }

    /**
     * Finds the game by Id.
     *
     * @param id The id to be searched in the database.
     * @return The game.
     */
    public Mono<GameDto> findById(ObjectId id) {
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new GameNotFoundException(id)))
                .map(GameDto::new);
    }

    /**
     * Creates a game from the information given in the interface.
     *
     * @param dto A class with all relevant information.
     * @return The created class.
     */
    public Mono<GameDto> createGame(CreateGameDto dto) {
        return Mono.just(dto)
                .flatMap(this::mapDtoToGame)
                .flatMap(gameRepository::save)
                .map(GameDto::new)
                .onErrorResume(Mono::error);
    }

    private Mono<Game> mapDtoToGame(CreateGameDto dto) {
        if (dto.getLeagueId() == null || dto.getLeagueId().isEmpty()) {
            return Mono.error(new IllegalArgumentException("League ID is empty or null"));
        }

        return Mono.just(new Game(dto));
    }

    /**
     * Updates the game with a winner.
     *
     * @param id     The id of the game to be updated.
     * @param winner The winner.
     * @return The game with the updated winner.
     */
    public Mono<GameDto> declareWinner(final ObjectId id, final String winner) {
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new GameNotFoundException(id)))
                .onErrorResume(Mono::error)
                .flatMap(game -> getGame(game, winner))
                .flatMap(gameRepository::save)
                .map(GameDto::new);
    }

    private Mono<Game> getGame(Game game, String winner) {
        // Return error if winner is neither opponent one nor opponent 2.
        if (!winner.equals(game.getOpponent1()) && !winner.equals(game.getOpponent2())) {
            return Mono.error(new IllegalArgumentException("Winner is not part of the game"));
        }

        game.setWinningOpponent(winner);
        return Mono.just(game);
    }
}
