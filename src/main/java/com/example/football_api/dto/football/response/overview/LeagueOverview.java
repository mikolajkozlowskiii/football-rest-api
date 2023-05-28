package com.example.football_api.dto.football.response.overview;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@ToString
@Getter
public class LeagueOverview {
    private final Long leagueId;
    private final String name;
    private final String season;
    private final String country;
}
