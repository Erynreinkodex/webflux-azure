package com.reinkodex.webfluxazure.gameapi.domain;

import com.reinkodex.webfluxazure.gameapi.dto.CreateGameDto;
import com.reinkodex.webfluxazure.gameapi.dto.GameDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Document(value = "game")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    @BsonId
    private ObjectId id;

    private ObjectId leagueId;

    private LocalDateTime dateTime;

    private String opponent1;

    private String opponent2;

    private String winningOpponent;

    public Game(CreateGameDto dto) {
        this.id = new ObjectId();
        this.leagueId = new ObjectId(dto.getLeagueId());
        this.dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(dto.getDateTime()), ZoneId.systemDefault());
        this.opponent1 = dto.getOpponent1();
        this.opponent2 = dto.getOpponent2();
        this.winningOpponent = "";
    }
}
