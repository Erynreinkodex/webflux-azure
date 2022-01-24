package com.reinkodex.webfluxazure.gameapi;

import com.reinkodex.webfluxazure.gameapi.domain.Game;
import com.reinkodex.webfluxazure.gameapi.repository.IGameRepository;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.Set;

@Slf4j
@SpringBootApplication
@EnableReactiveMongoRepositories
public class GameApiApplication {

    /**
     * Default constructor for the project.
     *
     * @param args An array of all relevant arguments.
     */
    public static void main(final String[] args) {
        SpringApplication.run(GameApiApplication.class, args);
    }


//    @Bean
//    ApplicationRunner init(IGameRepository gameRepository) {
//        var game1 = new Game(new ObjectId(), "New Game", LocalDateTime.of(2022, 1, 30, 12, 0), "username",
//                "username2", "");
//        var game2 = new Game(new ObjectId(), "New Game", LocalDateTime.of(2022, 2, 3, 10, 0), "username2",
//                "username3", "");
//
//        var games = Set.of(game1, game2);
//        return args -> {
//            gameRepository.deleteAll()
//                    .thenMany(Flux.just(games).flatMap(gameRepository::saveAll))
//                    .thenMany(gameRepository.findAll())
//                    .subscribe(g -> log.info("Saving: " + g.getId()));
//        };
//    }

}
