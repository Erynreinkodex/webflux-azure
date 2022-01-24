package com.reinkodex.webfluxazure.leagueapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateGameDto {

    private String leagueId;
    private String opponent1;
    private String opponent2;
    private long dateTime;
}
