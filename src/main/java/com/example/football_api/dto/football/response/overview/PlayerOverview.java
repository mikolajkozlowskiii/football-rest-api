package com.example.football_api.dto.football.response.overview;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@ToString
@Getter
public class PlayerOverview {
    private Long playerId;
    private String firstName;
    private String lastName;
    private String position;
    private int age;
}
