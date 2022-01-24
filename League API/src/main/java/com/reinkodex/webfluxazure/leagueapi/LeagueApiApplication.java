package com.reinkodex.webfluxazure.leagueapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import reactivefeign.spring.config.EnableReactiveFeignClients;

@EnableReactiveMongoRepositories
@EnableReactiveFeignClients
@SpringBootApplication
public class LeagueApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(LeagueApiApplication.class, args);
    }

}
