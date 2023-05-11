package com.example.football_api.dto.football.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;

@Builder
@ToString
@Getter
@Setter
public class LeagueRequest {
    @NotBlank
    @Size(min = 3, max = 30)
    private String name;
    @NotBlank
    @Size(min = 7, max = 7)
    private String season;
    @NotBlank
    @Size(min = 3, max = 3)
    private String country;
    private Set<TeamRequest> teams;
}
