package com.example.football_api.dto.football.response.overview;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Set;

@Builder
@ToString
@Getter
public class TeamOverview {
    private Long teamId;
    private String name;
    private Set<PlayerOverview> players;

}
