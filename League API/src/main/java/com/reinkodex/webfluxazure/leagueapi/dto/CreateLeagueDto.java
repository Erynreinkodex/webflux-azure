package com.reinkodex.webfluxazure.leagueapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateLeagueDto {

    public String name;
    public String location;
    public long startDate;
    public long endDate;
}
