package com.reinkodex.webfluxazure.leagueapi.dto;

import com.reinkodex.webfluxazure.leagueapi.domain.League;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZoneOffset;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeagueDto {

    private String id;
    private String name;
    private String location;
    private long startDate;
    private long endDate;

    public LeagueDto(League league) {
        this.id = league.getId().toString();
        this.name = league.getName();
        this.location = league.getLocation();
        this.startDate = league.getStartDate().toInstant(ZoneOffset.UTC).toEpochMilli();
        this.endDate = league.getEndDate().toInstant(ZoneOffset.UTC).toEpochMilli();
    }
}
