package com.example.football_api.dto.football.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;
import java.util.Set;

@Builder
@ToString
@Getter
public class PlayerResponse {
    private Long id;
    private Set<PlayerTeamHistoryResponse> teamsHistory;
    private String firstName;
    private String lastName;
    private String position;
    private int height;
    private int weight;
    private boolean strongerFeet;
    private Date birthDate;
}
