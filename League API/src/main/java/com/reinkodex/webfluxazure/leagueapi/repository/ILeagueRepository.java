package com.reinkodex.webfluxazure.leagueapi.repository;

import com.reinkodex.webfluxazure.leagueapi.domain.League;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ILeagueRepository extends ReactiveMongoRepository<League, ObjectId> {
}
