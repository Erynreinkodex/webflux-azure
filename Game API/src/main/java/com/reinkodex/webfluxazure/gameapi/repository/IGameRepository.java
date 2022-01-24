package com.reinkodex.webfluxazure.gameapi.repository;

import com.reinkodex.webfluxazure.gameapi.domain.Game;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IGameRepository extends ReactiveMongoRepository<Game, ObjectId> {
}
