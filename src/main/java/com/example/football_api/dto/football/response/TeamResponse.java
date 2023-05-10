package com.example.football_api.dto.football.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@ToString
@Getter
public class TeamResponse {
    private Long id;
    private String name;
}
