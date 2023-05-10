package com.example.football_api.services.football.mappers;

import com.example.football_api.dto.football.request.LeagueRequest;
import com.example.football_api.dto.football.request.TeamRequest;
import com.example.football_api.dto.football.response.TeamResponse;
import com.example.football_api.entities.football.League;
import com.example.football_api.entities.football.Team;
import com.example.football_api.exceptions.football.TeamNotFoundException;
import com.example.football_api.repositories.football.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class TeamMapper {
    private final TeamRepository teamRepository;
    public TeamResponse map(Team team){
        return TeamResponse.builder()
                .id(team.getId())
                .name(team.getName())
                .build();
    }

    public Team map(TeamRequest teamRequest){
        String teamName = teamRequest.getName();
        try{
            return teamRepository
                    .findByName(teamName)
                    .orElseThrow(()->new TeamNotFoundException(teamName));
        }catch (TeamNotFoundException ex){
            return Team.builder()
                    .name(teamName)
                    .build();
        }
    }
}
