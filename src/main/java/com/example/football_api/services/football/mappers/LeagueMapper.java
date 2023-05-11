package com.example.football_api.services.football.mappers;

import com.example.football_api.dto.football.request.LeagueRequest;
import com.example.football_api.dto.football.response.LeagueResponse;
import com.example.football_api.entities.football.League;
import com.example.football_api.services.football.LeagueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LeagueMapper {
    private final TeamMapper teamMapper;
    public League map(LeagueRequest leagueRequest){
        return League.builder()
                .country(leagueRequest.getCountry())
                .name(leagueRequest.getName())
                .season(leagueRequest.getSeason())
                .teams(
                        Optional
                        .ofNullable(leagueRequest.getTeams())
                        .orElse(Collections.emptySet())
                        .stream().map(teamMapper::map)
                        .collect(Collectors.toSet())
                )
                .build();
    }

    public LeagueResponse map(League league){
        LeagueResponse leagueResponse = LeagueResponse.builder()
                .id(league.getId())
                .country(league.getCountry())
                .name(league.getName())
                .season(league.getSeason())
                .teams(Optional
                        .ofNullable(league.getTeams())
                        .orElse(Collections.emptySet())
                        .stream().map(teamMapper::map).collect(Collectors.toSet())
                )
                .build();
        System.out.println(leagueResponse);
        return leagueResponse;
    }

    public League update(League league, LeagueRequest updateInfo){
        Optional.ofNullable(updateInfo.getName())
                .filter(name -> !name.isEmpty())
                .ifPresent(league::setName);

        Optional.ofNullable(updateInfo.getCountry())
                .filter(country -> !country.isEmpty())
                .ifPresent(league::setCountry);

        Optional.ofNullable(updateInfo.getSeason())
                .filter(season -> !season.isEmpty())
                .ifPresent(league::setSeason);

        return league;
    }

}
