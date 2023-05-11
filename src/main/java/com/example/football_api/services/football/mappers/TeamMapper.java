package com.example.football_api.services.football.mappers;

import com.example.football_api.dto.football.request.TeamRequest;
import com.example.football_api.dto.football.response.TeamResponse;
import com.example.football_api.entities.football.Team;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class TeamMapper {

    public TeamResponse map(Team team){
        return TeamResponse.builder()
                .id(team.getId())
                .name(team.getName())
                .build();
    }

    public Team map(TeamRequest teamRequest){
        return Team.builder()
                .name(teamRequest.getName())
                .build();
    }
}
