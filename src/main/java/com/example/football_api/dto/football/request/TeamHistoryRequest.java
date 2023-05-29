package com.example.football_api.dto.football.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Builder
@ToString
@Getter
public class TeamHistoryRequest {
    @NotNull
    private Long teamId;
    @NotNull
    private LocalDate start;
    @NotNull
    private LocalDate ends;
}
