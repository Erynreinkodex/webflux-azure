package com.reinkodex.webfluxazure.leagueapi.domain;

import com.reinkodex.webfluxazure.leagueapi.dto.CreateLeagueDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class League {

    @BsonId
    private ObjectId id;

    private String name;

    private String location;

    @Field(value = "start_date")
    private LocalDateTime startDate;

    @Field(value = "end_date")
    private LocalDateTime endDate;

    public League(CreateLeagueDto dto) {
        this.id = new ObjectId();
        this.name = dto.getName();
        this.location = dto.getLocation();
        this.startDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(dto.getStartDate()), ZoneId.systemDefault());
        this.endDate = LocalDateTime.ofInstant(Instant.ofEpochSecond(dto.getEndDate()), ZoneId.systemDefault());
    }
}
