package com.reinkodex.webfluxazure.gameapi.dto;


import com.reinkodex.webfluxazure.gameapi.domain.Game;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZoneOffset;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameDto {

    private String id;
    private String leagueId;
    private String opponent1;
    private String opponent2;
    private long dateTime;
    private String winningOpponent;

    public GameDto(Game game) {
        this.id = game.getId().toString();
        this.leagueId = game.getLeagueId().toString();
        this.dateTime = game.getDateTime().toInstant(ZoneOffset.UTC).toEpochMilli();
        this.opponent1 = game.getOpponent1();
        this.opponent2 = game.getOpponent2();
        this.winningOpponent = game.getWinningOpponent();
    }
}
