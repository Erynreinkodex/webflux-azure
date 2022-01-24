package com.reinkodex.webfluxazure.gameapi;

import com.reinkodex.webfluxazure.gameapi.domain.Game;
import com.reinkodex.webfluxazure.gameapi.dto.GameDto;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

public abstract class AbstractGameTest {

    public Game createDefaultGame() {
        var dateTime = LocalDateTime.of(2022, 1, 30, 12, 0);
        return new Game(new ObjectId(), "Default Game", dateTime, "Opponnent 1", "Opponent 2", "Opponent 1");
    }

    public GameDto createDefaultDto() {
        return new GameDto(createDefaultGame());
    }

    public Game createOpenGame() {
        var dateTime = LocalDateTime.of(2022, 2, 15, 17, 0);
        return new Game(new ObjectId(), "Open Game", dateTime, "Opponnent 3", "Opponent 2", "");
    }

    public GameDto createOpenGameDto() {
        return new GameDto(createOpenGame());
    }
}
